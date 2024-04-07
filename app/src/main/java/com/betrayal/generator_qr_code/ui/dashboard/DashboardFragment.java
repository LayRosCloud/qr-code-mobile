package com.betrayal.generator_qr_code.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.betrayal.generator_qr_code.databinding.FragmentDashboardBinding;
import com.betrayal.generator_qr_code.ui.dialog.MessageDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private Bitmap bitmap;
    private Button saveButton;
    private ImageView qrCodeImage;
    private EditText editTextView;

    private static final String REQUEST_PERMISSION_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String REQUEST_PERMISSION_READ = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int REQUEST_CODE = 213;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        qrCodeImage = binding.qrCode;
        editTextView = binding.text;
        saveButton = binding.saveButton;
        final Button generatorButton = binding.generateButton;
        generatorButton.setOnClickListener(this::generate);
        saveButton.setOnClickListener(this::save);

        return root;
    }

    private void checkPermissionsAndSave() {
        final String[] requestPermissions = new String[]{
                REQUEST_PERMISSION_WRITE,
        };

        Activity activity = getActivity();

        if (activity.checkSelfPermission(REQUEST_PERMISSION_WRITE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, requestPermissions, REQUEST_CODE);
        } else {
            saveImage();
        }
    }

    private void generate(View view) {
        String text = editTextView.getText().toString();
        if(text.isEmpty()) {
            MessageDialog dialog = new MessageDialog(getContext());
            dialog.show("Ошибка!", "Введите какое-либо значение");
            return;
        }
        text = text.trim();
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(bitMatrix);
            saveButton.setVisibility(View.VISIBLE);
            saveButton.setEnabled(true);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException ex) {
            MessageDialog dialog = new MessageDialog(getContext());
            dialog.show("Ошибка!", "Невозможно сгенерировать QR-код");
            ex.printStackTrace();
        }
    }

    private void saveImage() {
        MessageDialog dialog = new MessageDialog(getContext());
        try {
            saveButton.setEnabled(false);
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    bitmap,
                    UUID.randomUUID() + ".jpg", "qr-code");
            dialog.show("Успех!", "QR-код сохранен в папку Pictures");
        } catch (Exception ex) {
            dialog.show("Ошибка!", "QR-код не удалось сохранить...");
        }


    }

    private void save(View view) {
        checkPermissionsAndSave();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}