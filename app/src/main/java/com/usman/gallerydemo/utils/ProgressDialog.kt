package com.usman.gallerydemo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.usman.gallerydemo.databinding.LayoutProgressDialogBinding

class ProgressDialog {
    companion object {
        fun progressDialog(context: Context, cancellable: Boolean = false): Dialog {
            val dialog = Dialog(context)
            return dialog.apply {
                val binding = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context))
                setContentView(binding.root)
                setCancelable(cancellable)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }
}