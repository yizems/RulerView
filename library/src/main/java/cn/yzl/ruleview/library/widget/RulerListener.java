package cn.yzl.ruleview.library.widget;

/**
 * Created by 伊 on 2016/12/21.
 */
public interface RulerListener {
    void scroll(int position, String content);
    void finish(int position, String content);
}
