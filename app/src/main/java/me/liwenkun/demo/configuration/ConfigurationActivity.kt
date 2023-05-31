package me.liwenkun.demo.configuration;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.liwenkun.demo.demoframework.DemoBaseActivity;
import me.liwenkun.demo.R;

public class ConfigurationActivity extends DemoBaseActivity {

    private TextView testOrientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        testOrientation = findViewById(R.id.test_orientation);
        testOrientation.setText(R.string.orientation_string);

        TextView testTranslation = findViewById(R.id.test_translation);
        testTranslation.setText(R.string.test_translation);

        // 只有当设备的语言是某些语言时系统才会根据数量选择不同的字符串，如果是中文，那么系统永远选择 other
        TextView testPlurals = findViewById(R.id.test_plurals);
//        testPlurals.setText(getResources().getQuantityString(R.plurals.test_plurals, 100));
        testPlurals.setText(getResources().getQuantityString(R.plurals.test_plurals, 1));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 在这里系统已经将 Resource 更新成当前屏幕方向的资源了，
        // 所以重新应用资源时，使用的是当前屏幕方向对应的资源
        testOrientation.setText(R.string.orientation_string);
    }
}
