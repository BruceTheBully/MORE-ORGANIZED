
package com.example.FM;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorModulator implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyroSensor;

    public void start(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroSensor != null) {
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.e("SensorMod", "Gyroscope not available");
        }
    }

    public void stop() {
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values.length >= 2) {
            float[] data = {event.values[0], event.values[1]};
            // Replace with actual injection logic
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
