package com.namah.swipemodeview.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.namah.swipemodeview.SwipeModeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) SwipeModeView swipeModeView = findViewById(R.id.swipeView);

        swipeModeView.setOnModeChangeListener(mode -> {
            if (mode == SwipeModeView.Mode.LEFT) {
                // Left mode active
            } else {
                // Right mode active
            }
        });

// Programmatic control
//        swipeModeView.setSwipeEnabled(true);
//        swipeModeView.setMode(SwipeModeView.MODE_END);


    }
}