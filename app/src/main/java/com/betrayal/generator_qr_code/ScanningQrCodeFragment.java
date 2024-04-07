package com.betrayal.generator_qr_code;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.betrayal.generator_qr_code.databinding.FragmentScanningQrCodeBinding;
import com.betrayal.generator_qr_code.scripts.QrCodeRunnable;

public class ScanningQrCodeFragment extends Fragment {

    private String resultText;
    private FragmentScanningQrCodeBinding binding;
    private Button copyButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentScanningQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle arguments = getArguments();

        assert arguments != null;

        final TextView textView = binding.text;
        final Button navigateButton = binding.navigateSite;
        copyButton = binding.copy;

        resultText = arguments.getString(QrCodeRunnable.KEY).trim();
        textView.setText(resultText);
        boolean hasLink = resultText.startsWith("http");
        boolean noHasSpaces = !resultText.contains(" ");
        navigateButton.setVisibility(hasLink && noHasSpaces ? View.VISIBLE : View.GONE);
        navigateButton.setOnClickListener(this::navigateToSite);
        copyButton.setOnClickListener(this::copyText);

        return root;
    }

    private void navigateToSite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultText));
        startActivity(intent);
    }

    private void copyText(View view) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", resultText);
        clipboard.setPrimaryClip(clip);
        copyButton.setText("СКОПИРОВАНО");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}