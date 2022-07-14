package com.example.trending_repo.di.builder

import com.example.trending_repo.ui.repos.ReposFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeReposFragment(): ReposFragment?
}