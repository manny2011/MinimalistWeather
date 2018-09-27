package com.baronzhang.android.weather;

import android.arch.lifecycle.ViewModelProviders;

public class Singleton {
    //    public static Singleton instance=new Singleton();
    public static Singleton instance=null;
    static {
        System.err.println("static below: a="+Singleton.a+" b="+Singleton.b);
    }

    static {
        a=4;b=4;
    }

    public static int a=2;
    public static int b=2;
    public static Singleton getInstance(){
        return instance;
    }

    public Singleton() {
        a=3;b=3;
    }

    public static void main(String[] args){
        Singleton instance = Singleton.getInstance();
        System.err.println(instance);
        System.err.println(Singleton.a);
        System.err.println(Singleton.b);
    }
}
