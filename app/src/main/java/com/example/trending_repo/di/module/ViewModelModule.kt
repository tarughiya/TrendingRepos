package com.example.trending_repo.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trending_repo.di.ViewModelFactory
import com.example.trending_repo.ui.repos.ReposViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    abstract fun bindsRepoViewModel(reposViewModel: ReposViewModel?): ViewModel?

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory?): ViewModelProvider.Factory?
}