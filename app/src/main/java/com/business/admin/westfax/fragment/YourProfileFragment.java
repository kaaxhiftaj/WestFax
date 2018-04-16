package com.business.admin.westfax.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.LoginWestfax;
import com.business.admin.westfax.MainActivity;
import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.R;
import com.business.admin.westfax.adapter.TollNumAdapter;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResultsetForLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YourProfileFragment extends Fragment {
    EditText crmail, crfname, crlnam, crphn, cruname, crpass, crconfrm;
    String value, namee, areacode, faxnum, areaname, strorifaxtoll;
    Button btnyrnext;
    RadioGroup rdgrpgend;
    RadioButton rdmal, rdfeml, rdothr;
    TextView strusrvalid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_your_profile, container, false);
        btnyrnext = (Button) rootview.findViewById(R.id.btnyrnext);
        crmail = (EditText) rootview.findViewById(R.id.crmail);
        crfname = (EditText) rootview.findViewById(R.id.crfname);
        crlnam = (EditText) rootview.findViewById(R.id.crlnam);
        crphn = (EditText) rootview.findViewById(R.id.crphn);
        cruname = (EditText) rootview.findViewById(R.id.cruname);
        crpass = (EditText) rootview.findViewById(R.id.crpass);
        crconfrm = (EditText) rootview.findViewById(R.id.crconfrm);
        rdgrpgend = (RadioGroup) rootview.findViewById(R.id.rdgrpgend);
        rdmal = (RadioButton) rootview.findViewById(R.id.rdmal);
        rdfeml = (RadioButton) rootview.findViewById(R.id.rdfeml);
        rdothr = (RadioButton) rootview.findViewById(R.id.rdothr);
        strusrvalid = (TextView) rootview.findViewById(R.id.strusrvalid);


//        cruname.addTextChangedListener(new TextWatcher() {
//
//                                           @Override
//                                           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                                           }
//
//                                           @Override
//                                           public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                               // TODO Auto-generated method stub
//                                               if (cruname.getText().toString().length() > 0) {
//                                                   strusrvalid.setVisibility(View.VISIBLE);
//                                                   userValidation();
//                                               }
//
//                                           }
//
//                                           @Override
//                                           public void afterTextChanged(Editable editable) {
//
//                                           }
//                                       });
//        rdgrpgend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                // TODO Auto-generated method stub
//
//                if (checkedId == R.id.rdmal) {
//                    grptype = "Mr";
//
//                } else if (checkedId == R.id.rdfeml) {
//
//                    grptype = "no";
//                }
//            }
//
//        });

        Bundle bundle = getArguments();
        namee = bundle.getString("Name");
        value = bundle.getString("Value");
        areacode = bundle.getString("Areacode");
        faxnum = bundle.getString("Faxnum");
        areaname = bundle.getString("AreaName");
        strorifaxtoll = bundle.getString("OrigFaxnum");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile Info");


        btnyrnext.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                String mainpass = crpass.getText().toString();
                String confpass = crconfrm.getText().toString();
                String strfnam = crfname.getText().toString();
                String strlnam = crlnam.getText().toString();
                String strmail = crmail.getText().toString();
                String strphn = crphn.getText().toString();
                String struname = cruname.getText().toString();




                if (strfnam.equals("") ||  strfnam.length() < 3 || strfnam.length() > 10 || strfnam.contains("@") ||
                        strfnam.contains("~") || strfnam.contains("`") || strfnam.contains("#")  ||
                        strfnam.contains("$")  || strfnam.contains("%")  || strfnam.contains("^") ||
                        strfnam.contains("&")  || strfnam.contains("*") || strfnam.contains("(")||
                        strfnam.contains(")")  || strfnam.contains("-") || strfnam.contains("+") ||
                        strfnam.contains("{")  || strfnam.contains("}")  || strfnam.contains("[") ||
                        strfnam.contains("]")  || strfnam.contains("|")
                        ) {
                    crfname.setError("Enter a valid frist Name");
                }
                else if (strlnam.equals("") || strlnam.length() < 3 || strlnam.length() > 10|| strfnam.contains("@") ||
                        strfnam.contains("~") || strfnam.contains("`") || strfnam.contains("#")  ||
                        strfnam.contains("$")  || strfnam.contains("%")  || strfnam.contains("^") ||
                        strfnam.contains("&")  || strfnam.contains("*") || strfnam.contains("(")||
                        strfnam.contains(")")  || strfnam.contains("-") || strfnam.contains("+") ||
                        strfnam.contains("{")  || strfnam.contains("}")  || strfnam.contains("[") ||
                        strfnam.contains("]")  || strfnam.contains("|")
                        ){
                    crlnam.setError("Enter a valid Last Name");
                }
                else if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strmail).matches())) {
                    crmail.setError("Please enter valid Email Id");
                }
                else if (strphn.equals("") || strphn.length() != 10) {
                    crphn.setError("Enter a valid Phone Number");
                }
                else if (struname.equals("") || struname.length() < 6 || struname.length() > 10  || strfnam.contains("@") ||
                        strfnam.contains("~") || strfnam.contains("`") || strfnam.contains("#")  ||
                        strfnam.contains("$")  || strfnam.contains("%")  || strfnam.contains("^") ||
                        strfnam.contains("&")  || strfnam.contains("*") || strfnam.contains("(")||
                        strfnam.contains(")")  || strfnam.contains("-") || strfnam.contains("+") ||
                        strfnam.contains("{")  || strfnam.contains("}")  || strfnam.contains("[") ||
                        strfnam.contains("]")  || strfnam.contains("|")
                        ){
                    cruname.setError("Enter a valid usernName");
                }

                else if (mainpass.length() < 6 ) {
                    crpass.setError("Please enter a scure password");
                }
                else if (! confpass.equals(mainpass)){
                    crconfrm.setError("Password Mismatch");
                } else {

                    userValidation();
                }



