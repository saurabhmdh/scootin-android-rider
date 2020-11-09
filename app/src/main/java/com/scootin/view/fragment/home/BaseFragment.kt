package com.scootin.view.fragment.home

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.interfaces.IFullScreenListener

open class BaseFragment: Fragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)
    lateinit var fullScreenListener: IFullScreenListener
    private var dialog: AlertDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fullScreenListener = context as IFullScreenListener

    }
    fun showLoading(cancellable: Boolean = true, touchCancellable: Boolean = true) {
        activity?.apply {
            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.view_progress)
            dialog = builder.create().apply {
                setCancelable(cancellable)
                setCanceledOnTouchOutside(touchCancellable)
                show()
            }
        }
    }

    fun dismissLoading() {
        dialog?.apply {
            this.dismiss()
            dialog = null
        }
    }
}