package com.kanxue.classsloadertest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {

    private static final String TAG = "KANXUE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testClassLoader();
        Context context = this.getApplicationContext(); // Return the context of the single
//        testClassLoader1(context,"/sdcard/3.dex");
        testActivity(this,"/sdcard/5.dex");
//        testActivity(this,"/sdcard/4.dex");
    }

    private void testActivity(Context context,String dexdfilepath) {

        File optifle = context.getDir("opt_dex",0); // 优化的路径
        File libfile = context.getDir("lib_path",0); //lib 所在的路径
        System.out.println("optfile:"+optifle);
        System.out.println("libfile:"+libfile);
        ClassLoader pathclassloader = MainActivity.class.getClassLoader();
        ClassLoader bootClassloader = MainActivity.class.getClassLoader().getParent();
        System.out.println("pathclassloader:"+pathclassloader);
        DexClassLoader dexClassLoader = new DexClassLoader(dexdfilepath,optifle.getAbsolutePath(),libfile.getAbsolutePath(),bootClassloader);

        try {
            Field parentField = ClassLoader.class.getDeclaredField("parent");
            parentField.setAccessible(true);
            parentField.set(pathclassloader,dexClassLoader);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }


        Class claza = null;
        try{
            claza = dexClassLoader.loadClass("com.kanxue.classloadertest2.TestActivity");

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        // 启动activity
        context.startActivity(new Intent(context,claza));

    }


}
