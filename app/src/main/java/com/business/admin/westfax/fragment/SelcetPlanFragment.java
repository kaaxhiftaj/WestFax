package com.business.admin.westfax.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.business.admin.westfax.R;

public class SelcetPlanFragment extends Fragment implements  View.OnKeyListener{
    Button btnsecplan, btnfirplan;
    String ufname, ulname, umail, uphn, upass, namee, value, areacode, uname,faxnum,areaname,strorifaxtoll;

    public SelcetPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_selcet_plan, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Your Plan");

        Bundle bundle = getArguments();

        namee = bundle.getString("Name");
        value = bundle.getString("Value");
        areacode = bundle.getString("Areacode");
        ufname = bundle.getString("fname");
        ulname = bundle.getString("lname");
        umail = bundle.getString("email");
        upass = bundle.getString("pass");
        uphn = bundle.getString("phone");
        uname = bundle.getString("uname");
        faxnum = bundle.getString("Faxnum");
        areaname=bundle.getString("AreaName");
        strorifaxtoll=bundle.getString("OrigFaxnum");


        btnsecplan = (Button) rootview.findViewById(R.id.btnsecplan);
        btnfirplan = (Button) rootview.findViewById(R.id.btnfirplan);


        btnfirplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new PaymentMethod();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainfrm, fragment)
                        .commit();

                Bundle bundle = new Bundle();

                bundle.putString("Name", namee);
                bundle.putString("Value", value);
                bundle.putString("Areacode", areacode);
                bundle.putString("fname", ufname);
                bundle.putString("lname", ulname);
                bundle.putString("email", umail);
                bundle.putString("pass", upass);
                bundle.putString("phone", uphn);
                bundle.putString("uname", uname);
                bundle.putString("type", "freetrialdays");
                bundle.putString("days", "60");
                bundle.putString("Faxnum",faxnum);
                bundle.putString("AreaName",areaname);
                bundle.putString("OrigFaxnum", strorifaxtoll);

                fragment.setArguments(bundle);
            }
        });
        btnsecplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new PaymentMethod();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainfrm, fragment)
                        .commit();
                Bundle bundle = new Bundle();

                bundle.putString("Name", namee);
                bundle.putString("Value", value);
                bundle.putString("Areacode", areacode);
                bundle.putString("fname", ufname);
                bundle.putString("lname", ulname);
                bundle.putString("email", umail);
                bundle.putString("pass", upass);
                bundle.putString("phone", uphn);
                bundle.putString("uname", uname);
                bundle.putString("type", "freetrialdays");
                bundle.putString("days", "5");
                bundle.putString("Faxnum",faxnum );
                bundle.putString("AreaName",areaname);
                bundle.putString("OrigFaxnum", strorifaxtoll);

                fragment.setArguments(bundle);
            }
        });
        return rootview;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Fragment fragment = new SignupFragment();

                FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();


                if (fragment != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainfrm, fragment)
                            .commit();
                }

                return true;
            }
        }

        return false;
    }

}
