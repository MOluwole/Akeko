package com.yung_coder.oluwole.akeko

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yung_coder.oluwole.akeko.adapters.LangAdapter
import com.yung_coder.oluwole.akeko.db.DbManager
import com.yung_coder.oluwole.akeko.models.Models
import org.json.JSONObject

class Menu : AppCompatActivity() {

    var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)


        swipeRefresh?.isRefreshing = true
        Reload()
        swipeRefresh?.setOnRefreshListener {
            swipeRefresh?.isRefreshing = true
            loadFromNetwork()
            Reload()
        }
    }

    fun Reload(){
        val dbManager: DbManager = DbManager(this)

        val mList = dbManager.loadLang()
        val recycler_view = findViewById<RecyclerView>(R.id.list_view)
        recycler_view?.adapter = null
        val mLayoutManager = LinearLayoutManager(this)
        recycler_view?.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(recycler_view.context, mLayoutManager.orientation)
        recycler_view.addItemDecoration(dividerItemDecoration)

        val mAdapter = LangAdapter(mList)
        recycler_view?.adapter = mAdapter

        swipeRefresh?.isRefreshing = false
    }

    fun loadFromNetwork() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val data: ArrayList<Models.lang> = ArrayList()

        val url = MainActivity.BASE_URL + "/akekoo/language"
        val jsonObjReq = JsonObjectRequest(Request.Method.GET,
                url, null,
                Response.Listener<JSONObject> { response ->
                    val success: Int = response.getInt("success")
                    if (success == 1) {
                        val langs = response.getJSONArray("languages")
                        var count: Int = langs.length() - 1
                        while (count >= 0) {
                            val jObj = langs.getJSONObject(count)
                            val name = jObj.getString("name")
                            val _id = jObj.getInt("id")
                            val model = Models.lang(_id, name)
                            data.add(model)
                            count--
                        }
                        val dbManager: DbManager = DbManager(this)
                        dbManager.deleteLang()
                        dbManager.saveLang(data)
                    } else {
                        val message = response.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Error Message", "Error: " + error.message)
                })
        queue.add(jsonObjReq)
    }
}
