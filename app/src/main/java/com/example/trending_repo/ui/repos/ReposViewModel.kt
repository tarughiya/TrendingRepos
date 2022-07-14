package com.example.trending_repo.ui.repos

import android.text.TextUtils
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.trending_repo.data.GithubRepository
import com.example.trending_repo.data.model.Repo
import javax.inject.Inject

class ReposViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {
    companion object {
        private const val DEFAULT_QUERY = "language:Kotlin"
    }

    private var _repoNames = MutableLiveData<PagingData<Repo>>()
    private val _searchQuery = MutableLiveData<String?>()

    val repoNames: LiveData<PagingData<Repo>> = _repoNames
    private val searchQuery: LiveData<String?> = _searchQuery


    val filteredRepoNames: LiveData<PagingData<Repo>> =
        Transformations.switchMap(searchQuery) { it ->
            if (TextUtils.isEmpty(it)) {
                return@switchMap MutableLiveData<PagingData<Repo>>().apply {
                    this.value = repoNames.value
                }

            } else {
                return@switchMap MutableLiveData<PagingData<Repo>>().apply {
                    this.value = repoNames.value?.filter {
                        it.name.contains(searchQuery.value!!)
                    }
                }
            }
        }

    fun search(query: String?) {
        _searchQuery.postValue(query)
    }

    fun getRepoNames() {
        repository.getSearchResults(DEFAULT_QUERY).cachedIn(viewModelScope).observeForever {
            _repoNames.value = it
        }
    }
}