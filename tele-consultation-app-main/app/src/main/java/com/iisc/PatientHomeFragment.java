package com.iisc;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iisc.consultation.Available_Doctors;
import com.iisc.consultation.Main_Specialisation;
import com.iisc.consultation.View_All_Adapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHomeFragment extends Fragment {

    private RecyclerView recyclerView_spec_all;
    private ArrayList<Main_Specialisation> main_specialisations;
    private View_All_Adapter view_all_adapter;
    private TextView view_all;
    private EditText search_spec;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientHomeFragment newInstance(String param1, String param2) {
        PatientHomeFragment fragment = new PatientHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_home, container, false);

        search_spec = view.findViewById(R.id.editTextSearch_spec);

        recyclerView_spec_all = view.findViewById(R.id.recycler_view_all_doc);

        Integer[] specialisation_pic = {R.drawable.infectious, R.drawable.dermavenereolepro, R.drawable.skin, R.drawable.diabetes,
                R.drawable.thyroid, R.drawable.hormone, R.drawable.immunology, R.drawable.rheuma, R.drawable.neuro, R.drawable.ophtha, R.drawable.cardiac, R.drawable.cancer,
                R.drawable.gastro, R.drawable.ent};

        String[] specialisation_type = {"Infectious Disease", "Dermatology & Venereology", "Leprology", "Endocrinology & Diabetes", "Thyroid", "Hormone", "Immunology", "Rheumatology", "Neurology", "Ophthalmology", "Cardiac Sciences", "Cancer Care / Oncology", "Gastroenterology, Hepatology & Endoscopy", "Ear Nose Throat"};

        main_specialisations = new ArrayList<>();

        for (int i = 0; i < specialisation_pic.length; i++) {
            Main_Specialisation specialisation = new Main_Specialisation(specialisation_pic[i], specialisation_type[i]);
            main_specialisations.add(specialisation);

        }

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_spec_all.setLayoutManager(staggeredGridLayoutManager);
        recyclerView_spec_all.setItemAnimator(new DefaultItemAnimator());


        view_all_adapter = new View_All_Adapter(main_specialisations);
        recyclerView_spec_all.setAdapter(view_all_adapter);

        search_spec.addTextChangedListener(new TextWatcher() {
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

        ImageView all_doctors = view.findViewById(R.id.imageView_doc);
        all_doctors.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), Available_Doctors.class);
            intent.putExtra("flag",0+"");
            startActivity(intent);
        });

        return view;
    }

    private void filter(String text) {

        ArrayList<Main_Specialisation> filterdNames = new ArrayList<>();
        for (Main_Specialisation doc_data : main_specialisations) {
            //if the existing elements contains the search input
            if (doc_data.getSpecialisation_type().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(doc_data);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        view_all_adapter.filterList(filterdNames);
    }
}