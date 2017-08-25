package com.yung_coder.oluwole.akeko.db

import android.content.Context
import android.util.Log
import com.yung_coder.oluwole.akeko.models.Models
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by yung on 8/23/17.
 */

class DbManager(context: Context) : DBSource {
    var dbHelper: MySqlHelper = MySqlHelper.getInstance(context)

    override fun loadBooks(lang_id: Int): List<Models.book> {
      return dbHelper.use {
           val myParser = classParser<Models.book>()
           select(AkekooTable.BOOK_TABLE)
                   .whereSimple("${AkekooTable.LANG_ID} = ?", lang_id.toString())
                   .parseList(myParser).toList()
       }
    }

    override fun loadLang(): List<Models.lang> = dbHelper.use {
        val myParser = classParser<Models.lang>()
        select(AkekooTable.LANG_TABLE).parseList(myParser).toList()
    }

    override fun loadVideos(lang_id: Int): List<Models.video> {
        return dbHelper.use {
            val myParser = classParser<Models.video>()
            select(AkekooTable.VID_TABLE).whereSimple("${AkekooTable.LANG_ID} = ?", lang_id.toString())
                    .parseList(myParser).toList()
        }
    }

    override fun loadSingleBook(_id: Int): List<Models.book> {
        return dbHelper.use{
            val myParser = classParser<Models.book>()
            select(AkekooTable.BOOK_TABLE).whereSimple("${AkekooTable.ID} = ?", _id.toString())
                    .parseList(myParser).toList()
        }
    }

    override fun loadSingleVideo(_id: Int): List<Models.video> {
        return dbHelper.use {
            val myParser = classParser<Models.video>()
            select(AkekooTable.VID_TABLE).whereSimple("${AkekooTable.ID} = ?", _id.toString())
                    .parseList(myParser).toList()
        }
    }

    override fun saveBooks(books: ArrayList<Models.book>) {
        for (i in books){
            dbHelper.use {
                insert(AkekooTable.BOOK_TABLE, AkekooTable.ID to i._id, AkekooTable.COPYRIGHT to i.copyright,
                        AkekooTable.TITLE to i.title, AkekooTable.LANG_ID to i.lang_id)
            }
        }
    }

    override fun saveLang(lang: ArrayList<Models.lang>) {
        for (i in lang){
            dbHelper.use {
                insert(AkekooTable.LANG_TABLE, AkekooTable.ID to i._id, AkekooTable.NAME to i.name)
            }
        }
    }

    override fun saveVideos(videos: ArrayList<Models.video>) {
        for (i in videos){
            dbHelper.use {
                insert(AkekooTable.VID_TABLE, AkekooTable.ID to i._id, AkekooTable.LANG_ID to i.lang_id,
                        AkekooTable.TITLE to i.title, AkekooTable.COPYRIGHT to i.copyright)
            }
        }
    }

    override fun deleteLang() {
        dbHelper.use {
            delete(AkekooTable.LANG_TABLE)
        }
    }

    override fun deleteBook(_id: Int): Boolean {
        var isDeleted: Boolean = false
        dbHelper.use {
            try {
                beginTransaction()
                val result = delete(AkekooTable.BOOK_TABLE, "${AkekooTable.LANG_ID} = {lang_id}", "lang_id" to _id) > 0
                if (result) {
                    setTransactionSuccessful()
                    isDeleted = true
                } else {
                    isDeleted = false
                }
            } catch (e: Throwable) {
                Log.e("Error: ", e.message)
                isDeleted = false
            } finally {
                endTransaction()
            }
        }
        return isDeleted
    }

    override fun deleteTimer() {
        dbHelper.use {
            delete(AkekooTable.TIMER_TABLE)
        }
    }

    override fun deleteVideo(_id: Int): Boolean {
        var isDeleted: Boolean = false
        dbHelper.use {
            try {
                beginTransaction()
                val result = delete(AkekooTable.VID_TABLE, "${AkekooTable.LANG_ID} = {lang_id}", "lang_id" to _id) > 0
                if (result) {
                    setTransactionSuccessful()
                    isDeleted = true
                } else {
                    isDeleted = false
                }
            } catch (e: Throwable) {
                Log.e("Error: ", e.message)
                isDeleted = false
            } finally {
                endTransaction()
            }
        }
        return isDeleted
    }

    override fun saveTimer(timer: String) {
        dbHelper.use {
            insert(AkekooTable.TIMER_TABLE, AkekooTable.TIMER_COL to timer)
        }
    }
}