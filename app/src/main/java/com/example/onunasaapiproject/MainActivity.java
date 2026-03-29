package com.example.onunasaapiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String bgImageURL = "https://www.freepik.com/free-photo/3d-view-planet-earth_45149331.htm#fromView=keyword&page=1&position=2&uuid=a348b8c1-9082-4ae7-9b9b-e33b3bfaeefa&query=World+blurry";
        Picasso.get().load(bgImageURL).centerCrop();
    }

    public void onClick(View view)
    {
        Button button = findViewById(R.id.btnFindThreat);

        Intent intent= new Intent(MainActivity.this, MostDangerousAsteroidInfo.class);

        startActivity(intent);
    }
}