package com.example.yangchenglei.twelfday_switchtoggleview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SwitchToggleView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (SwitchToggleView) findViewById(R.id.stv);
        mView.setSwitchBackground(R.drawable.switch_background);
        mView.setSwitchSlide(R.drawable.slide_button_background);

        mView.setOnSwitchListener(new SwitchToggleView.OnSwitchListenter() {
            @Override
            public void onSwitchChanged(boolean isOpened) {
                Toast.makeText(getApplicationContext(),isOpened ? "打开":"关闭",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