//
//                Log.e("pass-->", mainpass + "--" + confpass);
//
//
//                //password wala kaam done
//
//                if (strfnam.length() > 0 && strlnam.length() > 0 &&
//                        strmail.length() > 0 && strphn.length() > 0) {
//
//                    if (mainpass.length() == confpass.length()) {
//                        if (mainpass.equals(confpass)) {
//
//                            if (mainpass.length() < 6) {
//                                Toast.makeText(getContext(), "Enter strong password", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                if (cruname.length() >= 6) {
//
//
//                                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//// onClick of button perform this simplest code.
//                                    if (strmail.matches(emailPattern)) {

//
//                                    } else {
//                                        Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
//                                    }
//
//
//
//                                } else {
//                                    Toast.makeText(getContext(), "Enter strong username", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        Toast.makeText(getContext(), "Password lenth doesn't match", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Enter all credentials", Toast.LENGTH_SHORT).show();
//                }
//
            }
       });


        return rootview;
    }

    public void userValidation() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.show();

        String stcookie = "false";
        String stunamee = cruname.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultsetForLogin> retCall = easyApi.getUsernameUnique(stcookie, stunamee);

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                Log.e("responss----->", response.body().getDattta() + "");
                pDialog.dismiss();
                Boolean data = false;
                if (response.body().getDattta().equals("true")) {
                    String ufname = crfname.getText().toString();
                    String ulname = crlnam.getText().toString();
                    String umail = crmail.getText().toString();
                    String uphn = crphn.getText().toString();
                    String upass = crpass.getText().toString();
                    String uname = cruname.getText().toString();

                    Fragment fragment = new SelcetPlanFragment();
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
                    bundle.putString("Faxnum", faxnum);
                    bundle.putString("AreaName", areaname);
                    bundle.putString("OrigFaxnum", strorifaxtoll);

                    fragment.setArguments(bundle);
                } else {

                    Toast.makeText(getContext(), "Username already exist", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


}
