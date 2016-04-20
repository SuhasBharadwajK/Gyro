package www.writurn.com.gyro;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import www.writurn.com.gyro.R;

public class MainActivity extends Activity implements SensorEventListener {

    ImageView ballSlider, sliderSpace;
    TextView logger;
    float originalSliderX, originalSliderY;
    ImageView iv, iv1;
    RelativeLayout.LayoutParams params, params1;
    RelativeLayout rlMain;
    Switch gyroToggle;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private TextView textView;
    DecimalFormat df = new DecimalFormat("#.00");
    int x = 9, y = 9, z = 9;

    String networkSSID = "RasPi";
    String networkPass = "Raspberry!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.preSharedKey = "\""+ networkPass +"\"";
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }


        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        ballSlider = (ImageView) findViewById(R.id.ballButton);
        sliderSpace = (ImageView) findViewById(R.id.slider);
        //logger = (TextView) findViewById(R.id.logger);
        //textView = (TextView)findViewById(R.id.textView1);
        //logger.setText(Float.toString(sliderSpace.getTop()) + Float.toString(sliderSpace.getLeft()));

        iv = new ImageView(this);
        iv.setImageResource(R.drawable.joyball);

        iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.slider);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initListeners();
//        iv.setMaxWidth(80);
//        iv.setMinimumWidth(80);

        int[] coords = new int[2];
        sliderSpace.getLocationInWindow(coords);
//        ballSlider.setX(coords[0]);
//        ballSlider.setY(coords[1]);
//        logger.setText(coords[1] + ", " + coords);


        rlMain = (RelativeLayout) findViewById(R.id.mainLayout);
        params = new RelativeLayout.LayoutParams(80, 80);
        params1 = new RelativeLayout.LayoutParams(80, 80);

        float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1050, getResources().getDisplayMetrics());
        params.topMargin = 0;
        params.leftMargin = 100;
//        ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
//        wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1050, getResources().getDisplayMetrics());

        params.width = (int) wt_px;
        params.height = (int) ht_px;
        rlMain.addView(iv1, params);

        float wt_px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        float ht_px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        params1.topMargin = 355;
        params1.leftMargin = 84;
        params1.width = (int) wt_px1;
        params1.height = (int) ht_px1;
        //params1.topMargin = 550;
        rlMain.addView(iv, params1);
//        iv.getLayoutParams().height = 1200;
//        iv.setLayoutParams(params1);



//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)ballSlider.getLayoutParams();
//        lp.leftMargin = 0;
//        lp.rightMargin = 0;
//        ballSlider.setLayoutParams(lp);

        System.out.print(coords);

        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String logg;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        logg = Float.toString(event.getRawY());
                        if (event.getRawY() >= 200 && event.getRawY() <= 950) {
                            params1.topMargin = (int) event.getRawY() - 200;
                            iv.setLayoutParams(params1);

                            if (event.getRawY() <= 559) {
                                (new HttpSender()).execute("throttle", "up", "medium", "");
                            }
                            else if (event.getRawY() >= 559) {
                                (new HttpSender()).execute("throttle", "down", "medium", "");
                            }

                        }
                        //logger.setText(logg + " Moving");
                        //logger.setText(Float.toString(ballSlider.getTop()) +
                        break;

//                    case MotionEvent.ACTION_DOWN:
//                        logg = Float.toString(event.getX());
//                        logger.setText(logg + " Down");
//                        //logger.setText(Float.toString(ballSlider.getTop()) + Float.toString(ballSlider.getLeft()));

                    case MotionEvent.ACTION_UP:
                        logg = Float.toString(event.getRawY());
                        //logger.setText(logg + " Up");
                        params1.topMargin = 360;
                        iv.setLayoutParams(params1);
                        //logger.setText(Float.toString(ballSlider.getTop()) + Float.toString(ballSlider.getLeft()));
                        break;
                }
                return true;
            }
        });
    }


    public void initListeners()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // This time step's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        float axisX = event.values[0];
        float axisY = event.values[1];
        float axisZ = event.values[2];
        int tempX, tempY;

        tempX = (int) axisX;
        tempY = (int) axisY;

        //textView.setText("X: " + tempX + "\nY: " + tempY + "\nZ: " + z);

        if ((tempX > 3 || tempX < -3) || (tempY > 2 || tempY < -2)) {
            if (x != tempX || y != tempY) {
                x = tempX;
                y = tempY;
                z = (int) axisZ;
                (new HttpSender()).execute("tilt", "" + x, "" + y, "" + z);
            }
        }

        //textView.setText("X: " + (int) axisX + "\nY: " + (int) axisY + "\nZ: " + (int) axisZ);
        //textView.setText("X: " + df.format(axisX) + "\nY: " + df.format(axisY) + "\nZ: " + df.format(axisZ));

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
    protected void onStart() {
        int[] coords = new int[2];
        sliderSpace.getLocationInWindow(coords);
        //logger.setText(Float.toString() + ", " + Float.toString(sliderSpace.getLeft()));
        //logger.setText(coords[0] + ", " + coords [1] + "CCC");





        super.onStart();
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            int[] coords = new int[2];
            sliderSpace.getLocationInWindow(coords);
            //logger.setText(coords[0] + ", " + coords [1]);
        }
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
