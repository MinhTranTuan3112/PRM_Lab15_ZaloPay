package com.example.lab15_zalopay;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab15_zalopay.Api.CreateOrder;

import org.json.JSONObject;

import java.util.Objects;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPaymentActivity extends AppCompatActivity {

    Button btnPayment;
    TextView txtQuantity, txtTotal;
    long currentTotal;
    int currentAmount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);

        btnPayment = findViewById(R.id.btnPayment);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtTotal = findViewById(R.id.txtTotal);

        Intent intent = getIntent();

        if (intent != null) {
            int amount = intent.getIntExtra("amount", 1);
            currentAmount = amount;
            txtQuantity.setText("So luong: " + currentAmount);
            currentTotal = 1_000_000 * amount;
            txtTotal.setText("Tổng: " + currentTotal);
        }

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });

    }

    private void createOrder() {

        CreateOrder createOrderApi = new CreateOrder();

        try {
            JSONObject data = createOrderApi.createOrder(String.valueOf(currentTotal));
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(OrderPaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) {
                        Log.d("appTransID", appTransID);
                        Log.d("transactionId", transactionId);

                        Intent intent = new Intent(OrderPaymentActivity.this, PaymentNotification.class);
                        intent.putExtra("status", "success");

//                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        Log.d("appTransID", appTransID);
                        Log.d("transactionId", zpTransToken);

                        Intent intent = new Intent(OrderPaymentActivity.this, PaymentNotification.class);
                        intent.putExtra("status", "cancel");

//                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Intent intent = new Intent(OrderPaymentActivity.this, PaymentNotification.class);
                        intent.putExtra("status", "error");

//                        startActivity(intent);
                    }
                });
            }

        } catch (Exception ex) {
            Log.e("ERROR", Objects.requireNonNull(ex.getMessage()));
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}