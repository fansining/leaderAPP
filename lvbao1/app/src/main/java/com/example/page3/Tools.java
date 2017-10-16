package com.example.page3;

import android.os.Environment;

/**
 * Created by 骛 on 2016/10/17.
 */
public class Tools {
    public static boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
}
