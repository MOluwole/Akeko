package com.yung_coder.oluwole.akeko

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
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
import org.json.JSONObject

class Menu : AppCompatActivity() {

    var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        Reload()
        swipeRefresh?.setOnRefreshListener {
            loadFromNetwork()
            Reload()
        }
    }

    fun Reload(){
        swipeRefresh?.isRefreshing = true
        val dbManager: DbManager = DbManager(this)

        val mList = dbManager.loadLang()
        Log.e("Data", mList.count().toString())
        val recycler_view = findViewById<RecyclerView>(R.id.list_view)
//        recycler_view?.adapter = null
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
        var data: ArrayList<Models.lang> = ArrayList()

        val url = MainActivity.BASE_URL + "/akekoo/language"
        val jsonObjReq = JsonObjectRequest(Request.Method.GET,
                url, null,
                Response.Listener<JSONObject> { response ->
                    var success: Int = response.getInt("success")
                    if (success == 1) {
                        var langs = response.getJSONArray("languages")
                        var count: Int = langs.length() - 1
                        while (count >= 0) {
                            var jObj = langs.getJSONObject(count)
                            var name = jObj.getString("name")
                            var _id = jObj.getInt("id")
                            var model = Models.lang(_id, name)
                            data.add(model)
                            count--
                        }
                        var dbManager: DbManager = DbManager(this)
                        var mList = dbManager.loadLang()
                        dbManager.deleteLang()
                        dbManager.saveLang(data)
                    } else {
                        var message = response.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Error Message", "Error: " + error.message)
                })
        queue.add(jsonObjReq)
    }
}
