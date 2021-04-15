package com.kanxue.classsloadertest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity3 extends Activity {

    private static final String TAG = "KANXUE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testClassLoader();
        Context context = this.getApplicationContext(); // Return the context of the single
//        testClassLoader1(context,"/sdcard/3.dex");
        testActivity(this,"/sdcard/4.dex");
    }

    private void testActivity(Context context,String dexdfilepath) {

        File optifle = context.getDir("opt_dex",0); // 优化的路径
        File libfile = context.getDir("lib_path",0); //lib 所在的路径
        System.out.println("optfile:"+optifle);
        System.out.println("libfile:"+libfile);
        ClassLoader parentClassloader = MainActivity3.class.getClassLoader();
        System.out.println("parentClassloader:"+parentClassloader);
        ClassLoader tmpclassloader = context.getClassLoader();
        System.out.println("tmpclassloader:"+tmpclassloader);
        DexClassLoader dexClassLoader = new DexClassLoader(dexdfilepath,optifle.getAbsolutePath(),libfile.getAbsolutePath(), MainActivity3.class.getClassLoader());
        replaceClassLoader(dexClassLoader);
        Class claza = null;
        try{
            claza = dexClassLoader.loadClass("com.kanxue.classloadertest2.TestActivity");

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        // 启动activity
        context.startActivity(new Intent(context,claza));

    }

    private void replaceClassLoader(DexClassLoader dexClassLoader) {
        // 通过反射调用ActivityThread
        try {
            Class ActivityThread = dexClassLoader.loadClass("android.app.ActivityThread");
            Method currentActivitymethod = ActivityThread.getDeclaredMethod(" currentActivityThread");
            Object ActivityThreadobj = currentActivitymethod.invoke(null);
            // 获取arraymap
            Field mpackagesField = ActivityThread.getDeclaredField("mPackages");
            mpackagesField.setAccessible(true);
            ArrayMap mpackagesobj = (ArrayMap)mpackagesField.get(ActivityThreadobj);
            WeakReference wr = (WeakReference)mpackagesobj.get(this.getPackageName());
            Object loadedapkobj = wr.get();

            Class loaderapkclazz = dexClassLoader.loadClass("android.app.LoadedApk");
            Field mclassloader = loaderapkclazz.getDeclaredField("mClassLoader");
            mclassloader.setAccessible(true);
            mclassloader.set(loadedapkobj,dexClassLoader);

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }

    }


    public void testClassLoader() {
//        获取当前启动类的类加载器
        ClassLoader thisClassLoader = MainActivity3.class.getClassLoader();
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
