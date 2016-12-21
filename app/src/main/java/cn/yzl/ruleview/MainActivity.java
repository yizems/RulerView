package cn.yzl.ruleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.yzl.ruleview.library.widget.RulerListener;
import cn.yzl.ruleview.library.widget.RulerView;

public class MainActivity extends AppCompatActivity {


    private RulerView rulerView;

    private Button but;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rulerView = (RulerView) findViewById(R.id.ruler);

        tv = (TextView) findViewById(R.id.tv);
        but = (Button) findViewById(R.id.but);
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
    }
}
