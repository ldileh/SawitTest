package com.example.core.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.core.config.BaseConfig
import com.example.core.utils.ext.viewLifecycleLazy

@Suppress("unused")
abstract class BaseFragment<T: ViewBinding>(bindingFactory: (View) -> T): Fragment(){

    /**
     * view binding of page
     */
    @Suppress
    val binding: T by viewLifecycleLazy{ bindingFactory(requireView()) }

    /**
     * default message type of page
     */
    open var messageType = BaseConfig.messageType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBeforeViewCreated()
        binding.onViewCreated(savedInstanceState)
    }

    /**
     * set event of view after prepare all condition on page (fragment)
     * @param savedInstanceState state of page
     * @see AppCompatActivity.onCreate
     */
    abstract fun T.onViewCreated(savedInstanceState: Bundle?)

    open fun onBeforeViewCreated() { }
}