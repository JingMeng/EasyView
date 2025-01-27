EasyView
=====

&emsp;&emsp;这是一个简单方便的Android自定义View库，我一直有一个想法弄一个开源库，现在这个想法付诸实现了，如果有什么需要自定义的View可以提出来，不一定都会采纳，合理的会采纳，时间周期不保证，咱要量力而行呀，踏实一点。

配置EasyView
--------

### 1. 工程build.gradle 或 settings.gradle配置

&emsp; &emsp;代码已经推送到`MavenCentral()`，在`Android Studio 4.2`以后的版本中默认在创建工程的时候使用`MavenCentral()`，而不是`jcenter()`。

&emsp; &emsp;如果是之前的版本则需要在`repositories{}`闭包中添加`mavenCentral()`，不同的是，老版本的Android Studio是在工程的`build.gradle`中添加，而新版本是工程的`settings.gradle`中添加，**如果已经添加，则不要重复添加。**

```gradle
repositories {
    ...
    mavenCentral()
}
```


### 2. 使用模块的build.gradle配置

&emsp; &emsp;例如在`app`模块中使用，则打开app模块下的`build.gradle`，在`dependencies{}`闭包下添加即可，之后记得要`Sync Now`。

```gradle
dependencies {
    implementation 'io.github.lilongweidev:easyview:1.0.3'
}
```

使用EasyView
--------


这是一个自定义View的库，会慢慢丰富里面的自定义View，我先画个饼再说。

一、MacAddressEditText
------

`MacAddressEditText`是一个蓝牙Mac地址输入控件，点击之后出现一个定制的Hex键盘，用于输入值。

### 1. xml中使用

首先是在xml中添加如下代码，具体参考app模块中的`activity_mac_address.xml`。

```xml
    <com.easy.view.MacAddressEditText
        android:id="@+id/mac_et"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:boxBackgroundColor="@color/white"
        app:boxStrokeColor="@color/black"
        app:boxStrokeWidth="2dp"
        app:boxWidth="48dp"
        app:separator=":"
        app:textColor="@color/black"
        app:textSize="14sp" />
```

### 2. 属性介绍

这里使用了`MacAddressEditText`的所有属性，可以自行进行设置，使用说明参考下表。

| 属性                            | 说明                   |
|:------------------------------|:---------------------|
| app:boxBackgroundColor        | 设置输入框的背景颜色           | 
| app:boxStrokeColor            | 设置输入框的边框颜色           |       
| app:boxStrokeWidth            | 设置输入框的边框大小           |           
| app:boxWidth                  | 设置输入框大小              |
| app:separator                 | Mac地址的分隔符，例如分号：      |
| app:textColor                 | 设置输入框文字颜色            |
| app:textSize                  | 设置输入框文字大小            |

### 3. 代码中使用

```java
    MacAddressEditText macEt = findViewById(R.id.mac_et);
    String macAddress = macEt.getMacAddress();
```

macAddress可能会是空字符串，使用之前请判断一下，参考app模块中的`MacAddressActivity`中的使用方式。


二、CircularProgressBar
------

`CircularProgressBar`是圆环进度条控件。

### 1. xml中使用

首先是在xml中添加如下代码，具体参考app模块中的`activity_progress_bar.xml`。

```xml
    <com.easy.view.CircularProgressBar
        android:id="@+id/cpb_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        app:maxProgress="100"
        app:progress="10"
        app:progressbarBackgroundColor="@color/purple_500"
        app:progressbarColor="@color/purple_200"
        app:radius="80dp"
        app:strokeWidth="16dp"
        app:text="10%"
        app:textColor="@color/teal_200"
        app:textSize="28sp" />
```

### 2. 属性介绍

这里使用了`MacAddressEditText`的所有属性，可以自行进行设置，使用说明参考下表。

| 属性                             | 说明           |
|:-------------------------------|:-------------|
| app:maxProgress                | 最大进度         | 
| app:progress                   | 当前进度         |       
| app:progressbarBackgroundColor | 进度条背景颜色      |           
| app:progressbarColor           | 进度颜色         |
| app:radius                     | 半径，用于设置圆环的大小 |
| app:strokeWidth                | 进度条大小        |
| app:text                       | 进度条中心文字      |
| app:textColor                  | 进度条中心文字颜色    |
| app:textSize                   | 进度条中心文字大小    |


