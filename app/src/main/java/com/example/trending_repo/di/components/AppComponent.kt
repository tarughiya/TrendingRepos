package com.example.trending_repo.di.components

import android.app.Application
import com.example.trending_repo.TrendingApplication
import com.example.trending_repo.di.builder.ActivityBuilderModule
import com.example.trending_repo.di.builder.FragmentBuilderModule
import com.example.trending_repo.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidInjectionModule::class, ActivityBuilderModule::class, FragmentBuilderModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(trendingApplication: TrendingApplication)
}