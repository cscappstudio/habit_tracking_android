package com.cscmobi.habittrackingandroid.presentation.di

import com.cscmobi.habittrackingandroid.data.repository.CollectionRepository
import com.cscmobi.habittrackingandroid.data.repository.CollectionRepositoryImpl
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import com.cscmobi.habittrackingandroid.data.repository.HomeRepositoryImpl
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
            HomeViewModel(get())
    }

    viewModel {
        CollectionViewModel(get())

    }
}

val repositoryModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl()
    }
    single<CollectionRepository> { CollectionRepositoryImpl()}
}