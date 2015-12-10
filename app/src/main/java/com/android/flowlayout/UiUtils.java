package com.android.flowlayout;

import android.content.Context;
import android.util.TypedValue;

/**
 * UI相关的辅助类
 * Created by wu on 2015/11/6.
 */
public class UiUtils {
     /*
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context,float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal,context.getResources().getDisplayMetrics());
    }
}
