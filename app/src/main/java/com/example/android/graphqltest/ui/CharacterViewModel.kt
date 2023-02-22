package com.example.android.graphqltest.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.example.android.graphqltest.api.callWrappers.ResultWrapper
import com.example.android.graphqltest.api.mappers.toCharacters
import com.example.android.graphqltest.api.models.Character
import com.example.android.graphqltest.api.repository.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharactersRepository
) : ViewModel() {

    init {
        getCharactersList()
    }

    private val _charactersList: MutableLiveData<List<Character>> =
        MutableLiveData()
    val charactersList: LiveData<List<Character>> = _charactersList

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    fun getCharactersList() {
        viewModelScope.launch {
            when (val dataRetrieved = repository.getCharacters()) {
                is ResultWrapper.GenericError -> {
                    _uiEvents.send(UiEvent.ErrorMessage(dataRetrieved.errorMessage ?: "Error"))
                }
                is ResultWrapper.NetworkError -> {
                    _uiEvents.send(UiEvent.ErrorMessage(dataRetrieved.cause ?: "Error"))
                }
                is ResultWrapper.Success -> {
                    _charactersList.postValue(dataRetrieved.value.data?.toCharacters())
                }
            }
        }
    }

    sealed class UiEvent {
        class ErrorMessage(val message: String) : UiEvent()
    }
}