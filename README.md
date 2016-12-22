# RulerView
米尺选择器-横向-常用语金额选择

## 1 效果图
![image](https://github.com/yizeliang/RulerView/raw/master/img/1.png)

![image](https://github.com/yizeliang/RulerView/raw/master/img/2.png)

![image](https://github.com/yizeliang/RulerView/raw/master/img/3.png)

![image](https://github.com/yizeliang/RulerView/raw/master/img/4.png)

![image](https://github.com/yizeliang/RulerView/raw/master/img/5.png)

![image](https://github.com/yizeliang/RulerView/raw/master/img/6.png)

## 2 使用方法

```xml
<cn.yzl.ruleview.library.widget.RulerView
        app:base_line_color="#123439"
        app:line_space="10dp"
        app:line_width="2dp"
        app:max_line_color="#303F9F"
        app:mid_line_color="#17E5A7"
        app:min_line_color="#E3E3E3"
        app:indicator_color="#DB6958"
        app:indicator_size="2dp"
        app:text_size="15sp"
        app:text_color="@color/colorAccent"

        app:base_line_height="1px"

        app:base_line_gravity="top"

        app:text_gravity="bottom"

        app:line_gravity="top"

        app:show_base_line="true"

        app:max_line_count="4"
        app:mid_line_count="2"
        android:id="@+id/ruler"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp" />
```

```java
 but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulerView.setSelPosition(10);
            }
        });

        rulerView.setListener(new RulerListener() {
            @Override
            public void scroll(int position, String content) {
                tv.setText(content);
            }

            @Override
            public void finish(int position, String content) {
                tv.setText(content);
                Toast.makeText(MainActivity.this, "滑动结束", 1).show();
            }
        });

        List<String> data = new ArrayList<>();

        for (int i = 0; i < 101; i++) {
            data.add(String.valueOf(i * 100));
        }
        rulerView.setData(data);

```

## 3 属性说明

| 属性名                | 说明         |
| --------------------- | ------------ |
| app:bottom_line_color | 底部线的颜色 |
| app:line_space        | 刻度间距     |
| app:line_width        | 刻度宽度     |
| app:max_line_color    | 10刻度颜色   |
| app:mid_line_color    | 5刻度颜色    |
| app:min_line_color    | 最小刻度颜色 |
| app:indicator_color   | 指示器颜色   |
| app:indicator_size    | 指示器宽度   |
| app:text_size         | 文字大小     |
| app:text_color        | 文字颜色     |
| app:text_gravity        | 文字位置     |
| app:base_line_color        | 基线颜色     |
| app:base_line_gravity        | 基线位置     |
| app:base_line_height        | 基线高度     |
| app:show_base_line        | 是否显示基线     |
| app:line_gravity        | 刻度位置     |
| app:max_line_count        | 多少个刻度显示一个最长刻度线 默认10     |
| app:mid_line_count        | 多少刻度显示一个中等刻度线 默认5     |

## 4 依赖库

```gradle
//工程gradle
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
//module
 dependencies {
           compile 'com.github.yizeliang:RulerView:1.4'
    }

```

## 5更新日志

### 1.4版本

- 修改版本号
- 加入 设置数量方法

### 1.3版本

- 加入 基线 文字,刻度 的位置变化属性

### 1.2版本

- 修复 选定位置后没有取消惯性的bug

### 1.1版本

- 修复项目滑动相关的bug

### 1.0版本

- 创建项目 实现基本功能

