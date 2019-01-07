package com.firebaseapp.notification.fcmserver;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity {
    EditText txtTitle, txtBody;
    Button btnSend;

    String msgBody, msgTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBody = (EditText) findViewById(R.id.body);
        txtTitle = (EditText) findViewById(R.id.txtTitle);

        //msgBody = txtBody.getText().toString();
        //msgTitle = txtTitle.getText().toString();

        btnSend = (Button) findViewById(R.id.buttonSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {
        final ProgressDialog loading = ProgressDialog.show(this,"Mengirim...","Mohon Tunggu...",false,false);
        JSONObject json = new JSONObject();

        try {


            JSONObject notif = new JSONObject();
            notif.put("body", txtBody.getText().toString());
            notif.put("title", txtTitle.getText().toString());

            json.put("to", "/topics/weather");
            json.put("notification", notif);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("failure")){
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "Gagal", Toast.LENGTH_LONG).show();
                        } else {
                            loading.dismiss();
                            try {
                                Toast.makeText(MainActivity.this, "Berhasil mengirim notifikasi " + response.getString("message_id"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                loading.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAAk8w3cQ4:APA91bFYd0xr1O__i1JQJIPGFPR_88ekl-D2Gn5ZuouamhC53g9qFvVqjwmMJOtyZoJzNmY2iMFct4LfxeOcs0ASERlTfZ8Kt717KgUCuLLlx6RRyKRAEGVD2lX5PENaK_SeDYaz5a6h");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this.getApplicationContext());

        requestQueue.add(jsonObjReq);
    }

}
