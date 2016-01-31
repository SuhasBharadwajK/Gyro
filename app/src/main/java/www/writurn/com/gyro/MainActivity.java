package www.writurn.com.gyro;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor gyroscope, accelerometer, magnetometer;
    private TextView textView;
    private String quadUrl = "http://192.168.0.103:8000";
    private URL url;
    private HttpURLConnection urlConnection;
    DecimalFormat df = new DecimalFormat("#.00");

//    private static final float NS2S = 1.0f / 1000000000.0f;
//    private final float[] deltaRotationVector = new float[4];
//    private float timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView1);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

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
        //textView.setText("X: " + df.format(axisX) + "\nY: " + df.format(axisY) + "\nZ: " + df.format(axisZ));
        (new SendTilt()).execute("" + (int) axisX, "" + (int) axisY, "" + (int) axisZ);
    }

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
