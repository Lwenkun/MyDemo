package me.liwenkun.demo.demoframework;

import android.os.Bundle;

import androidx.annotation.Nullable;

import me.liwenkun.demo.R;

public class DemoFragmentActivity extends DemoBaseActivity {

    public static final String EXTRA_FRAGMENT_NAME = "fragment_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        String fragmentName = getIntent().getStringExtra(EXTRA_FRAGMENT_NAME);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, getSupportFragmentManager()
                .getFragmentFactory().instantiate(getClassLoader(), fragmentName), "demo-fragment")
                .commitNow();
    }
}
