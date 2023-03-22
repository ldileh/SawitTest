package com.example.sawit.ui.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.core.base.BaseActivity
import com.example.sawit.databinding.ActivityMainResultBinding
import com.example.sawit.ui.model.MainModel

class MainResultActivity :
    BaseActivity<ActivityMainResultBinding>(ActivityMainResultBinding::inflate) {

    private val data: MainModel? by lazy {
        intent.getParcelableExtra(EXTRA_DATA)
    }

    override fun ActivityMainResultBinding.onViewCreated(savedInstanceState: Bundle?) {
        tieText.setText(data?.writtenText ?: "")
        tieDistance.setText(data?.distance ?: "")
        tieEstimatedTime.setText(data?.estimatedTime ?: "")
    }

    override fun configureToolbar() {
        super.configureToolbar()

        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    companion object{

        private const val EXTRA_DATA = "extra_data"

        fun showPage(context: Context, data: MainModel?){
            context.startActivity(
                Intent(context, MainResultActivity::class.java)
                    .apply {
                        putExtra(EXTRA_DATA, data)
                    }
            )
        }
    }
}