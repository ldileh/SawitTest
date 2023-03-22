package com.example.core.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.core.utils.PageMessageUtil
import com.example.core.utils.ext.forceCloseApp

@Suppress("unused")
abstract class BaseFragmentVM<T: ViewBinding, A: BaseViewModel>(bindingFactory: (View) -> T): BaseFragment<T>(bindingFactory) {

    /**
     * Initialize current model on fragment
     */
    abstract val viewModel: A

    /**
     * message util of page
     */
    private lateinit var messageUtil: PageMessageUtil

    override fun onBeforeViewCreated() {
        super.onBeforeViewCreated()

        messageUtil = PageMessageUtil(requireContext(), binding.root)
        viewModel.vmObserver()
    }

    /**
     * handle global event from BaseViewModel.
     * 1. show message
     * 2. handle token expired from remote data
     */
    open fun A.vmObserver() = apply {
        eventMessage.observe(viewLifecycleOwner) { msg ->
            messageUtil.showMessage(
                messageType,
                msg
            )
        }

        eventRestart.observe(viewLifecycleOwner) { result -> if (result) getBaseActivity()?.forceCloseApp() }
    }

    /**
     * show/hide progress dialog
     * @param isShow param show/hide
     */
    open fun showProgressDialog(isShow: Boolean) = getBaseActivity()?.showProgressDialog(isShow)

    private fun getBaseActivity() = activity?.let { if (it is BaseActivityVM<*, *>) it else null }
}