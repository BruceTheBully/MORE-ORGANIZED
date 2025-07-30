
package com.example.FM;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

public class FMRadioService extends Service {
    private boolean isRunning = false;
    private AudioRecord recorder;
    private Thread recordingThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            startRecording();
        }
        return START_STICKY;
    }

    private void startRecording() {
        int sampleRate = 192000;
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        recorder.startRecording();
        recordingThread = new Thread(() -> {
            byte[] buffer = new byte[bufferSize];
            while (isRunning) {
                int read = recorder.read(buffer, 0, buffer.length);
                if (read > 0) {
                    byte[] processed = DustDSPProcessor.process(buffer, read);
                }
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        if (recorder != null) {
            recorder.stop();
            recorder.release();
        }
        if (recordingThread != null) {
            recordingThread.interrupt();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
