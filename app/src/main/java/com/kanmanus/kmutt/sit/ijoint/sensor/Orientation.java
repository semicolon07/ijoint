
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

  private static final int SENSOR_DELAY_MICROS = 100 * 1000; // 10ms

  private final SensorManager mSensorManager;
  private final WindowManager mWindowManager;
//  private final Sensor mOrientationSensor;
  //private final Sensor mMagneticSensor;
  private final Sensor mRotateMatrix;
  private int mLastAccuracy;
  private Listener mListener;

  public Orientation(SensorManager sensorManager, WindowManager windowManager) {
    mSensorManager = sensorManager;
    mWindowManager = windowManager;
    // Can be null if the sensor hardware is not available
   // mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
   // mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    mRotateMatrix = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
  }

  public void startListening(Listener listener) {
    if (mListener == listener) {
      return;
    }
    mListener = listener;
  //  mSensorManager.registerListener(this, mOrientationSensor, SENSOR_DELAY_MICROS);
    mSensorManager.registerListener(this,mRotateMatrix,SENSOR_DELAY_MICROS);
   // mSensorManager.registerListener(this, mMagneticSensor, SENSOR_DELAY_MICROS);
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
//    if (event.sensor == mOrientationSensor) {
//      setOrientation(event.values,screenRotation);
//    }
//    if(event.sensor == mMagneticSensor){
//      screenRotation = event.values[2];
//    }
    if(event.sensor == mRotateMatrix){
      float[] rMatrix = new float[9];
      float[] orientationMatrix = new float[9];

      SensorManager.getRotationMatrixFromVector(rMatrix, event.values);
      SensorManager.getOrientation(rMatrix, orientationMatrix);
      setOrientation(orientationMatrix);
     }
  }

  private void setOrientation(float[] orientationMatrix){
    float azimuth = (float)(Math.toDegrees((orientationMatrix[0])) + 180.0);
    float pitch = (float)(Math.toDegrees(orientationMatrix[1]));
    float roll = (float)(Math.toDegrees(orientationMatrix[2]));

    mListener.onOrientationChanged(azimuth, pitch, roll,pitch);
  }

//  private void setOrientation(float[] orientationMatrix, float magneticRoll){
//    float azimuth = orientationMatrix[0];
//    float pitch = orientationMatrix[1];
//    float roll = orientationMatrix[2];
//
//    mListener.onOrientationChanged(azimuth, pitch, roll,magneticRoll);
//  }

}
