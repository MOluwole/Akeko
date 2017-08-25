package com.yung_coder.oluwole.akeko


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A simple [Fragment] subclass.
 */
class VideoList : Fragment() {

    companion object {
        var lang_id: Int = 0
        fun newInstance(extra: Int): VideoList {
            val fragment = VideoList()
            lang_id = extra
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_video, container, false)
    }

}// Required empty public constructor
