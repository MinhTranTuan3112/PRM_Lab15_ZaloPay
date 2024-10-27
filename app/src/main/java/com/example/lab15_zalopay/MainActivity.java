package com.example.lab15_zalopay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText etAmount;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etAmount = findViewById(R.id.etAmount);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(view -> {
            if (etAmount.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter the amount!", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(etAmount.getText().toString());

            Intent intent = new Intent(MainActivity.this, OrderPaymentActivity.class);

            intent.putExtra("amount", amount);

            startActivity(intent);

        });
    }

}