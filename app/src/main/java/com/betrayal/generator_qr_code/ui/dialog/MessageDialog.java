package com.betrayal.generator_qr_code.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.betrayal.generator_qr_code.R;

public class MessageDialog extends Dialog {

    public MessageDialog(@NonNull Context context) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.message_dialog);
    }

    public void show(String title, String description) {
        TextView titleView = findViewById(R.id.title);
        TextView descriptionView = findViewById(R.id.description);

        titleView.setText(title);
        descriptionView.setText(description);
        show();
    }
}
