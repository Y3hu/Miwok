package com.udacity.miwok;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.miwok.fragments.ColorsFragment;

public class ColorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ColorsFragment())
                .commit();

    }
}
