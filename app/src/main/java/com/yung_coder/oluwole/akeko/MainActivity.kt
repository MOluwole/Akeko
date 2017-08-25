package com.yung_coder.oluwole.akeko

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yung_coder.oluwole.akeko.db.DbManager
import com.yung_coder.oluwole.akeko.models.Models
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    companion object {
        val BASE_URL: String = "http://oluwole.pythonanywhere.com"//getString(R.string.BASE_URL)
    }

    var data:ArrayList<Models.lang> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val queue: RequestQueue = Volley.newRequestQueue(this)

        var list: List<Models.lang> = DbManager(this.applicationContext).loadLang()
        if(list.count() <= 0){
            val url = BASE_URL + "/akekoo/language"
            val jsonObjReq = JsonObjectRequest(Request.Method.GET,
                    url, null,
                    Response.Listener<JSONObject> { response ->
                        var success: Int = response.getInt("success")
                        if(success == 1){
                            var langs = response.getJSONArray("languages")
                            var count: Int = langs.length() - 1
                            while (count >= 0){
                                val jObj = langs.getJSONObject(count)
                                var name = jObj.getString("name")
                                var _id = jObj.getInt("id")
                                var model = Models.lang(_id, name)
                                data.add(model)
                                count--
                            }
                            var dbManager: DbManager = DbManager(this)
                            dbManager.saveLang(data)
                            var intent = Intent(this, Menu::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            var message = response.getString("message")
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    },
                    Response.ErrorListener { error ->
                Log.e("Error Message", "Error: " + error.message)
            })
            queue.add(jsonObjReq)
        }
        else{
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
}
