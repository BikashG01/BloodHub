package com.bloodhubnp.bikas.project.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodhubnp.bikas.project.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profileImage;
    ImageButton call, request, message;
    TextView profile_name, profile_location, profile_gender, profile_bloodGroup, profile_contact_number, profile_donation_date;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        call = (ImageButton) findViewById(R.id.call);
        request = (ImageButton) findViewById(R.id.add_to_circle);
        message = (ImageButton) findViewById(R.id.message);
        profileImage = (CircleImageView) findViewById(R.id.cv);
        profile_name = (TextView) findViewById(R.id.user_name);
        profile_location = (TextView) findViewById(R.id.user_location);
        profile_gender = (TextView) findViewById(R.id.genders);
        profile_bloodGroup = (TextView) findViewById(R.id.blood);
        profile_contact_number = (TextView) findViewById(R.id.contact_no);
        profile_donation_date = (TextView) findViewById(R.id.last_donation_dates);

        Bundle bundle = getIntent().getExtras();
        String image_url = bundle.getString("image_path");
        Picasso.with(this)
                .load(image_url)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(profileImage);

        profile_name.setText(bundle.getString("name"));
        profile_location.setText(bundle.getString("location"));
        profile_gender.setText(bundle.getString("gender"));
        profile_bloodGroup.setText(bundle.getString("bloodgroup"));
        profile_donation_date.setText(bundle.getString("donation_date"));
        profile_contact_number.setText(bundle.getString("phone_number"));
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message_AlertDialog();
            }
        });
    }

    public void Message_AlertDialog() {
        final String tel_number = profile_contact_number.getText().toString();
        builder = new AlertDialog.Builder(this);
        String lineseparator = System.getProperty("line.separator");
        builder.setTitle("Do your want to send SMS TO " + tel_number + " " + profile_name + " for Blood Donation." + lineseparator + "Standard SMS rates may apply");
        builder.setPositiveButton("SEND MESSAGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(tel_number, null, "Lets find blood donor.Join BloodHub for saving life.", null, null);
                Toast.makeText(ProfileActivity.this, "Sending", Toast.LENGTH_LONG).show();
            }


        });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void call() {
        final String tel_number = profile_contact_number.getText().toString();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Do your want to CALL for Blood Donation." + "." + "Standard CALL rates may apply");
        builder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tel_number));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {

                }
                startActivity(callIntent);
            }


        });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

}

