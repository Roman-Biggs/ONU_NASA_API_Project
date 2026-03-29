package com.example.onunasaapiproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostDangerousAsteroidInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_most_dangerous_asteroid_info);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String bgImageURL = "https://www.freepik.com/free-photo/3d-view-planet-earth_45149331.htm#fromView=keyword&page=1&position=2&uuid=a348b8c1-9082-4ae7-9b9b-e33b3bfaeefa&query=World+blurry";
        Picasso.get().load(bgImageURL).centerCrop();

        //Text views declaration
        TextView warningLabel = findViewById(R.id.tvWarningLabel);
        TextView asteroidName = findViewById(R.id.tvAsteroidName);
        TextView asteroidDiameter = findViewById(R.id.tvDiameter);
        TextView asteroidSpeed = findViewById(R.id.tvSpeed);
        TextView asteroidMissDistance = findViewById(R.id.tvMissDistance);

        //Retrofit entity builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Query entity
        NasaNeoFeedAPI nasaNeoFeedAPI = retrofit.create(NasaNeoFeedAPI.class);

        //Definition of query parameters
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateString = today.format(formatter);
        String personalAPIKey = "MvEZ0QtiAWp3dqBabgl3hj6m25oGPkiIHYpcc4u7";

        //Query
        nasaNeoFeedAPI.getAsteroids(currentDateString, currentDateString, personalAPIKey).enqueue(new Callback<NeoResponse>() {
            @Override
            public void onResponse(Call<NeoResponse> call, Response<NeoResponse> response)
            {
                if(response.isSuccessful() && response.body() != null)
                {
                    Map<String, List<AsteroidInfo>> allAsteroidsForAllDays = response.body().getNearEarthObjects();

                    if(allAsteroidsForAllDays != null)
                    {
                        List<AsteroidInfo> todayAsteroids = allAsteroidsForAllDays.get(currentDateString);

                        if (todayAsteroids != null)
                        {
                            //Potential candidate (depends on destructiveness index)
                            AsteroidInfo threateningAsteroid = null;
                            double maxDisctructivenessIndex = -1.0;

                            for(AsteroidInfo asteroid : todayAsteroids)
                            {
                                if(Boolean.TRUE.equals(asteroid.getIsPotentiallyHazardousAsteroid()))
                                {
                                    try
                                    {
                                        //Maximal possible diameter of asteroid (in meters)
                                        double diameter = asteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMax();

                                        // In JSON this speed provided in list 'close_approach_data' as 1-st element, parsing Double value
                                        String speedString = asteroid.getCloseApproachData().get(0).getRelativeVelocity().getKilometersPerSecond();
                                        double speed = Double.parseDouble(speedString);

                                        // Let index be speed * diameter^2
                                        double destructivenessIndex = speed * Math.pow(diameter, 2);

                                        if(destructivenessIndex > maxDisctructivenessIndex)
                                        {
                                            maxDisctructivenessIndex = destructivenessIndex;
                                            threateningAsteroid = asteroid;
                                        }

                                        Log.i("Asteroid Name", threateningAsteroid.getName());
                                    } catch (Exception e)
                                    {
                                        Log.i("NASA_LOG", "Failed to parse asteroid information about " + asteroid.getName());
                                    }
                                }
                            }

                            if(threateningAsteroid != null)
                            {
                                asteroidName.setText(threateningAsteroid.getName());
                                asteroidName.setTextColor(android.graphics.Color.RED);
                                asteroidDiameter.setText(String.format("Диаметр: %.2f м", threateningAsteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMax()));
                                asteroidDiameter.setTextColor(android.graphics.Color.RED);
                                asteroidSpeed.setText("Скорость: " + threateningAsteroid.getCloseApproachData().get(0).getRelativeVelocity().getKilometersPerSecond() + " км/с");
                                asteroidSpeed.setTextColor(android.graphics.Color.RED);
                                asteroidMissDistance.setText("Пролетит на удалении: " + threateningAsteroid.getCloseApproachData().get(0).getMissDistance().getKilometers() + "км");
                                asteroidMissDistance.setTextColor(android.graphics.Color.RED);
                            }
                            else
                            {
                                warningLabel.setText("");
                                asteroidName.setText("Угроз не обнаружено!");
                                asteroidName.setTextColor(android.graphics.Color.GREEN);
                                asteroidDiameter.setText("");
                                asteroidSpeed.setText("");
                                asteroidMissDistance.setText("");
                            }

                        }
                    }
                }

                Log.i("Roman", "OK");
            }

            @Override
            public void onFailure(Call<NeoResponse> call, Throwable t) {
                Log.i("Roman","Failure" + t);
            }
        });
    }
}