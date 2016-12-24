package app.discountcheo.com.discountcheo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckInActivity extends Activity {

    Context context;
    private static final String TAG = CheckInActivity.class.getSimpleName();
    private CallbackManager callbackManager;
    private LoginManager manager;
    Bundle extras;
    String product_id;
    String facebook_id;
    String images;
    String offers;
    String org_name;

    public static String regId;
    public static String pro_id;
    public static String image;
    public static String disounts;
    public static String organization_name;
    public static String fan_page;
    String last_discout = null;
    SharedPreferences preferences;
    LikeView likeView;
    TextView showOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        extras = getIntent().getExtras();
        pro_id = extras.getString("pro_id");
        facebook_id = extras.getString("regId");
        String Keyimages = extras.getString("ogImage");
        offers = extras.getString("discount_offers");
        org_name = extras.getString("organization_name");
        String Keyfan_pages = extras.getString("fan_page");

        images = "http://www.discountchao.com/" + Keyimages;
        fan_page = "https:" + Keyfan_pages;
        Log.d("fanPage", fan_page);


        showOffers = (TextView) findViewById(R.id.textView);

        showOffers.setText("Yes you got" + offers + "discount in " + org_name + "if you want like Fan Page");
        likeView = (LikeView) findViewById(R.id.like_view);


        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        likeView.setObjectIdAndType(fan_page, LikeView.ObjectType.PAGE);
        likeView.setForegroundColor(-256);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);

        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CheckInActivity.this,ThankYouActivity.class);
                intent.putExtra("p_id",pro_id);

                startActivity(intent);
            }
        });

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        manager = LoginManager.getInstance();


        manager.logInWithPublishPermissions(this, permissionNeeds);

        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                publishImage();
                offerInfo();


            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });
    }

    private void publishImage() {


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(images))
                .build();

        ShareApi.share(content, null);

    }




    public void offerInfo() {
        //sharedpreference
        preferences = getApplicationContext().getSharedPreferences("UserInfo",
                Context.MODE_WORLD_READABLE);

        regId = preferences.getString("id", "");

        // Instantiate the RequestQueue.

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.discountchao.com/c_main/single_user_fb_checkin_api";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Kuddus", response.toString());

                        pDialog.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "doesnot work ", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", regId);
                params.put("itemid", pro_id);


                return params;
            }

        };


// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);


    }
    @Override
    protected void onStop() {
        super.onStop();
        Intent intent=new Intent(CheckInActivity.this,MainActivity.class);
        startActivity(intent);
    }
}


