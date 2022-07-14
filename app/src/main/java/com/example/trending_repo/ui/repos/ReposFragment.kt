package com.example.trending_repo.ui.repos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.map
import com.example.trending_repo.R
import com.example.trending_repo.databinding.FragmentReposBinding
import com.example.trending_repo.ui.base.BaseCoreFragment
import com.example.trending_repo.ui.repos.adapter.ReposAdapter
import com.example.trending_repo.ui.repos.adapter.ReposLoadStateAdapter
import java.util.*
import javax.inject.Inject


class ReposFragment : BaseCoreFragment() {
    private lateinit var binding: FragmentReposBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ReposViewModel
    var repoAdapter: ReposAdapter? = null
    var orientationKey = "ORIENTATION_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ReposViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ReposViewModel::class.java)

        repoAdapter = ReposAdapter() {
            it.isSelected = !it.isSelected
            repoAdapter?.notifyDataSetChanged()
        }

        binding.apply {

            recycler.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = repoAdapter?.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { repoAdapter?.retry() },
                    footer = ReposLoadStateAdapter { repoAdapter?.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            btnRetry.setOnClickListener {
                repoAdapter?.retry()
            }
        }

        viewModel.repoNames.observe(viewLifecycleOwner) {
            if (it != null) {
                repoAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
                binding.emptyTv.visibility = View.GONE
            }
            else {
                binding.recycler.visibility = View.GONE
                binding.emptyTv.visibility = View.VISIBLE
            }
        }

        viewModel.filteredRepoNames.observe(viewLifecycleOwner) {
            if (it != null) {
                repoAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
                binding.emptyTv.visibility = View.GONE
            }
            else {
                binding.recycler.visibility = View.GONE
                binding.emptyTv.visibility = View.VISIBLE
            }
        }

        if (savedInstanceState?.get(orientationKey) != true) {
            viewModel.getRepoNames()
        }

        repoAdapter?.addLoadStateListener { loadState ->
            binding.apply {
                progress.visibility =
                    if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
                recycler.visibility =
                    if (loadState.source.refresh is LoadState.NotLoading) View.VISIBLE else View.GONE
                btnRetry.visibility =
                    if (loadState.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
                error.visibility =
                    if (loadState.source.refresh is LoadState.Error) View.VISIBLE else View.GONE

                // no results found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    repoAdapter?.itemCount ?: 0 < 1
                ) {
                    recycler.visibility = View.GONE
                    emptyTv.visibility = View.VISIBLE
                } else {
                    emptyTv.visibility = View.GONE
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_repos, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        val searchAutoComplete: SearchView.SearchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        searchAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.search(p0.toString())
            }

        })
        searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, itemIndex, _ ->
                val queryString = adapterView.getItemAtPosition(itemIndex) as String
                searchAutoComplete.setText(
                    String.format(
                        getString(R.string.search_query),
                        queryString
                    )
                )
                binding.recycler.scrollToPosition(0)
                viewModel.search(queryString)
                searchView.clearFocus()
                (activity as AppCompatActivity).supportActionBar?.title =
                    queryString.capitalize(Locale.ROOT)
            }
    }

    companion object {
        const val TAG = "ReposFragment"
        fun newInstance() = ReposFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(orientationKey, true);
        super.onSaveInstanceState(outState)
    }
}