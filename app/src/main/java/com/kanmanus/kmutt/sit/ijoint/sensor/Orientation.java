
package com.kanmanus.kmutt.sit.ijoint.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.WindowManager;


public class Orientation implements SensorEventListener {

  private float screenRotation;

  public interface Listener {
    void onOrientationChanged(float azimuth, float pitch, float roll ,float magneticRoll);
  }

  private static final int SENSOR_DELAY_MICROS = 100 * 1000; // 50ms

  private final SensorManager mSensorManager;
  private final WindowManager mWindowManager;
  private final Sensor mOrientationSensor;
  private final Sensor mMagneticSensor;
  private int mLastAccuracy;
  private Listener mListener;

  public Orientation(SensorManager sensorManager, WindowManager windowManager) {
    mSensorManager = sensorManager;
    mWindowManager = windowManager;
    // Can be null if the sensor hardware is not available
    mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
  }

  public void startListening(Listener listener) {
    if (mListener == listener) {
      return;
    }
    mListener = listener;
    if (mOrientationSensor == null) {
      Log.w(">>", "Rotation vector sensor not available; will not provide orientation data.");
      return;
    }
    mSensorManager.registerListener(this, mOrientationSensor, SENSOR_DELAY_MICROS);
    mSensorManager.registerListener(this, mMagneticSensor, SENSOR_DELAY_MICROS);
  }

  public void stopListening() {
    mSensorManager.unregisterListener(this);
    mListener = null;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    if (mLastAccuracy != accuracy) {
      mLastAccuracy = accuracy;
    }
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (mListener == null) {
      return;
    }
    if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
      return;
    }
    if (event.sensor == mOrientationSensor) {

      setOrientation(event.values,screenRotation);
      //updateOrientation(event.values);
    }
    if(event.sensor == mMagneticSensor){
      screenRotation = event.values[2];
    }
  }

  private void setOrientation(float[] orientationMatrix, float magneticRoll){
    float azimuth = orientationMatrix[0];
    float pitch = orientationMatrix[1];
    float roll = orientationMatrix[2];

    mListener.onOrientationChanged(azimuth, pitch, roll,magneticRoll);
  }
//  private void updateOrientation(float[] rotationVector) {
//    // Transform rotation matrix into azimuth/pitch/roll
//    float[] orientation = new float[3];
//    float[] rotationMatrix = new float[9];
//    float[] qMatrix = new float[4];
//    mSensorManager.getRotationMatrixFromVector(rotationMatrix,rotationVector);
//    SensorManager.getOrientation(rotationMatrix, orientation);
//    mSensorManager.getQuaternionFromVector(qMatrix,rotationVector);
//    Log.d("quater Y ","X"+qMatrix[0]+" Y"+qMatrix[1]+" Z"+qMatrix[2]+" W"+qMatrix[3]);
//    //Log.d("rotationMatrix"," rotationMatrix => {"+rotationMatrix[0]+","+rotationMatrix[1]+","+rotationMatrix[2]+","+rotationMatrix[3]+","+rotationMatrix[4]+","+rotationMatrix[5]+","+rotationMatrix[6]+","+rotationMatrix[7]+","+rotationMatrix[8]+"}");
//    // Convert radians to degrees
//    float azimuth = (float)Math.toDegrees(orientation[0]);
//    float pitch = (float)Math.toDegrees(orientation[1]);
//    float roll = (float)Math.toDegrees(orientation[2]);
//    int screenRotation = 0;
//    mListener.onOrientationChanged(azimuth, pitch, roll,screenRotation);
//  }
}
