package app.discountcheo.com.discountcheo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by Faisal on 7/3/2015.
 */
public class SendUserInfo {

    Context context;
    private int refCode;

    public SendUserInfo(Context context) {
        this.context = context;
    }

    public void sendUserInfo () {

        SharedPreferences preferences = context.getSharedPreferences("UserInfo",
                Context.MODE_WORLD_READABLE);
        String regId = preferences.getString("id", null);
        String id = preferences.getString("fbId", null);
        String name = preferences.getString("name", null);
        String email = preferences.getString("email", null);
        String phone = preferences.getString("phone", null);
        String birthday = preferences.getString("birthday", null);
        String gender = preferences.getString("gender", null);

        Random r = new Random();
        refCode = r.nextInt(9000 - 6000) + 456;


        final int DEFAULT_TIMEOUT = 10000;
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setTimeout(DEFAULT_TIMEOUT);

        RequestParams params = new RequestParams();
        params.put("regId", regId);
        params.put("fbId", id);
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("birthday", birthday);
        params.put("refCode", String.valueOf(refCode));
        params.put("gender", gender);




        client.post("http://www.discountchao.com/c_main/single_user_information_api",
                params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = null;
                        try {
                            response = new String(responseBody, "UTF-8");
                            String data =response.toString();
                            Log.d("response",data);
                           // System.out.println("text set: " + text.getText().toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        sendUserInfo();
                    }
                });

    }



}