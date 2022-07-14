# TrendingRepos
Android App that lists all the cool repositories that are trending in Github

## API
Since there is no official API for Trending Repositories (it is one of the internal GitHub API’s),
I have decided to use GitHub Search API and sort the repositories by their stars.

## Android App

### Tech stack: 

Minimum SDK level 23

Kotlin based + Coroutines for asynchronous.

Dagger2 (alpha) for dependency injection.

JetPack

LiveData - notify domain layer data to views.

Lifecycle - dispose of observing data when lifecycle state changes.

ViewModel - UI related data holder, lifecycle aware.

View Binding - declaratively bind observable data to UI elements.

Architecture
MVVM Architecture (View - DataBinding - ViewModel - Model)
Repository pattern

Glide - loading images.

Retrofit & OkHttp3 - construct the REST APIs and paging network data.

Material-Components - Material design components like ripple animation, cardView.
