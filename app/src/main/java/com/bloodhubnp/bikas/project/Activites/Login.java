package com.bloodhubnp.bikas.project.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bloodhubnp.bikas.project.Networks.Mysingleton;
import com.bloodhubnp.bikas.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    public static final String SHAREDPREP = "PREP";
    public static final String bac_email = "dummy_email";
    private ProgressDialog progressDialog;
    public static final String bac_password = "dummy_password";
    public String ip = "http://192.168.10.6/BloodHub/";
    SharedPreferences sharedPreferences;
    final Context context = this;
    TextView register, forgetPassword;
    EditText email, password;
    Button btn_login;
    String user_email, user_password;
    AlertDialog.Builder builder;
    String login_url = "http://192.168.10.6:81/BloodHub/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = (TextView) findViewById(R.id.register);
        forgetPassword = (TextView) findViewById(R.id.login_forget);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        btn_login = (Button) findViewById(R.id.login_button);
        sharedPreferences = getSharedPreferences(SHAREDPREP, Context.MODE_PRIVATE);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.send_email_layout, null);
                AlertDialog.Builder alertdialogInput = new AlertDialog.Builder(context);
                alertdialogInput.setView(view);

                final EditText editText = (EditText) view.findViewById(R.id.email_send);
                alertdialogInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String[] TO = {"bikashgurung96@gmail.com"};
                                String CC = editText.getText().toString();
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("text/plain");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Password Request");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Password");
                                startActivity(emailIntent);
//                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                finish();


                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertdialogInput.create();
                alertDialogAndroid.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();


                //hide keyboard
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                user_email = email.getText().toString();
                user_password = password.getText().toString();

                if (user_email.equals("") || user_password.equals("")) {
                    email.setError("Enter your valid email");
                    password.setError("Enter your password");
//                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                } else {
                    try {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("login failed")) {

                                        Toast.makeText(getApplicationContext(), "Email or password is wrong.", Toast.LENGTH_LONG).show();

                                        progressDialog.dismiss();

                                    } else {
                                        progressDialog.dismiss();
                                        String image_link = jsonObject.getString("image_url");
                                        String image_path = ip + image_link;

                                        try {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("user_name", jsonObject.getString("name"));
                                            bundle.putString("user_image", image_path);
                                            bundle.putString("user_bloodgroup", jsonObject.getString("bloodgroup"));
                                            bundle.putString("phone_number", jsonObject.getString("fullnumber"));
                                            intent.putExtras(bundle);
                                            finish();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slidup, R.anim.slide_down);
                                        } catch (JSONException e) {
                                            Log.e("CRASH", e.getMessage());
                                        }
                                    }
                                } catch (JSONException e) {
                                    Log.e("No Internet Connection!", e.getMessage());
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "No internet! Please check your Internet Connection.", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("user_email", user_email);
                                params.put("user_password", user_password);
                                return params;
                            }
                        };
                        Mysingleton.getInstance(Login.this).addToRequestque(stringRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("BIKASH", e.getMessage());
                    }
                }
            }


        });
    }
}




