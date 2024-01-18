package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.repository.CollectionRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionViewModel constructor(private val repository: CollectionRepository) :
    BaseViewModel() {

    val userIntent = Channel<CollectionIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<CollectionState>(CollectionState.Idle)
    val state: StateFlow<CollectionState>
        get() = _state


    init {
        handleIntent()
    }
    fun setUp(){}

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when(it) {
                    is CollectionIntent.FetchCollection -> fetchCollections()
                    is CollectionIntent.PassItemCollection -> passCollectionItem(it.data)
                    else -> {}
                }
            }
        }
    }
    private fun fetchCollections() {
        viewModelScope.launch {
            _state.value = CollectionState.Loading
            _state.value = try {
                CollectionState.Collections(repository.getListLocalCollection())
            } catch (e: Exception) {
                CollectionState.Collections(mutableListOf())
            }
        }
    }

    fun passCollectionItem(data: HabitCollection) {
        _state.value = CollectionState.Collection(data)
    }

}