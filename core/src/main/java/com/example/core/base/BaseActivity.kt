package com.example.core.base

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.core.component.dialog.CustomProgressDialog
import com.example.core.config.BaseConfig
import com.example.core.utils.PageMessageUtil

abstract class BaseActivity<T: ViewBinding>(private val bindingFactory: (LayoutInflater) -> T): AppCompatActivity(){

    /**
     * view binding of page
     */
    lateinit var binding: T

    /** use this attribute to show progress dialog */
    private val progressDialog: CustomProgressDialog by lazy { CustomProgressDialog(this) }

    /**
     * default message type
     */
    open var messageType = BaseConfig.messageType

    open var isDisplayHomeAsUp = false

    override fun onStart() {
        super.onStart()

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        onBeforeViewCreated()

        configureToolbar()
        binding.onViewCreated(savedInstanceState)
    }

    /**
     * init toolbar resource id if exist
     */
    @IdRes
    open fun toolbarId(): Int? = null

    /**
     * set event of view after prepare all condition on page
     * @param savedInstanceState: state of page
     * @see AppCompatActivity.onCreate
     */
    abstract fun T.onViewCreated(savedInstanceState: Bundle?)

    open fun getMessageUtil(context: Context): PageMessageUtil? = PageMessageUtil(this@BaseActivity, binding.root)

    open fun onBeforeViewCreated() { }

    /**
     * show/hide progress dialog
     * @param isShow param show/hide
     */
    fun showProgressDialog(isShow: Boolean) = progressDialog.showDialog(isShow)

    /**
     * Configure current toolbar if exist
     */
    open fun configureToolbar(){
        toolbarId()?.apply {
            setSupportActionBar(findViewById(this))
            supportActionBar?.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp)
        }
    }
}