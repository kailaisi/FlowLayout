package com.android.flowlayout;

import android.content.Context;
import android.database.Cursor;
import android.provider.UserDictionary;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 文字瀑布流，瀑布流中每个子控件是textview，如果不是，请重新写layout方法，将返回的子控件定义为你的控件类型，
 * Created by wu on 2015/11/12.
 */
public class FlowLayout extends ViewGroup {
    private List<Line> mLines = new ArrayList<>();
    private Line currentLine;//当前行
    private int usedWidth = 0;//当前行已经使用的宽度
    private int horizontalSpacing;//水平的间隔
    private int verticalSpacing;//垂直的间隔
    private int width;//控件的宽度
    private int height;//控件的高度
    private Context mContext;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        horizontalSpacing=UiUtils.dp2px(context,13);
        verticalSpacing=UiUtils.dp2px(context,13);
    }

    //测量当前控件
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取当前容器的宽高模式和大小
        mLines.clear();
        currentLine = null;
        usedWidth = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
        height = MeasureSpec.getSize(heightMeasureSpec)-getPaddingTop()-getPaddingBottom();
        int childWidthMode;
        int childHeightMode;
        //为了测量每个子控件，需要指定每个子控件的测量规则
        childWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
        childHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, childWidthMode);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, childHeightMode);
        currentLine = new Line();//创建了新的一行（第一行）
        for (int i = 0; i < getChildCount(); i++) {
            //测量子控件
            View child = getChildAt(i);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int measuredWidth = child.getMeasuredWidth();//获得子控件的宽度
            if(usedWidth+measuredWidth+horizontalSpacing<width ||currentLine.getChildCount()==0){
                //当前行没有数据或者现在的宽度+下一个宽度<行宽。不需要换行，直接添加到current中。
                currentLine.addChild(child);
                usedWidth+=measuredWidth;
                usedWidth+=horizontalSpacing;
            }else{
                newLine();
                currentLine.addChild(child);
                usedWidth+=measuredWidth;
                usedWidth+=horizontalSpacing;
            }
        }
        if (!mLines.contains(currentLine)) {//添加最后一行
            mLines.add(currentLine);
            Log.d("FlowLayout", "currentLine.getChildCount():" + currentLine.getChildCount());
        }
        int totalHeight = 0;
        for (Line line : mLines) {
            totalHeight += line.getHeight();
        }
        totalHeight += ((mLines.size() - 1) * verticalSpacing)+getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(width+getPaddingLeft()+getPaddingRight(), resolveSize(totalHeight, heightMeasureSpec));
    }

    //分配子控件的位置,如果剩余的距离不够使用，则需要换行
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l+=getPaddingLeft();
        t+=getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.layout(l, t);
            t += line.getHeight() + verticalSpacing;//每一行左上角的t值都会改变
        }
    }

    /**
     * 每一个行的类
     */
    private class Line {
        int height = 0;
        List<View> children = new ArrayList<>();
        int total = 0;

        /**
         * 添加一个子控件
         *
         * @param child
         */
        public void addChild(View child) {
            children.add(child);
            if (child.getMeasuredHeight() > height) {
                height = child.getMeasuredHeight();
            }
            total += child.getMeasuredWidth();
        }

        /**
         * 获取子控件的数量
         *
         * @return
         */
        public int getChildCount() {
            return children.size();
        }

        public int getHeight() {
            return height;
        }

        /**
         * 指定行的左上角位置，其子类的位置由该函数确定
         *
         * @param l  左侧位置
         * @param t  顶部位置
         */
        public void layout(int l, int t) {
            total += horizontalSpacing * (children.size() - 1);//现有子控件所占有的宽度
            int surplusChild = 0;
            int surplus = width - total;//右侧剩余的宽度
            surplusChild = surplus / children.size();//右侧剩余宽度平分给各个控件
            for (int i = 0; i < children.size(); i++) {
                //将每一个子TextView取出来
                TextView view = (TextView) children.get(i);
                //设置每个子TextView的布局，宽度在原有布局的基础上增加了surplusChild
                view.layout(l, t, l + view.getMeasuredWidth()+surplusChild, t + view.getMeasuredHeight());
                //为子View的字体设置居中，此步骤不能在给layout添加view的时候，给view设置gravity属性，只能在这里设置
                view.setGravity(Gravity.CENTER);
                String text=view.getText().toString();
                if(text!=null){
                    //如果此时textview的文字已经绘制完成，因为我们重新layout，会导致文字不居中，重新获取文字，并设置，
                    view.setText(text);
                }
                //更新下一个子View的左侧的位置
                l += view.getMeasuredWidth()+surplusChild;
                l += verticalSpacing;
            }
        }
    }

    /**
     * 创建新的行
     */
    public void newLine() {
        mLines.add(currentLine);
        currentLine = new Line();
        usedWidth = 0;
    }
}
