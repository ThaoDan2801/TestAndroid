package com.glucozo.androidassessment.view

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.glucozo.androidassessment.R
import com.glucozo.androidassessment.model.AudioFile
import com.glucozo.androidassessment.util.ConvertBytes
import com.glucozo.androidassessment.util.FormatDate

class AudioFileAdapter(
    val context: Context,
    private val listAudioFile: ArrayList<AudioFile>
) : RecyclerView.Adapter<AudioFileAdapter.ViewHolder>() {
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var name: AppCompatTextView = view.findViewById(R.id.tv_name)
        var date: AppCompatTextView = view.findViewById(R.id.tv_date)
        var size: AppCompatTextView = view.findViewById(R.id.tv_size)
        private var layout: CardView = view.findViewById(R.id.item_layout)

        init {
            layout.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(view: View) {
            val position = listAudioFile[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.show_popup)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editText -> {
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.dialog_edit_item, null)
                        val editName = view.findViewById<AppCompatEditText>(R.id.edt_item)
                        AlertDialog.Builder(context)
                            .setView(view)
                            .setPositiveButton(R.string.Audio_File_09) { dialog, _ ->
                                position.name = editName.text.toString()

                                //update item file audio
                                val values = ContentValues().apply {
                                    put(MediaStore.Audio.Media.DISPLAY_NAME, position.name)
                                }
                                val uri =
                                    Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + "/" + position.id)
                                context.contentResolver.update(uri, values, null, null)

                                notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            .setNegativeButton(R.string.Audio_File_10) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }

                    R.id.delete -> {
                        AlertDialog.Builder(context)
                            .setTitle(R.string.Audio_File_07)
                            .setMessage(R.string.Audio_File_08)
                            .setPositiveButton(R.string.Audio_File_09) { dialog, _ ->

                                //delete item file audio
                                listAudioFile.removeAt(adapterPosition)
                                val uri =
                                    Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + "/" + position.id)
                                context.contentResolver.delete(uri, null, null)
                                notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            .setNegativeButton(R.string.Audio_File_10) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_audio_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listAudioFile.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listAudioFile[position].let { audioFile ->
            holder.name.text = audioFile.name
            holder.date.text = FormatDate.convertTimestampToString(audioFile.date)
            val size = ConvertBytes.convertBytesToMegabytes(audioFile.size)
            holder.size.text = String.format("%.1f MB", size)
        }
    }
}