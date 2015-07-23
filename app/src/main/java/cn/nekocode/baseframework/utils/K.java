package cn.nekocode.baseframework.utils;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import cn.nekocode.baseframework.AppContext;

/**
 * Created by Nekocode on 2014/11/24 0024.
 */
public class K {
    private static final String prefix = "memcache_";
    private static HashMap<String, Object> caches = new HashMap<>();
    private static Kryo kryo = new Kryo();

    public static void save(String fileName, Object obj) {
        try {
            FileOutputStream fo = AppContext.get().openFileOutput(fileName, Context.MODE_PRIVATE);
            Output output = new Output(fo);

            kryo.writeObject(output, obj);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <E>E load(String fileName, Class<E> c) {
        E rlt = null;
        try {
            FileInputStream fi = AppContext.get().openFileInput(fileName);
            Input input = new Input(fi);
            rlt = kryo.readObject(input, c);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rlt;
    }

    public static void delete(Context context, String fileName) {
        context.deleteFile(fileName);
    }

    public static <T>T getMemCache(String key, Class<T> classType) {
        key = prefix + key;

        T cache = (T) caches.get(key);
        if(cache == null)
            cache = load(key, classType);
        return cache;
    }

    public static void putMemCache(String key, Object object) {
        key = prefix + key;

        caches.put(key, object);
        if(object != null)
            save(key, object);
    }
}
