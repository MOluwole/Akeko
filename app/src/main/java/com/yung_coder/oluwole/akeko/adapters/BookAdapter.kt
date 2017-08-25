package com.yung_coder.oluwole.akeko.adapters

import android.content.Context
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.yung_coder.oluwole.akeko.R
import com.yung_coder.oluwole.akeko.models.Models
import java.io.File

/**
 * Created by yung on 8/24/17.
 */
class BookAdapter constructor(mList: List<Models.book>) : RecyclerView.Adapter<BookAdapter.BookViewAdapter>() {

    var mList: List<Models.book>? = null
    var generator = ColorGenerator.MATERIAL
    var context: Context? = null

    init {
        this.mList = mList
    }

    override fun getItemCount(): Int {
        return mList?.count() ?: 0
    }

    override fun onBindViewHolder(holder: BookViewAdapter?, position: Int) {
        val book_details = mList?.get(position)
        holder?.book_name?.text = book_details?.title
        val letter = book_details?.title?.get(0).toString()
        val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
        holder?.book_thumbnail?.setImageDrawable(drawable)
        holder?.book_copyright?.text = "Source: " + book_details?.copyright


        val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Akekoo/Books")
        if (!storageDir.exists()) storageDir.mkdirs()
        val filename = book_details?.title + ".pdf"
        val file = File(storageDir.toString() + filename)
        if (!file.exists()) {
            holder?.book_download?.visibility = View.VISIBLE
        } else {
            holder?.book_download?.visibility = View.GONE
        }
//        ("Check if File has been downloaded here. If YES, hide download image else show Download Image")
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BookViewAdapter {
        val rootView = LayoutInflater.from(parent?.context).inflate(R.layout.item_material_list, parent, false)
        val view_adapter = BookViewAdapter(rootView)
        context = rootView.context
        return view_adapter
    }

    class BookViewAdapter(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        var book_name = layoutView.findViewById<TextView>(R.id.item_name)!!
        var book_thumbnail = layoutView.findViewById<ImageView>(R.id.item_image)!!
        var book_copyright = layoutView.findViewById<TextView>(R.id.item_source)
        var book_download = layoutView.findViewById<ImageView>(R.id.item_download)
//        var click_item = layoutView.findViewById<RelativeLayout>(R.id.lang_list)!!
    }
}