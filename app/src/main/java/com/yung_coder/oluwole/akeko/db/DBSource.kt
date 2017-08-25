package com.yung_coder.oluwole.akeko.db

import com.yung_coder.oluwole.akeko.models.Models
import java.util.*

/**
 * Created by yung on 8/23/17.
 */
interface DBSource {
    fun loadBooks(lang_id: Int): List<Models.book>
    fun loadLang(): List<Models.lang>
    fun loadVideos(lang_id: Int): List<Models.video>

    fun saveBooks(books: ArrayList<Models.book>)
    fun saveLang(lang: ArrayList<Models.lang>)
    fun saveVideos(videos: ArrayList<Models.video>)

    fun loadSingleBook(_id: Int): List<Models.book>
    fun loadSingleVideo(_id: Int): List<Models.video>

    fun deleteLang()
    fun deleteBook(_id: Int): Boolean
    fun deleteVideo(_id: Int): Boolean

    fun deleteTimer()
    fun saveTimer(timer: String)
}