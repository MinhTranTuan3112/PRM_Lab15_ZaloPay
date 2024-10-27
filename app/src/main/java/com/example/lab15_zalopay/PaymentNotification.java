package com.example.lab15_zalopay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentNotification extends AppCompatActivity {

    TextView txtResult;
    String resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtResult = findViewById(R.id.txtResult);

        Intent intent = getIntent();

        if (intent == null) {
            return;
        }

        String status = intent.getStringExtra("status");

        if (status == null || status.isEmpty()) {
            return;
        }

        switch (status) {
            case "success":
                resultText = "Thanh toan thanh cong";
                break;
            case "cancel":
                resultText = "Da huy";
                break;
            case "error":
                resultText = "Loi khi thanh toan";
                break;
            default:
                break;
        }

    }
}