package com.example.trending_repo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.trending_repo.R
import com.example.trending_repo.di.ViewModelFactory
import com.example.trending_repo.ui.base.BaseCoreActivity
import com.example.trending_repo.ui.repos.ReposFragment
import javax.inject.Inject

class MainActivity : BaseCoreActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var repoFragment: ReposFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repoFragment = if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(ReposFragment.TAG) as ReposFragment
        } else
            ReposFragment.newInstance()
        showReposFragment()
    }

    private fun showReposFragment() {
        switchFragment({
            repoFragment
        }, ReposFragment.TAG)
    }

    private fun switchFragment(getFragment: () -> Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_page_container, getFragment(), tag).commitAllowingStateLoss()
    }

}