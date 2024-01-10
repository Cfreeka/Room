package com.example.room.ui.theme


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao: ContactDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.FIRST_NAME -> dao.getContactsOrderedByFirstName()
                SortType.LAST_NAME -> dao.getContactsOrderedByLastName()
                SortType.PHONE_NUMBER -> dao.getContactsOrderedByPhoneNumber()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())

    val state = combine(_state, _contacts, _sortType) { state, contacts, sortType ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ContactState())


    fun onEvent(event: ContactEvents) {
        when (event) {
            is ContactEvents.DeleteContacts ->
                viewModelScope.launch {
                    dao.deleteContacts(event.contact)
                }

            ContactEvents.HideDialog ->
                _state.update {
                    it.copy(isAddingContact = false)
                }

            ContactEvents.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
                    return
                }
                val contact = Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch {
                    dao.upsertContacts(contact)
                }
                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lastName = "",
                        phoneNumber = ""
                    )
                }

            }


            is ContactEvents.SetFirstName ->
                _state.update {
                    it.copy(firstName = event.firstName)
                }

            is ContactEvents.SetLastName ->
                _state.update {
                    it.copy(lastName = event.lastName)
                }

            is ContactEvents.SetPhoneNumber ->
                _state.update {
                    it.copy(phoneNumber = event.phoneNumber)
                }

            ContactEvents.ShowDialog ->
                _state.update {
                    it.copy(isAddingContact = true)
                }

            is ContactEvents.SortContacts ->
                _sortType.value = event.sortTYpe
        }

    }

}