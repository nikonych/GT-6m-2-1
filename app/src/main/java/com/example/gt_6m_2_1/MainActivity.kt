package com.example.gt_6m_2_1

import android.Manifest.*
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.gt_6m_2_1.databinding.ActivityMainBinding
import com.example.gt_6m_2_1.model.Photo


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var media: MutableList<Photo> = arrayListOf()
    private var adapter: AllImagesAdapter? = null

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                Log.d("gg", "Number of items selected: ${uris.size}")
                media = uris.map {Photo(name=it)} as MutableList<Photo>
                loadMedia()
            } else {
                Log.d("gg", "No media selected")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        loadMedia()
        initListeners()


    }

    private fun initListeners() {
        binding.btnDone.setOnClickListener {
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putParcelableArrayListExtra(SEND_IMAGES_TO_RESULT_ACTIVITY_KEY, adapter?.getSelectedImages())
            startActivity(intent)
        }
    }

    private fun loadMedia() {
        Log.d("gg", "Loading media")
        adapter = AllImagesAdapter(media, this::updateSelectedImagesCount)
        binding.recyclerView.adapter = adapter
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            media = getAllShownImagesPath()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                media = getAllShownImagesPath()
            } else {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }
        }
    }

    private fun getAllShownImagesPath(): MutableList<Photo> {
        val listOfImages = mutableListOf<Photo>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val selection =
            "${MediaStore.Images.Media.MIME_TYPE} =? or ${MediaStore.Images.Media.MIME_TYPE} =?"

        val selectionArgs = arrayOf("image/jpeg", "image/png")

        val queryUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val cursor =
            this.contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder)

        if (cursor != null && cursor.moveToFirst()) {
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenColumnIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (!cursor.isAfterLast) {
                val id = cursor.getLong(idColumnIndex)
                val name = cursor.getString(nameColumnIndex)
                val dateTaken = cursor.getLong(dateTakenColumnIndex)
                val size = cursor.getLong(sizeColumnIndex)

                val contentUri = ContentUris.withAppendedId(queryUri, id)
                listOfImages.add(Photo(name = contentUri))
                if (listOfImages.size <20)
                    cursor.moveToNext()
                else
                    return listOfImages
            }
            cursor.close()
        }
        return listOfImages
    }


    @SuppressLint("SetTextI18n")
    fun updateSelectedImagesCount(count: Int) {
        if (count <= 0)  {
            binding.cardViewSelected.isVisible = false
        } else {
            binding.cardViewSelected.isVisible = true
            binding.tvSelectedCount.text = "Выбрано ${count} фотографии"
        }
    }

    companion object {
        const val REQUEST_CODE = 1
        const val SEND_IMAGES_TO_RESULT_ACTIVITY_KEY = "SEND_IMAGES_TO_RESULT_ACTIVITY_KEY"
    }

}