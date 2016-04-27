package www.writurn.com.gyro;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    ImageView sliderSpace;
    ImageView iv, iv1;
    RelativeLayout.LayoutParams params, params1;
    RelativeLayout rlMain;
    Switch wifiToggle;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    DecimalFormat df = new DecimalFormat("#.00");
    int x = 9, y = 9, z = 9;

    String networkSSID = "RasPi";
    String networkPass = "Raspberry!";

    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            url = new URL("http://169.254.151.12:8000");
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }

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

        sliderSpace = (ImageView) findViewById(R.id.slider);
        wifiToggle = (Switch) findViewById(R.id.gyroToggle);

        iv = new ImageView(this);
        iv.setImageResource(R.drawable.joyball);

        iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.slider);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initListeners();

        int[] coords = new int[2];
        sliderSpace.getLocationInWindow(coords);


        rlMain = (RelativeLayout) findViewById(R.id.mainLayout);
        params = new RelativeLayout.LayoutParams(80, 80);
        params1 = new RelativeLayout.LayoutParams(80, 80);

        float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1050, getResources().getDisplayMetrics());
        params.topMargin = 0;
        params.leftMargin = 100;

        params.width = (int) wt_px;
        params.height = (int) ht_px;
        rlMain.addView(iv1, params);

        float wt_px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        float ht_px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        params1.topMargin = 355;
        params1.leftMargin = 84;
        params1.width = (int) wt_px1;
        params1.height = (int) ht_px1;
        rlMain.addView(iv, params1);


        System.out.print(coords);

        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (event.getRawY() >= 200 && event.getRawY() <= 950) {
                            params1.topMargin = (int) event.getRawY() - 200;
                            iv.setLayoutParams(params1);


                            if (event.getRawY() <= 559) {
                                new Poster().execute("throttle", "up", "medium", "");
                            } else if (event.getRawY() >= 559) {
                                new Poster().execute("throttle", "down", "medium", "");
                            }

                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        params1.topMargin = 360;
                        iv.setLayoutParams(params1);
                        break;
                }
                return true;
            }
        });

        wifiToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    networkSSID = "suhas";
                    networkPass = "reebokpuma";
                    try {
                        url = new URL("http://192.168.0.103:8000");
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    networkSSID = "RasPi";
                    networkPass = "Raspberry!";
                    try {
                        url = new URL("http://169.254.151.12:8000");
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

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
            }
        });
    }


    public void initListeners()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float axisX = event.values[0];
        float axisY = event.values[1];
        float axisZ = event.values[2];
        int tempX, tempY;

        tempX = (int) axisX;
        tempY = (int) axisY;

        if ((tempX > 3 || tempX < -3) || (tempY > 2 || tempY < -2)) {
            if (x != tempX || y != tempY) {
                x = tempX;
                y = tempY;
                z = (int) axisZ;
                new Poster().execute("tilt", "" + x, "" + y, "" + z);
            }
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        initListeners();
        super.onResume();
    }

    @Override
    protected void onPause() {
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
        super.onStart();
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            int[] coords = new int[2];
            sliderSpace.getLocationInWindow(coords);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class Poster extends AsyncTask<String, Void, String> {

        final StringBuilder responseOutput = new StringBuilder();
        String responseMessage;


        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                String urlParameters = "{\"type\": \"" + params[0] + "\", \"direction\": \"" + params[1] + "\", \"speed\":\"" + params[2] + "\"}";
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                responseMessage = connection.getResponseMessage();
                DataInputStream diS = new DataInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return responseMessage;
        }

        protected void onPostExecute(String result) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }
}
