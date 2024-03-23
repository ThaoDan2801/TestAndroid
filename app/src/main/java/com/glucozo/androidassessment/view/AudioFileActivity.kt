package com.glucozo.androidassessment.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.glucozo.androidassessment.databinding.ActivityAudioFileBinding
import com.glucozo.androidassessment.model.AudioFile

class AudioFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioFileBinding
    private var listAudioFile: ArrayList<AudioFile> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        loadAudioFile()
    }

    private fun loadAudioFile() {
        val audioFileUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val audioColumns = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_ADDED,
        )
        val cursor = contentResolver.query(
            audioFileUrl,
            audioColumns,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                val date =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                val size =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                val audioFile = AudioFile(id,name, date, size)
                listAudioFile.add(audioFile)
            }
        }
    }

    private fun setupView() {
        binding.rcFile.layoutManager = LinearLayoutManager(this)
        binding.rcFile.adapter = AudioFileAdapter(this,listAudioFile)
    }
}