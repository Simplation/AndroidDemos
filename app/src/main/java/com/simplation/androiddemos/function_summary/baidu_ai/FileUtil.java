/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.simplation.androiddemos.function_summary.baidu_ai;

import android.content.Context;

import java.io.File;

public class FileUtil {

    public static File getSaveFile(Context context) {
        return new File(context.getFilesDir(), "pic.jpg");
    }
}
