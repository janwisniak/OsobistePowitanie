package com.example.licznik;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.licznik.R;

public class MainActivity extends AppCompatActivity {

    //liczba startowa licznika i textview do wyswietlania jaka liczba w nim jest
    private int counter = 0;
    private TextView textViewCounter;

    private static final String COUNTER_KEY = "COUNTER_KEY";
    // tu stworzenie aplikacji
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //laczy jave z xml
        textViewCounter = findViewById(R.id.textViewCounter);
        Button buttonIncrement = findViewById(R.id.buttonIncrement);

        // tu jezeli savedInstanceState NIE ! jest puste to odczytuje zapisany wczesniej stan czyli cunter
        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt(COUNTER_KEY, 0);
            textViewCounter.setText(String.valueOf(counter));
        }

        // tu jest jak dziala przycisk
        buttonIncrement.setOnClickListener(v -> {
            counter++;
            textViewCounter.setText(String.valueOf(counter));
        });
    }
        //zapisanie tego co bylo przed obrotem ekranu
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNTER_KEY, counter); // zapis stanu licznika
    }
}
