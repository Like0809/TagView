package com.like.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.like.widget.tagview.R;

public class MainActivity extends AppCompatActivity {

    private TagView mTagView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTagView = findViewById(R.id.tagView);

        findViewById(R.id.red_color).setOnClickListener(v -> mTagView.newTag("红色文字").textColor(Color.RED)
                .background(new ColorDrawable(Color.parseColor("#aaaaaa"))).add());

        findViewById(R.id.green_color).setOnClickListener(v -> mTagView.newTag("绿色文字").textColor(Color.GREEN)
                .background(new ColorDrawable(Color.parseColor("#aaaaaa"))).add());

        findViewById(R.id.blue_color).setOnClickListener(v -> mTagView.newTag("蓝色文字").textColor(Color.BLUE)
                .background(new ColorDrawable(Color.parseColor("#aaaaaa"))).add());

        findViewById(R.id.SP12).setOnClickListener(v -> mTagView.newTag("12SP").textSize(24)
                .background(new ColorDrawable(Color.parseColor("#aaaaaa"))).add());

        findViewById(R.id.SP18).setOnClickListener(v -> mTagView.newTag("18SP").textSize(36)
                .background(new ColorDrawable(Color.parseColor("#aaaaaa"))).add());

        findViewById(R.id.SP32).setOnClickListener(v -> mTagView.newTag("32SP").textSize(64)
                .background(new ColorDrawable(Color.parseColor("#aaaaaa"))).add());

        findViewById(R.id.no_bg).setOnClickListener(v -> mTagView.newTag("无背景").padding(8).add());

        findViewById(R.id.color_bg).setOnClickListener(v -> mTagView.newTag("纯色背景")
                .padding(8).textSize(24).textColor(Color.YELLOW).background(new ColorDrawable(Color.parseColor("#ff9900"))).add());

        findViewById(R.id.shape_bg).setOnClickListener(v -> mTagView.newTag("shape背景")
                .padding(8).background(getResources().getDrawable(R.drawable.background)).add());

        findViewById(R.id.normal).setOnClickListener(v -> mTagView.newTag("未选中")
                .padding(8).background(getResources().getDrawable(R.drawable.background)).add());

        findViewById(R.id.select).setOnClickListener(v -> mTagView.newTag("已选中")
                .padding(8).background(getResources().getDrawable(R.drawable.background)).selected(true).add());

        findViewById(R.id.padding4).setOnClickListener(v -> mTagView.newTag("内边距4DP")
                .padding(8).background(new ColorDrawable(getResources().getColor(R.color.colorPrimary))).add());

        findViewById(R.id.padding8).setOnClickListener(v -> mTagView.newTag("内边距8DP")
                .padding(16).background(new ColorDrawable(getResources().getColor(R.color.colorPrimary))).add());
    }
}
