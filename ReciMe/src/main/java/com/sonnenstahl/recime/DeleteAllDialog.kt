package com.sonnenstahl.recime

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.dp
import com.sonnenstahl.recime.utils.data.Ingredient

/**
 * small popup when the delete option in [Fridge] is selected without selecting any dishes
 *
 * @param ingredients the whole list that will be deleted
 * @param onDismiss to do when exiting the screen and deleting all the ingredients
 */
@Composable
fun DeleteAllDialog(
    ingredients: SnapshotStateList<Ingredient>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirm Deletion")
        },
        text = {
            Text("Are you sure you want to delete all of your ingredients?")
        },
        confirmButton = {
            Button(
                onClick = {
                    ingredients.clear()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Yes, Delete") // This will now correctly display
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel") // This will now correctly display
            }
        }
    )
}