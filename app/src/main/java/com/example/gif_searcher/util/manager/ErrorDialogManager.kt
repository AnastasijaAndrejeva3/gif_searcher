package com.example.gif_searcher.util.manager

import android.content.Context
import androidx.appcompat.app.AlertDialog

class ErrorDialogManager(private val context: Context) {
    private var errorDialog: AlertDialog? = null


    fun showErrorDialog(message: String) {

        if (errorDialog?.isShowing == true) return

        errorDialog = AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                errorDialog = null
            }
            .create()

        errorDialog?.show()
    }

    fun dismissErrorDialog() {
        errorDialog?.dismiss()
        errorDialog = null
    }
}
