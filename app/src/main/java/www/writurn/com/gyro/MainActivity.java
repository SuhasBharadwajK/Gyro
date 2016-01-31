package www.writurn.com.gyro;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.Math.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor gyroscope, accelerometer, magnetometer;
    private TextView textView;
    private String quadUrl = "http://192.168.0.103:8000";
    private URL url;
    private HttpURLConnection urlConnection;

//    private static final float NS2S = 1.0f / 1000000000.0f;
//    private final float[] deltaRotationVector = new float[4];
//    private float timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView1);

//        try {
//            url = new URL(quadUrl);
//        }
//        catch(MalformedURLException e) {
//            //Do something
//            textView.setText(e.toString());
//            Log.d("myTag", "This is my message1");
//        }
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setReadTimeout(10000);
//            urlConnection.setConnectTimeout(15000);
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//        }
//        catch (IOException e) {
//            //Do something
//            textView.setText(e.toString());
//            Log.d("myTag", "This is my message2");
//        }



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //gyroscope= mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

//        PackageManager PM= this.getPackageManager();
//        boolean gyro = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
//        boolean light = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);

        initListeners();

//        if(gyro){
//
//            if(light){
//                Toast.makeText(getApplicationContext(),"Both light and gyroscope sensors are present", Toast.LENGTH_LONG).show();
//            }
//            else
//                Toast.makeText(getApplicationContext(),"Only gyroscope sensor is present", Toast.LENGTH_LONG).show();
//
//        }

    }

    public void initListeners()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

//    float[] inclineGravity = new float[3];
//    float[] mGravity;
//    float[] mGeomagnetic;
//    float orientation[] = new float[3];
//    float pitch;
//    float roll;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This time step's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        float axisX = event.values[0];
        float axisY = event.values[1];
        float axisZ = event.values[2];

        textView.setText("X: " + (int) axisX + "\nY: " + (int) axisY + "\nZ: " + (int) axisZ);

//        try {
//            url = new URL(quadUrl);
//        }
//        catch(MalformedURLException e) {
//            //Do something
//            textView.setText(e.toString());
//            Log.d("myTag", "This is my message1");
//        }
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setReadTimeout(10000);
//            urlConnection.setConnectTimeout(15000);
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//        }
//        catch (IOException e) {
//            //Do something
//            textView.setText(e.toString());
//            Log.d("myTag", "This is my message2");
//        }
//
//        ContentValues values=new ContentValues();
//        values.put("type", "motor");
//        values.put("num", "1");
//        values.put("operation", "incr");
//        String dataToSend = "{\"type\": \"motor\", \"num\": \"1\", \"operation\":\"incr\"}";
//
//        try {
//            OutputStream os = urlConnection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(dataToSend);
//            writer.flush();
//            writer.close();
//            os.close();
//
//        }
//        catch (IOException e) {
//            Log.d("Error: ", e.toString());
//            textView.setText(e.toString());
//
//        }

//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
//        {
//            mGravity = event.values;
//        }
//        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
//        {
//            mGeomagnetic = event.values;
//
//            if (isTiltDownward())
//            {
//                Log.d("test", "downwards");
//                tv.setText("Downwards");
//            }
//            else if (isTiltUpward())
//            {
//                Log.d("test", "upwards");
//                tv.setText("Upwards");
//            }
//        }
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://<ip address>:3000");

//        try {
//            //add data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//            nameValuePairs.add(new BasicNameValuePair("data", data[0]));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            //execute http post
//            HttpResponse response = httpclient.execute(httppost);
//
//        } catch (ClientProtocolException e) {
//
//        } catch (IOException e) {
//
//        }
    }

//    public boolean isTiltUpward()
//    {
//        if (mGravity != null && mGeomagnetic != null)
//        {
//            float R[] = new float[9];
//            float I[] = new float[9];
//
//            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//
//            if (success)
//            {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//
//                /*
//                * If the roll is positive, you're in reverse landscape (landscape right), and if the roll is negative you're in landscape (landscape left)
//                *
//                * Similarly, you can use the pitch to differentiate between portrait and reverse portrait.
//                * If the pitch is positive, you're in reverse portrait, and if the pitch is negative you're in portrait.
//                *
//                * orientation -> azimut, pitch and roll
//                *
//                *
//                */
//
//                pitch = orientation[1];
//                roll = orientation[2];
//
//                inclineGravity = mGravity.clone();
//
//                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);
//
//                // Normalize the accelerometer vector
//                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
//                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
//                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);
//
//                //Checks if device is flat on ground or not
//                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));
//
//                Float objPitch = new Float(pitch);
//                Float objZero = new Float(0.0);
//                Float objZeroPointTwo = new Float(0.2);
//                Float objZeroPointTwoNegative = new Float(-0.2);
//
//                int objPitchZeroResult = objPitch.compareTo(objZero);
//                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
//                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);
//
//                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 30 && inclination < 40))
//                {
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        }
//
//        return false;
//    }

//    public boolean isTiltDownward()
//    {
//        if (mGravity != null && mGeomagnetic != null)
//        {
//            float R[] = new float[9];
//            float I[] = new float[9];
//
//            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//
//            if (success)
//            {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//
//                pitch = orientation[1];
//                roll = orientation[2];
//
//                inclineGravity = mGravity.clone();
//
//                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);
//
//                // Normalize the accelerometer vector
//                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
//                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
//                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);
//
//                //Checks if device is flat on groud or not
//                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));
//
//                Float objPitch = new Float(pitch);
//                Float objZero = new Float(0.0);
//                Float objZeroPointTwo = new Float(0.2);
//                Float objZeroPointTwoNegative = new Float(-0.2);
//
//                int objPitchZeroResult = objPitch.compareTo(objZero);
//                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
//                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);
//
//                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 170))
//                {
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        }
//
//        return false;
//    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something if sensor accuracy changes.
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        initListeners();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // important to unregister the sensor when the activity pauses.
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onBackPressed()
    {
        mSensorManager.unregisterListener(this);
        super.onBackPressed();
    }

    @Override
    public void onDestroy()
    {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
