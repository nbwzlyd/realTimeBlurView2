# realTimeBlurView2
一个实时高斯模糊方案，支持任意图形，（只要你能画出来）任意覆盖色。

性能上个人觉得还可以吧，如果你觉得卡，可以设置一下RealtimeBlurView_realtimeDownsampleFactor 默认是4 ，值大的话卡顿效果会好一点
原理就是在创建bitmap时候并不是创建实际大小的，而是会先缩放，再高斯模糊，再放大，这样性能会更好


![](https://github.com/nbwzlyd/realTimeBlurView2/blob/master/app/gifs/touch.gif)

支持高斯模糊覆盖色 RealtimeBlurView_realtimeOverlayColor
圆角模式
圆形模式 任意模式

使用方法很简单，在你需要高斯模糊的图上，像布局一样写就行了

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/RecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <View.RealtimeBlurView
        android:layout_marginTop="10dp"
        android:id="@+id/RealtimeBlurView"
        android:layout_width="300dp"
        android:layout_height="150dp"
        android:layout_centerHorizontal="true"/>

    <View.RoundCornerBlurView
        android:id="@+id/RoundCornerBlurView"
        android:layout_width="match_parent"
        android:layout_marginRight="10dp"
        android:layout_marginLeft="10dp"
        android:layout_height="150dp"
        android:layout_below="@id/RealtimeBlurView"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="40dp"
        app:realtimeBlurRoundCornerRadius="6dp"
        app:realtimeOverlayColor="#5a4B0082"/>
</RelativeLayout>


如果你觉得这样性能不好，你可以单独设置要高斯模糊的图片，利用bindView（View targetView）方法
