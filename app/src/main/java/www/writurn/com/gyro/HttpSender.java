package www.writurn.com.gyro;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by suhasb on 31/1/16.
 */
public class HttpSender extends AsyncTask<String, Void, String> {
    String str = "";
    @Override
    protected String doInBackground(String... params) {
        byte[] result = null;

        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("http://192.168.0.103:8000");
        HttpPost httppost = new HttpPost("http://169.254.151.12:8000");
        String dataToSend;// = "{\"type\": \"gyro\", \"command\": \"tilt\", \"operation\":\"incr\"}";

        if (params[0] == "tilt") {
            dataToSend = "{\"type\": \"gyro\", \"command\": \"tilt\", \"x\":" + params[1] + ", \"y\":" + params[2] + ", \"z\":" + params[3] + "}";
        }
        else if (params[0] == "throttle") {
            dataToSend = "{\"type\": \"throttle\", \"direction\": \"" + params[1] + "\", \"speed\":\"" + params[2] + "\"}";
        }
        else {
            dataToSend = "{\"type\": \"something\", \"command\": \"tilt\", \"operation\":\"incr\"}";
        }

        try {

            httppost.setEntity(new StringEntity(dataToSend));

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, "UTF-8");
                Log.d("response", str);
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return str;
    }

    /**
     * on getting result
     */
    @Override
    protected void onPostExecute(String result) {
        Log.d("Post result", this.str);
    }
}
