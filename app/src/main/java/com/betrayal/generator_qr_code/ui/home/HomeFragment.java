package com.betrayal.generator_qr_code.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.betrayal.generator_qr_code.databinding.FragmentHomeBinding;
import com.betrayal.generator_qr_code.scripts.QrCodeDecode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private CodeScanner codeScanner;

    private static final int REQUEST_CODE = 2_132;

    private static final String REQUEST_PERMISSION = Manifest.permission.CAMERA;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        checkPermissions();
        CodeScannerView scannerView = binding.scanner;
        codeScanner = new CodeScanner(getActivity(), scannerView);
        codeScanner.setDecodeCallback(new QrCodeDecode(getActivity(), root));
        scannerView.setOnClickListener(this::clickOnScannerView);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        codeScanner.stopPreview();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void clickOnScannerView(View view) {
        codeScanner.startPreview();
    }

    private void checkPermissions() {
        final String[] requestPermissions = new String[]{
                REQUEST_PERMISSION
        };

        Activity activity = getActivity();

        if (activity.checkSelfPermission(REQUEST_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, requestPermissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE) {
            checkPermissions();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}