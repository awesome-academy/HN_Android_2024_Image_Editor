package com.example.imageEditor.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.imageEditor.R

abstract class BaseActivity<BINDING : ViewDataBinding> : AppCompatActivity(), OnListenProcess {
    private lateinit var _binding: BINDING
    val binding get() = _binding
    private val loadingDialog by lazy { setupProgressDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, getContentLayout())
        setContentView(_binding.root)
        initView()
        initListener()
    }

    abstract fun initListener()

    abstract fun initView()

    abstract fun getContentLayout(): Int

    override fun onProgress() {
        loadingDialog.show()
    }

    override fun onSuccess() {
        loadingDialog.dismiss()
    }

    override fun onError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    private fun setupProgressDialog(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setCancelable(false)

        val myLayout = LayoutInflater.from(this)
        val dialogView: View = myLayout.inflate(R.layout.fragment_progress_dialog, null)

        builder.setView(dialogView)

        val dialog: AlertDialog = builder.create()
        val window: Window? = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }
}
