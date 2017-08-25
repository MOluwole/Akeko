package com.yung_coder.oluwole.akeko.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yung on 8/23/17.
 */
class Models {
    @SerializedName("lang")
    @Expose
    var language: List<lang>? = null

    @SerializedName("books")
    @Expose
    var books: List<book>? = null

    @SerializedName("videos")
    @Expose
    var videos: List<video>? = null

    class lang{
        @SerializedName("id")
        @Expose
        var _id: Int = 0

        @SerializedName("name")
        @Expose
        var name: String = ""

        constructor(_id: Int, name: String){
            this._id = _id
            this.name = name
        }
    }

    class book{
        @SerializedName("id")
        @Expose
        var _id: Int = 0

        @SerializedName("lang_id")
        @Expose
        var lang_id: Int = 0

        @SerializedName("name")
        @Expose
        var title: String = ""

        @SerializedName("copyright")
        @Expose
        var copyright: String = ""

        constructor(_id: Int, lang_id: Int, title: String, copyright: String){
            this._id = _id
            this.title = title
            this.copyright = copyright
            this.lang_id = lang_id
        }
    }

    class video{
        @SerializedName("id")
        @Expose
        var _id: Int = 0

        @SerializedName("lang_id")
        @Expose
        var lang_id: Int = 0

        @SerializedName("name")
        @Expose
        var title: String = ""

        @SerializedName("copyright")
        @Expose
        var copyright: String = ""

        constructor(_id: Int, lang_id: Int, title: String, copyright: String){
            this._id = _id
            this.title = title
            this.copyright = copyright
            this.lang_id = lang_id
        }
    }
}