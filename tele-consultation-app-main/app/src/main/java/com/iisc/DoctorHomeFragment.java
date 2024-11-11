package com.iisc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iisc.consultation.Appointment_notif;
import com.iisc.consultation.Appointments_Adapter;
import com.iisc.consultation.Doctors_Session_Mangement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DoctorHomeFragment extends Fragment {
    private ArrayList<Appointment_notif> current_appt;
    private Appointments_Adapter adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        EditText search = view.findViewById(R.id.editTextSearch_appointment);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance("https://consultapp-2ba50-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Doctors_Appointments");
        RecyclerView rv = view.findViewById(R.id.recycler_available_appointments);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        String[] monthName = {"Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov",
                "Dec"};

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        String month = monthName[cal.get(Calendar.MONTH)];
        int year = cal.get(Calendar.YEAR);

        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = null;
        try {
            d1 = sdformat.parse(day + "-" + month + "-" + year);
        } catch (ParseException e) {
            Log.e("ERROR", "An error occurred while processing", e);
        }
        Date finalD = d1;
        Doctors_Session_Mangement doctors_session_mangement = new Doctors_Session_Mangement(requireContext());
        String email = doctors_session_mangement.getDoctorSession()[0].replace(".", ",");
        reference.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current_appt = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String value_2 = dataSnapshot.getKey();
                        value_2 = value_2.replace(" ", "-");
                        try {
                            Date d2 = sdformat.parse(value_2);
                            if (finalD.compareTo(d2) <= 0) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Appointment_notif appointment_notif = dataSnapshot1.getValue(Appointment_notif.class);
                                    if (appointment_notif.getAppointment_text().equals("1")) {
                                        current_appt.add(appointment_notif);
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            Log.e("ERROR", "An error occurred while processing", e);
                        }

                    }

                    adapter = new Appointments_Adapter(current_appt, getContext());
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void filter(String text) {
        ArrayList<Appointment_notif> filterdNames = new ArrayList<>();
        int counter = 0;
        for (Appointment_notif obj : this.current_appt) {
            counter = counter + 1;
            if (obj.getDate().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(obj);
            }
        }
        this.adapter.filterList(filterdNames);
    }
}