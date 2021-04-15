package com.kanxue.classsloadertest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "KANXUE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testClassLoader();
        Context context = this.getApplicationContext(); // Return the context of the single
        testClassLoader1(context,"/sdcard/3.dex");
    }

    private void testClassLoader1(Context context,String dexdfilepath) {

        File optifle = context.getDir("opt_dex",0); // 优化的路径
        File libfile = context.getDir("lib_path",0); //lib 所在的路径
        DexClassLoader dexClassLoader = new DexClassLoader(dexdfilepath,optifle.getAbsolutePath(),libfile.getAbsolutePath(), MainActivity2.class.getClassLoader());

        Class claza = null;
        try{
            claza = dexClassLoader.loadClass("com.kanxue.classsloadertest.TestClass");

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        if (claza!=null){

            try {
                Method tetfuc = claza.getDeclaredMethod("testFunc");
                Object obj  = claza.newInstance();
                tetfuc.invoke(obj);
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }catch (InstantiationException e){
                e.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (InvocationTargetException e){
                e.printStackTrace();
            }
        }

    }


    public void testClassLoader() {
//        获取当前启动类的类加载器
        ClassLoader thisClassLoader = MainActivity2.class.getClassLoader();
        Log.i(TAG,"thisclassloader:"+thisClassLoader);
        //2021-04-14 17:33:53.957 21484-21484/com.kanxue.classsloadertest I/KANXUE: thisclassloader:dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.kanxue.classsloadertest-1/base.apk"],nativeLibraryDirectories=[/data/app/com.kanxue.classsloadertest-1/lib/arm64, /system/lib64, /vendor/lib64, /system/vendor/lib64, /product/lib64]]]
        ClassLoader tempClassloader = null;
        // 返回当前classloader的父加载器
        ClassLoader paraentclassloader = thisClassLoader.getParent();
        // 循环查找父加载器
        while (paraentclassloader != null){
            Log.i(TAG,"this:"+thisClassLoader+"==="+paraentclassloader);
            tempClassloader=paraentclassloader.getParent();
            thisClassLoader=paraentclassloader;
            paraentclassloader=tempClassloader;
        }
        Log.i(TAG,"根节点--classloader: "+thisClassLoader);
    }
}
