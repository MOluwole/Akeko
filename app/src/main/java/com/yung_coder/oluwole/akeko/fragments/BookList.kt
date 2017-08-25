package com.yung_coder.oluwole.akeko.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yung_coder.oluwole.akeko.MainActivity
import com.yung_coder.oluwole.akeko.R
import com.yung_coder.oluwole.akeko.adapters.BookAdapter
import com.yung_coder.oluwole.akeko.db.DbManager
import com.yung_coder.oluwole.akeko.models.Models
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class BookList : Fragment() {

    companion object {
        var lang_id: Int = 0
        var queue: RequestQueue? = null
        var data: ArrayList<Models.book> = ArrayList()
        fun newInstance(extra: Int): BookList {
            val fragment = BookList()
            lang_id = extra
            return fragment
        }
    }

    var recycler_view: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    fun Reload(context: Context) {
        val dbManager: DbManager = DbManager(context)

        val mList = dbManager.loadBooks(lang_id)


        val mAdapter = BookAdapter(mList)
        recycler_view?.adapter = mAdapter

        swipeRefresh?.isRefreshing = false
    }

    fun loadFromNetwork(context: Context) {
        val url = MainActivity.BASE_URL + "/akekoo/book?lang_id=" + lang_id
        val jsonObjReq = JsonObjectRequest(Request.Method.GET,
                url, null,
                Response.Listener<JSONObject> { response ->
                    val success: Int = response.getInt("success")
                    if (success == 1) {
                        val books = response.getJSONArray("data")
                        var count: Int = books.length() - 1
                        while (count >= 0) {
                            val jObj = books.getJSONObject(count)
                            var title = jObj.getString("title")
                            var _id = jObj.getInt("id")
                            var copyright = jObj.getString("copyright")
                            var model = Models.book(_id, lang_id, title, copyright)
                            data.add(model)
                            count--
                        }
                        var dbManager: DbManager = DbManager(context)
                        var res = dbManager.deleteBook(lang_id)
                        if (res) dbManager.saveBooks(data)
                        else Toast.makeText(context, "Internal Error", Toast.LENGTH_SHORT).show()
                        Reload(context)
                    } else {
                        var message = response.getString("message")
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Error Message", "Error: " + error.message)
                })
        queue?.add(jsonObjReq)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_book, container, false)
        queue = Volley.newRequestQueue(rootView.context)
        recycler_view = rootView?.findViewById<RecyclerView>(R.id.book_list_view)
        recycler_view?.adapter = null
        val mLayoutManager = LinearLayoutManager(rootView.context)
        recycler_view?.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(rootView.context, mLayoutManager.orientation)
        recycler_view?.addItemDecoration(dividerItemDecoration)

        swipeRefresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_book_refresh)
        swipeRefresh?.isRefreshing = true

        val list = DbManager(rootView.context).loadBooks(lang_id)
        if (list.count() <= 0) {
            loadFromNetwork(rootView.context)
        } else {
            Reload(rootView.context)
        }
        swipeRefresh?.setOnRefreshListener {
            swipeRefresh?.isRefreshing = true
            loadFromNetwork(rootView.context)
            Reload(rootView.context)
        }
        return rootView
    }

}// Required empty public constructor
