package com.bloodhubnp.bikas.project.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bloodhubnp.bikas.project.Networks.Mysingleton;
import com.bloodhubnp.bikas.project.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private DatePickerDialog.OnDateSetListener onDateSetListener, onDonationdate;
    private AlertDialog.Builder builder;
    private CircleImageView profileImage;
    private final int img_request = 1;
    private String bloodname = "Blood Group";
    private String gendername = "Gender";
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private String myLat;
    private String myLong;
    private String server_url = "http://192.168.10.6/BloodHub/signup_withcheck.php";
    private EditText dob, donation_date, userName, contactNumber, email, password, confirmPassword, address;
    private String name, userEmail, userPass, userConfigpass, userContact, userDob, userAddress, gender, bloodGroup, country_name, last_donation_date;
    private Button signUp;
    String full_phone_number, phone_number, withoutplus_number, genuine_number;
    private CountryCodePicker cpp;
    private ArrayAdapter<CharSequence> adapater, adapater2;
    Spinner bloodGroupSpin, genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialization
        setContentView(com.bloodhubnp.bikas.project.R.layout.activity_sign_up);

        cpp = (CountryCodePicker) findViewById(R.id.ccp);
        signUp = (Button) findViewById(R.id.sign_up);
        userName = (EditText) findViewById(R.id.masked);
        contactNumber = (EditText) findViewById(R.id.Phone_Number);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.con_password);
        address = (EditText) findViewById(R.id.address);
        builder = new AlertDialog.Builder(SignUp.this);


        //image view

        profileImage = (CircleImageView) findViewById(R.id.ProfileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_Image();

            }
        });
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1 * 1000);

        locationRequest.setFastestInterval(1 * 1000);

        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        dob = (EditText) findViewById(R.id.dob);

        donation_date = (EditText) findViewById(R.id.donation_date);


        donation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        SignUp.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDonationdate,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        SignUp.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();


            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = +month + "/" + day + "/" + year;
                dob.setText(date);

            }

        };

        onDonationdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = +month + "/" + day + "/" + year;
                donation_date.setText(date);
            }
        };


        //Gender Spinner
        genderSpinner = (Spinner) findViewById(R.id.Gender);
        adapater2 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapater2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapater2);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genderSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Blood Group Spinner
        bloodGroupSpin = (Spinner) findViewById(R.id.blood_group);
        adapater = ArrayAdapter.createFromResource(this, R.array.bloodGroup, android.R.layout.simple_spinner_item);
        adapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpin.setAdapter(adapater);
        bloodGroupSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = bloodGroupSpin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SIGNUP BUTTON CLICK EVENT
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                register();

            }
        });
    }

    public void LoginMove() {
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        super.onBackPressed();
    }

    public void register() {

        intialize();
        if (!validation()) {
            Toast.makeText(SignUp.this, "Sign Up Failed !", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

        } else {
            uploadImage();

        }
    }

    public void signUpSuccess() {
        //Data push to database when success of validation...
//        Toast.makeText(SignUp.this,"Sign Up Successful",Toast.LENGTH_LONG).show();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Successful.");
        builder.setMessage("Please Login to your account.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginMove();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void alertMatched() {
        //Data push to database when success of validation...
//        Toast.makeText(SignUp.this,"Sign Up Successful",Toast.LENGTH_LONG).show();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Signup Failed");
        builder.setMessage("Email or Phone number already exists." + "\n" + "Please Login or use different email or phone number.");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginMove();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public boolean validation() {
        boolean valid = true;
        if (name.isEmpty() || name.length() > 32) {
            userName.setError("Please Enter the valid name");
//            layoutName.setError("Valid Input Required");
            valid = false;

        } else if (gender.equals(gendername)) {
            builder.setMessage("Please select your Gender");
            builder.setTitle("Please Try Again");
            builder.setPositiveButton("OK", null);
            builder.create().show();
            valid = false;

        } else if (bloodGroup.equals(bloodname)) {
            builder.setMessage("Please select your BloodGroup");
            builder.setTitle("Please Try Again");
            builder.setPositiveButton("OK", null);
            builder.create().show();
            valid = false;

        } else if (userContact.isEmpty() || userContact.length() < 5 || userContact.length() > 10) {
            contactNumber.setError("Please enter the valid contact number");
//            layoutPhoneNumber.setError("Valid phone number is required");
            valid = false;
        } else if (userAddress.isEmpty()) {
            address.setError("Please enter the valid address");
//            layoutAddress.setError("Valid address Required");
            valid = false;

        } else if (userDob.isEmpty()) {
            dob.setError("Please enter the valid date");
//            layoutDob.setError("Valid date is required");
            valid = false;

        } else if (userEmail.isEmpty()) {
            email.setError("Please enter the valid email address");
//            layoutEmail.setError("Valid Input Required");
            valid = false;
        } else if (userPass.isEmpty()) {
            password.setError("Please enter the valid password ");
//            layoutPassword.setError("Valid Input Required");
            valid = false;

        } else if (userConfigpass.isEmpty()) {
            confirmPassword.setError("Re-enter the password");
//            layoutConfigPass.setError("Valid Input Required");
            valid = false;

        } else if (!userConfigpass.equals(userPass)) {
            confirmPassword.setError("Password Not matched");
//            layoutConfigPass.setError("Password Not matched");
            valid = false;
        }
        return valid;
    }

    public void intialize() {
        name = userName.getText().toString().trim();
        userEmail = email.getText().toString().trim();
        userPass = password.getText().toString().trim();
        userConfigpass = confirmPassword.getText().toString().trim();
        userContact = contactNumber.getText().toString().trim();
        userDob = dob.getText().toString().trim();
        userAddress = address.getText().toString().trim();
        phone_number = cpp.getFullNumberWithPlus().toString();
        withoutplus_number = cpp.getFullNumber().toString();
        country_name = cpp.getSelectedCountryName();
        full_phone_number = phone_number + "-" + userContact;
        genuine_number = withoutplus_number + userContact;
//        Toast.makeText(getApplicationContext(),genuine_number,Toast.LENGTH_LONG).show();
    }

    public void selected_Image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, img_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == img_request && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        //String ff = FirebaseInstanceId.getInstance().getToken();
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
        Log.e("Ehi ho token", token);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("found");

                    if (code.equals("found")) {
//                        Toast.makeText(getApplicationContext(),"Email or Phone Number is already registered in BloodHub.",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        alertMatched();
                    } else {


                        profileImage.setImageResource(0);
                        signUpSuccess();
                        progressDialog.dismiss();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SignUp.this, "No internet.Try again.", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Name", userName.getText().toString());
                params.put("Address", address.getText().toString());
                params.put("Email", email.getText().toString());
                params.put("Password", password.getText().toString());
                params.put("DOB", dob.getText().toString());
                params.put("LBDD", donation_date.getText().toString());
                params.put("Gender", gender);
                params.put("country_name", country_name);
                params.put("BloodGroup", bloodGroup);
                params.put("image", ImageToString(bitmap));
                params.put("Contact_Number", full_phone_number);
                params.put("Full_Number", genuine_number);
                params.put("Token", token);
                params.put("Lat", myLat);
                params.put("Long", myLong);
                return params;
            }
        };
        Mysingleton.getInstance(SignUp.this).addToRequestque(stringRequest);

    }

    public String ImageToString(Bitmap bitmap) {
        byte[] imgBytes = new byte[0];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            imgBytes = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {

        }
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    @Override
    public void onLocationChanged(Location location) {
        myLat = String.valueOf(location.getLatitude());
        myLong = String.valueOf(location.getLongitude());
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationProvider.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        googleApiClient.disconnect();

    }


}
