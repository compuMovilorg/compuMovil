//package com.example.myapplication.utils
//
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.text.input.TextFieldValue
//import com.example.myapplication.ui.mainuser.EditingState
//
//@Composable
//fun EditReviewDialog(
//    editing: EditingState,
//    onTextChange: (String) -> Unit,
//    onCancel: () -> Unit,
//    onConfirm: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onCancel,
//        title = { Text("Editar reseña") },
//        text = {
//            OutlinedTextField(
//                value = editing.newText,
//                onValueChange = onTextChange,
//                minLines = 4
//            )
//        },
//        confirmButton = {
//            TextButton(onClick = onConfirm) { Text("Guardar") }
//        },
//        dismissButton = {
//            TextButton(onClick = onCancel) { Text("Cancelar") }
//        }
//    )
//}