### 3. 代码中使用

```java
    CircularProgressBar cpbTest = findViewById(R.id.cpb_test);
    int progress = 10;
    cpbTest.setText(progress + "%");
    cpbTest.setProgress(progress);
```

参考app模块中的`ProgressBarActivity`中的使用方式。

三、TimingTextView
------

TimingTextView是计时文字控件

### 1. xml中使用

首先是在xml中添加如下代码，具体参考app模块中的`activity_timing_text.xml`。

```xml
    <com.easy.view.TimingTextView
        android:id="@+id/tv_timing"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        android:text="计时文字"
        android:textColor="@color/black"
        android:textSize="32sp"
        app:countdown="false"
        app:max="60"
        app:unit="s" />
```

### 2. 属性介绍

这里使用了`TimingTextView`的自定义属性不多，只有3个，TextView的属性就不列举说明，使用说明参考下表。

| 属性                   | 说明                  |
|:---------------------|:--------------------|
| app:countdown        | 是否倒计时               | 
| app:max              | 最大时间长度              |       
| app:unit             | 时间单位：s（秒）、m（分）、h（时） |           


### 3. 代码中使用

```java
    TimingTextView tvTiming = findViewById(R.id.tv_timing);
    tvTiming.setMax(6);//最大时间
    tvTiming.setCountDown(false);//是否倒计时
    tvTiming.setUnit(3);//单位 秒
    tvTiming.setListener(new TimingListener() {
        @Override
        public void onEnd() {
            //定时结束
        }
    });
    //开始计时
    tvTiming.start();
    //停止计时
    //tvTiming.end();
```

参考app模块中的`TimingActivity`中的使用方式。

四、EasyEditText
------

`EasyEditText`是一个简易输入控件，可用于密码框、验证码输入框进行使用。

### 1. xml中使用

首先是在xml中添加如下代码，具体参考app模块中的`activity_easy_edittext.xml`。

```xml
    <com.easy.view.EasyEditText
        android:id="@+id/et_content"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:boxBackgroundColor="@color/white"
        app:boxFocusStrokeColor="@color/green"
        app:boxNum="6"
        app:boxStrokeColor="@color/black"
        app:boxStrokeWidth="2dp"
        app:boxWidth="48dp"
        app:ciphertext="false"
        app:textColor="@color/black"
        app:textSize="16sp" />
```

### 2. 属性介绍

这里使用了`EasyEditText`的所有属性，可以自行进行设置，使用说明参考下表。

| 属性                      | 说明            |
|:------------------------|:--------------|
| app:boxBackgroundColor  | 设置输入框的背景颜色    |
| app:boxFocusStrokeColor | 设置输入框获取焦点时的颜色 | 
| app:boxNum              | 设置输入框的个数，4~6个 |    
| app:boxStrokeColor      | 设置输入框的边框颜色    |       
| app:boxStrokeWidth      | 设置输入框的边框大小    |           
| app:boxWidth            | 设置输入框大小       |
| app:ciphertext          | 是否密文，用于密码框    |
| app:textColor           | 设置输入框文字颜色     |
| app:textSize            | 设置输入框文字大小     |

### 3. 代码中使用

```java
        binding.cbFlag.setOnCheckedChangeListener((buttonView, isChecked) -> {
        binding.etContent.setCiphertext(isChecked);
        binding.cbFlag.setText(isChecked ? "密文" : "明文");
        });
        //输入框
        binding.btnGetContent.setOnClickListener(v -> {
        String content = binding.etContent.getText();
        if (content.isEmpty()) {
        showMsg("请输入内容");
        return;
        }
        if (content.length() < binding.etContent.getBoxNum()) {
        showMsg("请输入完整内容");
        return;
        }
        showMsg("输入内容为：" + content);
        });
```

参考app模块中的`EasyEditTextActivity`中的使用方式。


