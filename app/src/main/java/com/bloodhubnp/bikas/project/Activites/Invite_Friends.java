package com.bloodhubnp.bikas.project.Activites;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bloodhubnp.bikas.project.Adapters.RecycleAdapter;
import com.bloodhubnp.bikas.project.DataModels.DataReference;
import com.bloodhubnp.bikas.project.Extras.Details;
import com.bloodhubnp.bikas.project.Networks.Mysingleton;
import com.bloodhubnp.bikas.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bikas on 7/8/2017.
 */

public class Invite_Friends extends Fragment {
    // Cursor Adapter for storing contacts data
    SimpleCursorAdapter adapter;
    // List View Widget
    ListView lvContacts;
    CircleImageView cir_img;
    private RecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;
    ArrayList<String> number = new ArrayList<String>();
    private List<DataReference> referencelayouts = new ArrayList<>();
    public ListView listview;
    public String ip = "http://192.168.10.6:81/BloodHub/";
    public String database_url = "http://192.168.10.6:81/BloodHub/sendNumber.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        check();
        return inflater.inflate(R.layout.contactlayout, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadcontact();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter = new RecycleAdapter(referencelayouts, getContext());
        recyclerView.setAdapter(recycleAdapter);
        cir_img = (CircleImageView) view.findViewById(R.id.image_view);


    }

    public class Android_Contact {
        public String android_contacts_name = "";
        public String android_contacts_number = "";


    }


    //SEARCHING CONTACT NUMBER FROM USER AND COMPARING WITH REMOTE DATABASE
    public void loadcontact() {
        StringBuilder builder = new StringBuilder();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Invite_Friends.Android_Contact android_contact = new Invite_Friends.Android_Contact();
                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                final String contact_name = cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                android_contact.android_contacts_name = contact_name;

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
                            , new String[]{contact_id}
                            , null);
                    while (phoneCursor.moveToNext()) {

                        final String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                     String name_person=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        final String name_person = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        final String image_uri = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, database_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String status = jsonObject.getString("status");
                                    int type;
                                    String content;
                                    if (status.equals("match")) {
                                        String name = jsonObject.getString("name");
                                        String image_link = jsonObject.getString("image_url");
                                        String image_path = ip + image_link;
                                        /*Toast.makeText(getApplicationContext(),image_path,Toast.LENGTH_LONG).show();*/
                                        Log.e("Image_Ko_URl", image_path);

                                        referencelayouts.add(new DataReference(name, phoneNumber, type = Details.FIRST_ROW, image_path));
                                        recycleAdapter.notifyDataSetChanged();

                                        /*Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();*/
                                    } else {
                                        referencelayouts.add(new DataReference(name_person, phoneNumber, type = Details.OTHER_ROW, ip));
                                        recycleAdapter.notifyDataSetChanged();


                                        String invite = jsonObject.getString("invite");
//                                        Toast.makeText(getApplicationContext(),invite+" "+name_person,Toast.LENGTH_LONG).show();
                                    }
//                                    referencelayouts.add(new DataReference(phoneNumber,name_person,type));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(getApplication(),"No Internet. Please Try Again",Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("phone", phoneNumber);
                                Log.e("PHONE", phoneNumber);
                                return params;
                            }
                        };
                        Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);

                    }
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
    }


}
