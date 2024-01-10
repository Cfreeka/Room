package com.example.room.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialog(
    state: ContactState,
    onEvent: (ContactEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ContactEvents.HideDialog)
        },
        title = {
            Text(text = "Add Contact")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.firstName,
                    onValueChange = {
                        onEvent(
                            ContactEvents.SetFirstName(it)
                        )
                    },
                    placeholder = {
                        Text(text = "First name")
                    }
                )
                TextField(
                    value = state.lastName,
                    onValueChange = {
                        onEvent(
                            ContactEvents.SetLastName(it)
                        )

                    },
                    placeholder = {
                        Text(text = "Second name")
                    }
                )
                TextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onEvent(
                            ContactEvents.SetPhoneNumber(it)
                        )
                    },
                    placeholder = {
                        Text(text = "Phone number")
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(ContactEvents.SaveContact)
            }) {
                Text(text = "Save Contact")
            }

        },
        dismissButton = {
            Button(onClick = {
                onEvent(ContactEvents.HideDialog)
            }) {
                Text(text = "Cancel")

            }
        }
    )
}
