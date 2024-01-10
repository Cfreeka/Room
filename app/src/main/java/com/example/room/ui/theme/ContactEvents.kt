package com.example.room.ui.theme

sealed interface ContactEvents{
    object SaveContact : ContactEvents
    data class SetFirstName(val firstName: String): ContactEvents
    data class SetLastName(val lastName: String): ContactEvents
    data class SetPhoneNumber(val phoneNumber: String): ContactEvents
    object ShowDialog: ContactEvents
    object HideDialog: ContactEvents
    data class SortContacts(val sortTYpe : SortType): ContactEvents
    data class DeleteContacts(val contact:Contact):ContactEvents
}