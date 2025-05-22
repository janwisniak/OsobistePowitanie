package com.example.osobiste;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.osobiste.R;

public class MainActivity extends AppCompatActivity {

    private EditText editTextImie;
    private static final String CHANNEL_ID = "powitanie_channel";
    private static final int REQUEST_CODE_NOTIFICATIONS = 1;
    private String pendingNameForNotification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        editTextImie = findViewById(R.id.editTextImie);
        Button buttonPowitanie = findViewById(R.id.buttonPowitanie);

        buttonPowitanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imie = editTextImie.getText().toString().trim();

                if (imie.isEmpty()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Błąd")
                            .setMessage("Proszę wpisać swoje imię!")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Potwierdzenie")
                            .setMessage("Cześć " + imie + "! Czy chcesz otrzymać powiadomienie powitalne?")
                            .setPositiveButton("Tak, poproszę", (dialog, which) -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                                            Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                        pendingNameForNotification = imie;
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                                REQUEST_CODE_NOTIFICATIONS);
                                    } else {
                                        wyslijPowiadomienie(imie);
                                        Toast.makeText(MainActivity.this, "Powiadomienie zostało wysłane!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    wyslijPowiadomienie(imie);
                                    Toast.makeText(MainActivity.this, "Powiadomienie zostało wysłane!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Nie, dziękuję", (dialog, which) -> {
                                Toast.makeText(MainActivity.this, "Rozumiem. Nie wysyłam powiadomienia.", Toast.LENGTH_SHORT).show();
                            })
                            .show();
                }
            }
        });
    }

    private void wyslijPowiadomienie(String imie) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Witaj!")
                .setContentText("Miło Cię widzieć, " + imie + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1001, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kanał Powitania";
            String description = "Kanał do wyświetlania powitań";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Obsługa odpowiedzi użytkownika na żądanie pozwolenia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingNameForNotification != null) {
                    wyslijPowiadomienie(pendingNameForNotification);
                    Toast.makeText(this, "Powiadomienie zostało wysłane!", Toast.LENGTH_SHORT).show();
                    pendingNameForNotification = null;
                }
            } else {
                Toast.makeText(this, "Brak pozwolenia na powiadomienia.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
