package com.betrayal.generator_qr_code.scripts;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.betrayal.generator_qr_code.R;
import com.betrayal.generator_qr_code.ui.dialog.MessageDialog;
import com.google.zxing.Result;

public class QrCodeRunnable implements Runnable {
    public static final String KEY = "text";
    private final View view;
    private final Result result;

    public QrCodeRunnable(View view, Result result) {
        this.result = result;
        this.view = view;
    }

    @Override
    public void run() {
        try {
            String resultText = result.getText();
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString(KEY, resultText);
            navController.navigate(R.id.action_navigation_home_to_scanningQrCodeFragment, bundle);
        } catch (Exception e) {
            MessageDialog dialog = new MessageDialog(view.getContext());
            dialog.show("Ошибка!", "Произошла ошибка при сканировании QR-кода, попробуйте ещё раз!");
            e.printStackTrace();
            Log.e("QR_CODE_SCANNING", e.getMessage());
        }
    }
}
