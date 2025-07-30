
package com.example.FM;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.FM.ui.theme.MyApplicationTheme;

class MainActivity extends ComponentActivity {
    private final int AUDIO_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkAudioPermission()) startFMService();
        setContent(() -> MyApplicationTheme.INSTANCE);
    }

    private boolean checkAudioPermission() {
        String permission = Manifest.permission.RECORD_AUDIO;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, AUDIO_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    private void startFMService() {
        Intent serviceIntent = new Intent(this, FMRadioService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUDIO_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startFMService();
        }
    }
}
