package com.silver.fox.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.view.R
import com.silver.fox.viewext.createTest
import kotlinx.android.synthetic.main.view_activity_coordinator.*

class CoordinatorLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_coordinator)
        recyclerView.createTest(this)
    }
}
