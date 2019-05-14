package dk.easv.geoshare.CustomCamera;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

public class RotationHelper implements SensorEventListener {
    private static int SENSOR_DELAY = 500 * 1000; // 500ms
    private View[] rotatingViews;
    private int currentRotation;
    private Sensor mRotationSensor;

    public RotationHelper(View[] viewsToRotate, SensorManager sensorManager) {
        this.rotatingViews = viewsToRotate;
        mRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    public int getJPEGOrientation(int sensorOrientation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        if (currentRotation == 0){
            currentRotation = 90;
        } else if (currentRotation == 90){
            currentRotation = 0;
        } else if (currentRotation == 180){
            currentRotation = 270;
        } else if (currentRotation == 270){
            currentRotation = 180;
        }
        return (currentRotation + sensorOrientation + 270) % 360;
    }

    private void updateRotation(float[] vectors) {
        int FROM_RADS_TO_DEGS = -57;
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float roll = orientation[2] * FROM_RADS_TO_DEGS;
        currentRotation = get90degreeIntervals(roll);
        setViewRotations(roll);
    }

    private int get90degreeIntervals(float roll) {
        if (roll < 45 && roll > -45){
            return 0;
        }else if (roll < 135 && roll > 45){
            return 90;
        }else if (roll > 135 || roll < -135){
            return 180;
        }else if (roll > -135 && roll < -45){
            return 270;
        }
        return 0;
    }

    private void setViewRotations(float roll){
        for (View rotatingView: rotatingViews) {
            if (rotatingView != null){
                rotatingView.setRotation(get90degreeIntervals(roll));
            }
        }
    }

    /**
     * Called when there is a new sensor event.
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // We only want events from the rotationSensor
        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                updateRotation(truncatedRotationVector);
            } else {
                updateRotation(event.values);
            }
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not used, but part of the implemented interface SensorEventListener
    }
}
