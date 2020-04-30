package swu.xl.pagecontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class PagerController extends LinearLayout {
    //页面数量
    private int numberOfPage;

    //页面之间的距离
    private int pagePadding;

    //记录当前记录的是第几页
    private int currentPage;

    //页面的样式
    private int pageResource;

    //页面切换的监听者
    private PageChangeListener pageChangeListener;

    //页面切换的动画
    private PageChangeAnimation pageChangeAnimation;

    /**
     * 构造方法 Java代码创建该类的时候会调用该方法
     * @param context
     */
    public PagerController(Context context) {
        super(context);

        //初始化
        init();
    }

    /**
     * 构造方法 Xml代码创建该类的时候会调用该方法
     * @param context
     * @param attrs
     */
    public PagerController(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化
        init();

        //取出自定义的属性
        if (attrs != null){
            //1.获得自定义属性的集合
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerController);

            //2.取出资源并设置
            //注意: setNumberOfPage 必须最后调用
            //原因: 内部需要pagePadding，pageResource
            pagePadding = typedArray.getInteger(
                    R.styleable.PagerController_pagePadding,
                    20);
            pageResource = typedArray.getResourceId(
                    R.styleable.PagerController_pageResource,
                    0);
            currentPage = typedArray.getInteger(
                    R.styleable.PagerController_currentPage,
                    0);
            this.setNumberOfPage(
                    typedArray.getInteger(
                            R.styleable.PagerController_numberOfPage,
                            4)
            );

            //释放资源
            typedArray.recycle();
        }
    }

    /**
     * 初始化方法
     */
    private void init() {
        //设置子View水平排列
        this.setOrientation(LinearLayout.HORIZONTAL);

        //设置子View水平垂直居中
        this.setGravity(Gravity.CENTER);
    }

    /**
     * 触摸事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果是按下事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //获取触摸点的横坐标
            float x = event.getX();

            //1.判断如何切换页面
            if (x > this.getWidth() / 2.0){
                //向右切换
                this.setCurrentPage((currentPage+1) % numberOfPage);
            }else {
                //向左切换
                this.setCurrentPage((currentPage-1+numberOfPage) % numberOfPage);
            }

            //2.回调事件
            if (this.pageChangeListener != null) {
                pageChangeListener.pageHasChange(this.currentPage);
            }
        }

        //消费事件
        return true;
    }

    /**
     * 接听接口-监听页面的切换
     */
    public interface PageChangeListener {
        //回调当前的页面
        void pageHasChange(int currentPage);
    }

    /**
     * 动画接口-动画是如何切换的
     */
    public interface PageChangeAnimation {
        //页面切换的动画
        void changeAnimation(View last_dot, View current_dot);
    }

    //setter,getter方法
    public int getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(int numberOfPage) {
        this.numberOfPage = numberOfPage;

        //创建页面
        for (int i = 0; i < numberOfPage; i++) {
            //1.创建一个视图
            ImageView dot = new ImageView(getContext());
            //2.设置样式
            dot.setBackgroundResource(this.getPageResource());
            //3.设置约束 大小交给样式文件
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            if (i > 0) {
                layoutParams.leftMargin = this.getPagePadding();
            } else {
                //默认选中第一个点
                dot.setEnabled(false);
            }

            //加入到布局中
            this.addView(dot,layoutParams);
        }
    }

    public int getPagePadding() {
        return pagePadding;
    }

    public void setPagePadding(int pagePadding) {
        this.pagePadding = pagePadding;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        //1.将上一个页面还原为默认状态
        ImageView last_dot = (ImageView) this.getChildAt(this.currentPage);
        last_dot.setEnabled(true);

        //2.改变当前页面
        this.currentPage = currentPage;

        //3.将当前点设置为选中状态
        ImageView current_dot = (ImageView) this.getChildAt(this.currentPage);
        current_dot.setEnabled(false);

        //4.开启动画
        if (this.pageChangeAnimation != null) {
            pageChangeAnimation.changeAnimation(last_dot, current_dot);
        }
    }

    public int getPageResource() {
        return pageResource;
    }

    public void setPageResource(int pageResource) {
        this.pageResource = pageResource;
    }

    public PageChangeListener getPageChangeListener() {
        return pageChangeListener;
    }

    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public PageChangeAnimation getPageChangeAnimation() {
        return pageChangeAnimation;
    }

    public void setPageChangeAnimation(PageChangeAnimation pageChangeAnimation) {
        this.pageChangeAnimation = pageChangeAnimation;
    }
}
