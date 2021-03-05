package com.usman.gallerydemo.ui.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<VM : ViewModel, DB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {

    protected lateinit var context: Context

    protected lateinit var binding: DB

    protected abstract val viewModel: VM

    protected abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.apply {
            setVariable(getBindingVariable(), viewModel)
            lifecycleOwner = this@BaseActivity
        }
    }
}