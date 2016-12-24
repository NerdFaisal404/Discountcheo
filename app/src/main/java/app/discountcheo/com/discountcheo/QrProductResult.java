package app.discountcheo.com.discountcheo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class QrProductResult extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    Button scan_btn;
    TextView qrText1, qrText2;
    Context context;
    public static String regId;
    public static String pro_id;
    public static String image;
    public static String disounts;
    public static String organization_name;
    public static String fan_page;
    String last_discout = null;
    String id;
    String name;
    String email;
    String birthday;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_product_result);

        scan_btn = (Button) findViewById(R.id.scanBtn);
        qrText1 = (TextView) findViewById(R.id.txtQr1);
        qrText2 = (TextView) findViewById(R.id.txtQr2);
        //scan code


        //IntentIntegrator.initiateScan(this);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        // intent.putExtra("PROMPT_MESSAGE", "Please Wait ....");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                // intent.putExtra("PROMPT_MESSAGE", "Please Wait ....");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                //  Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                // toast.show();
                String[] datas = contents.split("#");
                String product_name = datas[0];
                pro_id = datas[1];
                //double cost = Double.valueOf(datas[2]);
                qrText1.setText(product_name);
                qrText2.setText("Item ID" + pro_id);

                //sharedpreference
                preferences = getApplicationContext().getSharedPreferences("UserInfo",
                        Context.MODE_WORLD_READABLE);

                regId = preferences.getString("id", "");

                // Instantiate the RequestQueue.

                final ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading...");
                pDialog.show();
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://www.discountchao.com/c_main/single_client_information_api";


                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("Faisal", response.toString());
                                // mTextView.setText("message"+ response);;
                                String[] datas = response.split(",");

                                String discount = datas[10];
                                String[] discount_percent = discount.split(":");
                                String last_discout = discount_percent[1];
                                disounts = last_discout.replace('"', ' ');

                                String name_br = datas[1];
                                String[] name_splt = name_br.split(":");
                                String last_name = name_splt[1];
                                organization_name = last_name.replace('"', ' ');
                                Log.d("Try", organization_name);

                                String fan = datas[11];
                                String[] fanSlp = fan.split(":");
                                String fanlast = fanSlp[2];
                                String q = fanlast.replaceAll("^\"|\"$", "");
                                fan_page = q.replaceAll("\\\\(.)", "\\/");
                                Log.d("Try", fan_page);

                                String ogImg = datas[12];
                                String[] gSplt = ogImg.split(":");
                                String ogLast = gSplt[1];
                                String b = ogLast.replaceAll("^\"|\"$", "");
                                image = b.replaceAll("\\\\(.)", "\\/");

                                Log.d("Try", image);

                                pDialog.dismiss();

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(QrProductResult.this);
                                alertDialog.setTitle("Offer...");
                                alertDialog.setMessage("If you want to get " + disounts + "Discount please CHECK IN");
                                alertDialog.setIcon(R.drawable.abc_ic_commit_search_api_mtrl_alpha);

                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("CHECK IN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User pressed YES button. Write Logic Here
                                        Intent intent = new Intent(QrProductResult.this, CheckInActivity.class);
                                        intent.putExtra("pro_id", pro_id);
                                        intent.putExtra("ogImage", image);
                                        intent.putExtra("userId", regId);
                                        intent.putExtra("discount_offers", disounts);
                                        intent.putExtra("organization_name", organization_name);
                                        intent.putExtra("fan_page", fan_page);
                                        startActivity(intent);
                                    }
                                });

                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                        // intent.putExtra("PROMPT_MESSAGE", "Please Wait ....");
                                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                                        startActivityForResult(intent, 0);

                                    }
                                });


                                // Showing Alert Message
                                alertDialog.show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "doesnot work ", Toast.LENGTH_SHORT).show();
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
                queue.add(stringRequest);

            }
            else {
                Toast.makeText(getApplicationContext(),"QR Code not valid",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"QR Code not valid",Toast.LENGTH_SHORT).show();
        }

    }


}
