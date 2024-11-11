package com.iisc.consultation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iisc.PatientHomeFragment;
import com.iisc.R;
import com.google.android.material.navigation.NavigationView;
import com.iisc.databinding.ActivityPatientBinding;

import java.util.ArrayList;

public class Patient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toast backToast;
    private long backPressedTime;
    private DrawerLayout drawerLayout1;
    private ActivityPatientBinding patientBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientBinding = ActivityPatientBinding.inflate(getLayoutInflater());
        setContentView(patientBinding.getRoot());
        this.replaceFragment(new PatientHomeFragment());
        patientBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    this.replaceFragment(new PatientHomeFragment());
                    break;
                case R.id.appointments:
                    startActivity(new Intent(this, Patient_Appointments.class));
                    break;
                case R.id.chats:
                    startActivity(new Intent(this, Patient_Chat_Display.class));
                    break;
                case R.id.tutorials:
                    break;
            }
            return true;
        });
//
//        RecyclerView recyclerView_spec = (RecyclerView) findViewById(R.id.recycler_spec);
//        ImageView all_doctors = (ImageView) findViewById(R.id.imageView_doc);
//        SliderView sliderView = findViewById(R.id.slider);

        drawerLayout1 = findViewById(R.id.drawer_layout1);
        NavigationView navigationView1 = findViewById(R.id.nav_view1);
        Toolbar toolbar1 = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar1);

        navigationView1.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout1, toolbar1, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout1.addDrawerListener(toggle);
        toggle.syncState();
        navigationView1.setNavigationItemSelectedListener(Patient.this);
        navigationView1.setCheckedItem(R.id.nav_home);



        Integer [] specialisation_pic={R.drawable.infectious,R.drawable.dermavenereolepro,R.drawable.skin,R.drawable.diabetes,
                                        R.drawable.thyroid,R.drawable.hormone, R.drawable.immunology, R.drawable.rheuma, R.drawable.neuro, R.drawable.ophtha, R.drawable.cardiac, R.drawable.cancer,
                                        R.drawable.gastro, R.drawable.ent};

        String[] specialisation_type={"Infectious Disease","Dermatology & Venereology","Leprology","Endocrinology & Diabetes","Thyroid","Hormone","Immunology","Rheumatology","Neurology","Ophthalmology","Cardiac Sciences","Cancer Care / Oncology","Gastroenterology, Hepatology & Endoscopy","Ear Nose Throat"};

        ArrayList<Main_Specialisation> main_specialisations = new ArrayList<>();

        for(int i=0;i<specialisation_pic.length;i++){
           Main_Specialisation specialisation=new Main_Specialisation(specialisation_pic[i],specialisation_type[i]);
           main_specialisations.add(specialisation);

        }

        //design horizontal layout
        LinearLayoutManager layoutManager_spec=new LinearLayoutManager(
                Patient.this,LinearLayoutManager.HORIZONTAL,false);

//        recyclerView_spec.setLayoutManager(layoutManager_spec);
//        recyclerView_spec.setItemAnimator(new DefaultItemAnimator());
//
//        Specialist_Adapter specialist_adapter = new Specialist_Adapter(main_specialisations, Patient.this);
//        recyclerView_spec.setAdapter(specialist_adapter);
//
//        all_doctors.setOnClickListener(v -> {
//            Intent intent=new Intent(Patient.this,Available_Doctors.class);
//            intent.putExtra("flag",0+"");
//            startActivity(intent);
//        });

        Integer [] sliderDataArrayList={R.drawable.image1,R.drawable.image2,R.drawable.image3};

        ArrayList<Slider_Data> slider_data = new ArrayList<>();

        for (Integer integer : sliderDataArrayList) {

            Slider_Data slider_data_arr = new Slider_Data(integer);
            slider_data.add(slider_data_arr);
        }

        SliderAdapter adapter = new SliderAdapter(this, slider_data);

//        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
//
//        sliderView.setSliderAdapter(adapter);
//
//        sliderView.setScrollTimeInSec(3);
//
//        sliderView.setAutoCycle(true);
//
//        sliderView.startAutoCycle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch (menu.getItemId()){
            case R.id.search:
                break;
            case R.id.logout:
                Patient_Session_Management session_management=new Patient_Session_Management(Patient.this);
                session_management.removeSession();
                moveToMainpage();
                break;
        }
        return true;
    }

    private void moveToMainpage() {
        Intent intent=new Intent(Patient.this,AskRole.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout1.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout1.closeDrawer(GravityCompat.START);
        }
        else
        {
            if(backPressedTime+2000>System.currentTimeMillis())
            {
                finishAffinity();
                backToast.cancel();
                super.onBackPressed();
                return;
            }
            else
            {
                backToast = Toast.makeText(getBaseContext(),"Press Back again to exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, AskRole.class));
                break;
            case R.id.appointments:
                startActivity(new Intent(Patient.this, Patient_Appointments.class));
                break;
            case R.id.chats:
                startActivity(new Intent(Patient.this, Patient_Chat_Display.class));
                break;
            case R.id.analytics:
                startActivity(new Intent(Patient.this, DeviceAnalytics.class));
                break;
            case R.id.step:
                startActivity(new Intent(Patient.this, PhyziHealth.class));
                break;
            case R.id.settingsApp:
                Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent1.setData(Uri.parse("package:"+getApplicationContext().getPackageName()));
                startActivity(intent1);
                break;
            case R.id.logout:
                Patient_Session_Management session_management=new Patient_Session_Management(Patient.this);
                session_management.removeSession();
                moveToMainpage();
                break;
        }
        drawerLayout1.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_frame, fragment);
        fragmentTransaction.commit();
    }
}
