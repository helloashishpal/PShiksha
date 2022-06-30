package com.example.pshiksha.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.pshiksha.R

class LoaderBuilder(context: Context?) {
    private val dialog: Dialog?
    private val view: View
    init {
        dialog = Dialog(context!!)
        val layoutInflater = LayoutInflater.from(context)
        view = layoutInflater.inflate(R.layout.loader_layout, null)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }
    fun setTitle(title: String): LoaderBuilder {
        (view.findViewById<View>(R.id.loader_title) as TextView).text = title
        return this
    }

    fun show() {
        if (dialog == null) return
        dialog.setContentView(view)
        dialog.create()
        dialog.show()
    }

    fun hide() {
        if (dialog == null) return
        dialog.hide()
    }
}