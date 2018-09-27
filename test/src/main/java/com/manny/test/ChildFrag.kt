package com.manny.test

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ChildFrag : Fragment() {

    companion object {
        const val name="ChildFrag"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.e(name,"onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(name,"onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e(name,"onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(name,"onViewCreated")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(name,"onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.e(name,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(name,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(name,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(name,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(name,"onDestroy")
    }

    fun getName():String{
        return object : Any() {}.javaClass
                .enclosingMethod
                .name
    }
}