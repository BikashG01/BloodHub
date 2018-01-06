package com.bloodhubnp.bikas.project.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bloodhubnp.bikas.project.R;

/**
 * Created by bikas on 7/8/2017.
 */

public class Search_Blood extends Fragment {
    private String bloodvalue, u_name, u_phonenumber;
    private Spinner bloodspinner;
    private ArrayAdapter<CharSequence> adapterSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.blood_search_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bloodspinner = (Spinner) view.findViewById(R.id.spinner);
        adapterSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.bloodGroup1, R.layout.spinner_layout);
        adapterSpinner.setDropDownViewResource(R.layout.textview_background);
        bloodspinner.setAdapter(adapterSpinner);
        bloodspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodvalue = bloodspinner.getSelectedItem().toString();
                ((TextView) bloodspinner.getSelectedView()).setTextColor(getResources().getColor(R.color.background));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button search_button = (Button) view.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                String name = sharedPreferences.getString(MainActivity.Name, u_name);
                String phone = sharedPreferences.getString(MainActivity.Phone, u_phonenumber);
                Intent intent = new Intent(getContext(), Search_section.class);
                Bundle bundle = new Bundle();
                bundle.putString("blood_value", bloodvalue);
                bundle.putString("name", name);
                bundle.putString("phone_number", phone);
                Log.e("First_Name", name);
                Log.e("First_Number", phone);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }
}
