package com.example.gt_6m_2_1

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gt_6m_2_1.MainActivity.Companion.SEND_IMAGES_TO_RESULT_ACTIVITY_KEY
import com.example.gt_6m_2_1.databinding.ActivityResultBinding
import com.example.gt_6m_2_1.model.Photo

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadImages()

        initListener()
    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadImages() {
        val media = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelableArrayList(
                SEND_IMAGES_TO_RESULT_ACTIVITY_KEY,
                Photo::class.java
            )
        } else {
            intent.extras?.getParcelableArrayList(SEND_IMAGES_TO_RESULT_ACTIVITY_KEY)
        }
        Log.d("gg", media.toString())
        val adapter = media?.let { SelectedImagesAdapter(it.toMutableList()) }
        binding.recyclerView.adapter = adapter
    }
}