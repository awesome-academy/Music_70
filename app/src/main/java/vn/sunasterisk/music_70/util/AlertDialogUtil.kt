package vn.sunasterisk.music_70.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_message.view.*
import vn.sunasterisk.music_70.R

object AlertDialogUtil {

    @SuppressLint("InflateParams")
    fun showMessageDialog(
        context: Context,
        message: String,
        cancelable: Boolean,
        onButtonClicked: () -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)
        val dialog = createDialog(context, view,cancelable)
        view.apply {
            textMessage.text = message
            buttonConfirm.setOnClickListener {
                dialog.cancel()
                onButtonClicked.invoke()
            }
        }
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun createDialog(context: Context, view: View, cancelable: Boolean): Dialog {
        val dialog = AlertDialog.Builder(context)
            .setCancelable(cancelable)
            .setView(view)
            .create()
        dialog.window?.apply {
            decorView?.setBackgroundResource(android.R.color.transparent)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        return dialog
    }
}
