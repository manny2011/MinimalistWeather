package com.manny.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    companion object {
        const val name="MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val childFrag = ChildFrag()
        supportFragmentManager.beginTransaction()
                .add(R.id.fl,childFrag)
                .commitAllowingStateLoss()
        Log.e(name,"onCreate")
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
