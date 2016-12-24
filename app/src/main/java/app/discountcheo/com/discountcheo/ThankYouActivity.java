package app.discountcheo.com.discountcheo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.share.widget.LikeView;

import java.util.HashMap;
import java.util.Map;


public class ThankYouActivity extends ActionBarActivity {
    SharedPreferences preferences;
    LikeView likeView;
    public static String regId;
    public static String pro_id;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        extras = getIntent().getExtras();
        pro_id = extras.getString("p_id");


        preferences = getApplicationContext().getSharedPreferences("UserInfo",
                Context.MODE_WORLD_READABLE);

        regId = preferences.getString("id", "");

        // Instantiate the RequestQueue.

        final ProgressDialog pDialog = new ProgressDialog(ThankYouActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(ThankYouActivity.this);
        String url = "http://www.discountchao.com/c_main/single_user_fb_like_api";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Test", response.toString());

                        pDialog.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "does not work ", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", regId);
                params.put("itemid", pro_id);


                return params;
            }

        };


// Add the request to the RequestQueue.

    }



    @Override
    protected void onStop() {
        super.onStop();
        Intent intent=new Intent(ThankYouActivity.this,MainActivity.class);
        startActivity(intent);
    }



}
