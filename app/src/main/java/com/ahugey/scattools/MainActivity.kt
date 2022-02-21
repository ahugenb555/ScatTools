package com.ahugey.scattools

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private var mBtnDie: MaterialButton? = null
    private lateinit var mViewModel: ScatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(ScatViewModel::class.java)
        mBtnDie = findViewById(R.id.scat_btn_die)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        mViewModel.getViewState().observe(this, Observer { state ->
            mBtnDie?.text = state.dieText.toString()
        })
    }

    private fun setupListeners() {
        mBtnDie?.setOnClickListener(
            View.OnClickListener { _ ->
                mViewModel.onDieTapped()
            }
        )
    }
}