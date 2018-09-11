# realTimeBlurView2
## 一个实时高斯模糊方案，支持任意图形，（只要你能画出来）任意覆盖色。

性能上个人觉得还可以吧，如果你觉得卡，可以设置一下RealtimeBlurView_realtimeDownsampleFactor 默认是4 ，值大的话卡顿效果会好一点
原理就是在创建bitmap时候并不是创建实际大小的，而是会先缩放，再高斯模糊，再放大，这样性能会更好


![](https://github.com/nbwzlyd/realTimeBlurView2/blob/master/app/gifs/touch.gif)

支持高斯模糊覆盖色 RealtimeBlurView_realtimeOverlayColor
圆角模式
圆形模式 任意模式

### Jcenter
```
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.nbwzlyd:realTimeBlurView2:v0.1'
	}

```
### Maven
```
<repositories>
  <repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
  </repository>
</repositories>
Step 2. Add the dependency

<dependency>
	    <groupId>com.github.nbwzlyd</groupId>
	    <artifactId>realTimeBlurView2</artifactId>
	    <version>v0.1</version>
</dependency>
```


使用方法很简单，在你需要高斯模糊的图上，像布局一样写就行了
```
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
```
如果你觉得这样性能不好，你可以单独设置要高斯模糊的图片，利用bindView（View targetView）方法
```
public RealtimeBlurView bindView(View targetView) {
        mTargetView = targetView;
        post(new Runnable() {
            @Override
            public void run() {
                mTargetView.getViewTreeObserver().addOnPreDrawListener(mBlurPreDrawListener);
            }
        });
        return this;
    }
    
 ```  
   
用FastBlur实现，兼容性上没问题，性能上，看你的设置了 realtimeBlurRadius  和realtimeDownsampleFactor  
其中realtimeBlurRadius不要超过25。
我自己测试，模糊也就几毫秒的时间，很不错了。
    鄙人不太喜欢写库也没能力写很牛逼的库，代码写的都很简单。人人都能看懂
我也是参考的github上大神的代码，做了适度精简，替换了renderscript方案，采用fastBlur，兼容性好，逻辑更清晰，但是健壮性就差很多了，拿来自己用，bug也好修复。
