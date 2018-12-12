package com.library.test;

import android.app.Activity;
import android.content.Intent;

public class StartUtils {


    public static void startActivity(Activity activity){
        activity.startActivity(new Intent(activity,TestActivity.class));
    }
}
