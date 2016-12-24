package app.discountcheo.com.discountcheo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;


public class MainActivity extends Activity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    SharedPreferences.Editor editor;
    EditText txtphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        txtphone= (EditText) findViewById(R.id.txtphone);

        if( txtphone.getText().toString().trim().equals(""))
        {
            txtphone.setError( "phone no is required!" );
            //You can Toast a message here that the Username is Empty
        }
        else
        {
            String phone = txtphone.getText().toString();
            editor.putString("phone", phone);
        }

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday,publish_actions"));

        editor = this.getSharedPreferences("UserInfo", Context.MODE_WORLD_READABLE).edit();


        isLoggedIn();

        if (isLoggedIn()) {


            loginButton.setVisibility(View.INVISIBLE);
           /* new CountDownTimer(1000, 2000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    finish();
                }

            }.start();*/
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        } else {

        }


        // Callback registration
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            if(object !=null){
                                            String fbId = (String) object.get("id");
                                            String name = (String) object.get("name");
                                            String email = (String) object.get("email");
                                            String birthday = (String) object.get("birthday");
                                            String gender = (String) object.get("gender");

                                            editor.putString("fbId", fbId);
                                            editor.putString("name", name);
                                            editor.putString("email", email);
                                            editor.putString("birthday", birthday);
                                            editor.putString("gender", gender);


                                            editor.commit();
                                            SendUserInfo sender = new SendUserInfo(getApplication());
                                            sender.sendUserInfo();
                                            }
                                            else{}

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "fbId,name, email, phone, birthday, gender");
                        request.setParameters(parameters);
                        request.executeAsync();
                        loginButton.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(intent);
                        //  finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplication(),
                                "this is errro message.",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    /*@Override
    protected void onStop() {
        super.onStop();
        MainActivity.this.finish();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        return accessToken != null;

    }



}