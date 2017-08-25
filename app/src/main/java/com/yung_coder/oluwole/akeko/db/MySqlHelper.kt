package com.yung_coder.oluwole.akeko.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by yung on 8/23/17.
 */
class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Akekoo") {
    companion object {
        private var instance: MySqlHelper? = null

//        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable(AkekooTable.LANG_TABLE, true, AkekooTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT, AkekooTable.NAME to TEXT)
        p0?.createTable(AkekooTable.BOOK_TABLE, true, AkekooTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT, AkekooTable.LANG_ID to INTEGER, AkekooTable.TITLE to TEXT, AkekooTable.COPYRIGHT to TEXT)
        p0?.createTable(AkekooTable.VID_TABLE, true, AkekooTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT, AkekooTable.LANG_ID to INTEGER, AkekooTable.TITLE to TEXT, AkekooTable.COPYRIGHT to TEXT)
        p0?.createTable(AkekooTable.TIMER_TABLE, true, AkekooTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT, AkekooTable.TIMER_COL to TEXT)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

//Access property for Context
//val Context.database: MySqlHelper
//    get() = MySqlHelper.getInstance(applicationContext)