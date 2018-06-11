package com.example.a14512.weatherkotlin.network.RxUtil

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.weatherkotlin.network.RxUtil.exception.ApiException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * @author 14512 on 2018/3/17
 */
abstract class ApiObserver<T> : DialogInterface.OnCancelListener, Observer<T> {
    private var contextWeakReference: WeakReference<Context>? = null
    private var dialog: ProgressDialog? = null
    private var cancelable = false  //是否可以取消
    private var isShow: Boolean = false
    private lateinit var disposable: Disposable

    constructor()

    constructor(context: Context, isShow: Boolean, cancelable: Boolean) {
        this.contextWeakReference = WeakReference(context)
        this.cancelable = cancelable
        this.isShow = isShow
        initProgressDialog()
    }

    constructor(context: Context, isShow: Boolean, cancelable: Boolean, message: String) {
        this.contextWeakReference = WeakReference(context)
        this.cancelable = cancelable
        this.isShow = isShow
        initProgressDialog(message)
    }

    constructor(context: Context, cancelable: Boolean, dialog: ProgressDialog) {
        this.contextWeakReference = WeakReference(context)
        this.dialog = dialog
        this.cancelable = cancelable
        dialog.setCancelable(cancelable)
        dialog.setOnCancelListener(this)
    }

    private fun initProgressDialog() {
        val context = this.contextWeakReference!!.get()
        if (dialog == null && context != null) {
            dialog = ProgressDialog(context)
            dialog?.setMessage("加载中...")
            dialog?.setCancelable(cancelable)
            dialog?.setOnCancelListener(this)
        }
    }


    private fun initProgressDialog(message: String) {
        val context = this.contextWeakReference?.get()
        if (dialog == null && context != null) {
            dialog = ProgressDialog(context)
            dialog?.setMessage(message)
            dialog?.setCancelable(cancelable)
            dialog?.setOnCancelListener(this)
        }
    }

    private fun showProgressDialog() {
        val context = this.contextWeakReference!!.get()
        if (dialog == null || context == null || !isShow) {
            return
        }
        if (!dialog?.isShowing!!) {
            dialog?.show()
        }
    }

    private fun dismissProgressDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        }
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        showProgressDialog()
    }

    override fun onCancel(dialog: DialogInterface) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onError(e: Throwable) {
        val ex = e as ApiException
        val context = this.contextWeakReference?.get()
        if (context != null) {
            ToastUtil.show(ex.getDisplayMessage())
        }
        dismissProgressDialog()
    }

    override fun onComplete() {
        dismissProgressDialog()
    }
}