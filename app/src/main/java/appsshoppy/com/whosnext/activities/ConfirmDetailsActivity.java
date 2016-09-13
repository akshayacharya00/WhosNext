package appsshoppy.com.whosnext.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import appsshoppy.com.whosnext.R;

public class ConfirmDetailsActivity extends AppCompatActivity {

    private Button btnEverBefore, btnConfirmPayment, btnCancellationPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        btnEverBefore = (Button) findViewById(R.id.btnEverBefore);
        btnEverBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ConfirmDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirmation_pop_up);
                dialog.show();

            }
        });

        btnConfirmPayment = (Button) findViewById(R.id.btnConfirmPayment);
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmDetailsActivity.this,PaymentDetailsActivity.class));
            }
        });

        btnCancellationPolicy = (Button) findViewById(R.id.btnCancellationPolicy);
        btnCancellationPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ConfirmDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.cancellation_pop_up);
                dialog.show();
            }
        });

    }
}
