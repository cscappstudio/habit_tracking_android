package com.cscmobi.habittrackingandroid.presentation.di

import com.cscmobi.habittrackingandroid.MyApplication
import com.cscmobi.habittrackingandroid.data.repository.CollectionRepository
import com.cscmobi.habittrackingandroid.data.repository.CollectionRepositoryImpl
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepositoryImpl
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import com.cscmobi.habittrackingandroid.data.repository.HomeRepositoryImpl
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.DetailTaskViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.database.Dao
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
            HomeViewModel(get(),get(),get())
    }

    viewModel {
        CollectionViewModel(get(),get(),androidContext())
    }

    viewModel {
        DetailTaskViewModel(get())
    }
}

val databaseModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single<Dao> { get<AppDatabase>().dao() }
    single<DatabaseRepository> { DatabaseRepositoryImpl(get()) }
}

val repositoryModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(androidContext())
    }
    single<CollectionRepository> { CollectionRepositoryImpl(androidContext())}

}

