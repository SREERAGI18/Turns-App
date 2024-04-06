package com.turnsapp.screens

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.turnsapp.R

open class BasicDialog(
    private val title:String,
    private val message:String,
    private val onDismissed:() -> Unit = {}
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = MaterialAlertDialogBuilder(it, R.style.MaterialAlertDialog_rounded)

            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton("OK") { dialogInterface, p1 ->
                dismiss()
            }

            isCancelable = true

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissed()
    }

}