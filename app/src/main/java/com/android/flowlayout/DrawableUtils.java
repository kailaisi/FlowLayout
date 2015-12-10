package com.android.flowlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by wu on 2015/11/12.
 */
public class DrawableUtils {
    /**
     * 生成圆角图片
     * @param context
     * @param color
     * @return
     */
    public static Drawable createShape(Context context, int color) {
        GradientDrawable drawable=new GradientDrawable();
        drawable.setCornerRadius(UiUtils.dp2px(context,5));
        drawable.setColor(color);
        return  drawable;
    }

    /**
     * 生成selector，动态设置
     * @param pressedDrawable   按下时的drawable
     * @param normalDrawable    正常状态是的drawable
     * @return
     */
    public static Drawable creatStateListDrawable(Drawable pressedDrawable,Drawable normalDrawable){
        StateListDrawable drawable=new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},pressedDrawable);
        drawable.addState(new int[]{},normalDrawable);
        return drawable;
    }
}
