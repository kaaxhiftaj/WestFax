package com.business.admin.westfax.fragment;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.R;
import com.business.admin.westfax.adapter.AreaCdNumAdapter;
import com.business.admin.westfax.adapter.TollNumAdapter;
import com.business.admin.westfax.model.SampleModel;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.SignToll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupFragment extends Fragment implements  View.OnKeyListener {

    RadioGroup grptab;
    RadioButton rdtoll, rdareacod;
    Spinner spinarcod;
    RadioGroup grpsrch;
    ImageView btnsignext;
    LinearLayout linspin;
    ProgressDialog pDialog;
    RecyclerView recycletoll;
    private GridLayoutManager layoutManager, laygridmanager;
    TollNumAdapter tollNumAdapter;
    ArrayList<String> arraycode = null;
    ArrayList<String> spinarray = null;
    String spinnstring, stuidd;
    String rdtollarea, radiofaaxnum;
    String strorifaxtoll;
    AreaCdNumAdapter areaAdaptt;
    TextView norcrd;
    RecyclerView recyclearea;
    TextView txttoll, txtareaa;
    List<String> itmlist;
    List<String> itmlistArea;
    ArrayList<String> array3;
    String numblast;
    ArrayList<SampleModel> lisss;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_signup, container, false);
        itmlist = new ArrayList<>();
        itmlistArea = new ArrayList<>();
        array3 = new ArrayList<>();
        lisss = new ArrayList<>();
        rdtoll = (RadioButton) rootview.findViewById(R.id.rdtoll);
        rdareacod = (RadioButton) rootview.findViewById(R.id.rdareacod);
        grpsrch = (RadioGroup) rootview.findViewById(R.id.grpsrch);
        spinarcod = (Spinner) rootview.findViewById(R.id.spinarcod);
        btnsignext = (ImageView) rootview.findViewById(R.id.btnsignext);
        norcrd = (TextView) rootview.findViewById(R.id.norcrd);

        recycletoll = (RecyclerView) rootview.findViewById(R.id.recycletoll);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recycletoll.setHasFixedSize(true);
        recycletoll.setLayoutManager(layoutManager);

        recyclearea = (RecyclerView) rootview.findViewById(R.id.recyclearea);
        laygridmanager = new GridLayoutManager(getContext(), 2);
        recyclearea.setHasFixedSize(true);
        recyclearea.setLayoutManager(laygridmanager);
        norcrd.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Step 1: Select Fax Number");

        arraycode = new ArrayList<>();
//        arraycode.add("");
        arraycode.add("202");
        arraycode.add("205");
        arraycode.add("206");
        arraycode.add("208");
        arraycode.add("209");
        arraycode.add("210");
        arraycode.add("213");
        arraycode.add("215");
        arraycode.add("216");
        arraycode.add("217");
        arraycode.add("218");
        arraycode.add("219");
        arraycode.add("220");
        arraycode.add("224");
        arraycode.add("225");
        arraycode.add("226");
        arraycode.add("228");
        arraycode.add("231");
        arraycode.add("239");
        arraycode.add("240");
        arraycode.add("248");
        arraycode.add("251");
        arraycode.add("252");
        arraycode.add("253");
        arraycode.add("254");
        arraycode.add("256");
        arraycode.add("260");
        arraycode.add("262");
        arraycode.add("267");
        arraycode.add("269");
        arraycode.add("270");
        arraycode.add("272");
        arraycode.add("276");
        arraycode.add("281");
        arraycode.add("301");
        arraycode.add("302");
        arraycode.add("303");
        arraycode.add("307");
        arraycode.add("309");
        arraycode.add("310");
        arraycode.add("312");
        arraycode.add("313");
        arraycode.add("314");
        arraycode.add("315");
        arraycode.add("317");
        arraycode.add("318");
        arraycode.add("319");
        arraycode.add("320");
        arraycode.add("321");
        arraycode.add("323");
        arraycode.add("325");
        arraycode.add("331");
        arraycode.add("334");
        arraycode.add("336");
        arraycode.add("337");
        arraycode.add("339");
        arraycode.add("346");
        arraycode.add("351");
        arraycode.add("352");
        arraycode.add("360");
        arraycode.add("361");
        arraycode.add("364");
        arraycode.add("385");
        arraycode.add("386");
        arraycode.add("401");
        arraycode.add("402");
        arraycode.add("405");
        arraycode.add("406");
        arraycode.add("407");
        arraycode.add("409");
        arraycode.add("410");
        arraycode.add("412");
        arraycode.add("413");
        arraycode.add("414");
        arraycode.add("417");
        arraycode.add("419");
        arraycode.add("423");
        arraycode.add("424");
        arraycode.add("425");
        arraycode.add("430");
        arraycode.add("432");
        arraycode.add("434");
        arraycode.add("435");
        arraycode.add("440");
        arraycode.add("443");
        arraycode.add("458");
        arraycode.add("469");
        arraycode.add("470");
        arraycode.add("475");
        arraycode.add("478");
        arraycode.add("480");
        arraycode.add("484");
        arraycode.add("501");
        arraycode.add("502");
        arraycode.add("504");
        arraycode.add("505");
        arraycode.add("507");
        arraycode.add("508");
        arraycode.add("509");
        arraycode.add("510");
        arraycode.add("512");
        arraycode.add("513");
        arraycode.add("515");
        arraycode.add("516");
        arraycode.add("517");
        arraycode.add("518");
        arraycode.add("520");
        arraycode.add("530");
        arraycode.add("531");
        arraycode.add("534");
        arraycode.add("539");
        arraycode.add("540");
        arraycode.add("551");
        arraycode.add("559");
        arraycode.add("561");
        arraycode.add("562");
        arraycode.add("563");
        arraycode.add("567");
        arraycode.add("570");
        arraycode.add("571");
        arraycode.add("573");
        arraycode.add("574");
        arraycode.add("575");
        arraycode.add("580");
        arraycode.add("585");
        arraycode.add("586");
        arraycode.add("601");
        arraycode.add("605");
        arraycode.add("606");
        arraycode.add("607");
        arraycode.add("608");
        arraycode.add("609");
        arraycode.add("610");
        arraycode.add("612");
        arraycode.add("614");
        arraycode.add("615");
        arraycode.add("616");
        arraycode.add("617");
        arraycode.add("618");
        arraycode.add("619");
        arraycode.add("620");
        arraycode.add("626");
        arraycode.add("628");
        arraycode.add("629");
        arraycode.add("630");
        arraycode.add("631");
        arraycode.add("636");
        arraycode.add("641");
        arraycode.add("646");
        arraycode.add("650");
        arraycode.add("651");
        arraycode.add("657");
        arraycode.add("660");
        arraycode.add("661");
        arraycode.add("662");
        arraycode.add("667");
        arraycode.add("669");
        arraycode.add("678");
        arraycode.add("680");
        arraycode.add("682");
        arraycode.add("701");
        arraycode.add("702");
        arraycode.add("704");
        arraycode.add("706");
        arraycode.add("707");
        arraycode.add("708");
        arraycode.add("712");
        arraycode.add("714");
        arraycode.add("716");
        arraycode.add("717");
        arraycode.add("724");
        arraycode.add("725");
        arraycode.add("726");
        arraycode.add("727");
        arraycode.add("731");
        arraycode.add("732");
        arraycode.add("734");
        arraycode.add("737");
        arraycode.add("740");
        arraycode.add("747");
        arraycode.add("754");
        arraycode.add("757");
        arraycode.add("758");
        arraycode.add("760");
        arraycode.add("762");
        arraycode.add("763");
        arraycode.add("765");
        arraycode.add("769");
        arraycode.add("772");
        arraycode.add("773");
        arraycode.add("774");
        arraycode.add("775");
        arraycode.add("781");
        arraycode.add("786");
        arraycode.add("800");
        arraycode.add("803");
        arraycode.add("804");
        arraycode.add("805");
        arraycode.add("806");
        arraycode.add("808");
        arraycode.add("810");
        arraycode.add("812");
        arraycode.add("813");
        arraycode.add("814");
        arraycode.add("815");
        arraycode.add("816");
        arraycode.add("817");
        arraycode.add("818");
        arraycode.add("828");
        arraycode.add("830");
        arraycode.add("831");
        arraycode.add("832");
        arraycode.add("838");
        arraycode.add("843");
        arraycode.add("845");
        arraycode.add("847");
        arraycode.add("850");
        arraycode.add("854");
        arraycode.add("855");
        arraycode.add("856");
        arraycode.add("857");
        arraycode.add("858");
        arraycode.add("859");
        arraycode.add("860");
        arraycode.add("862");
        arraycode.add("863");
        arraycode.add("864");
        arraycode.add("865");
        arraycode.add("866");
        arraycode.add("870");
        arraycode.add("872");
        arraycode.add("877");
        arraycode.add("878");
        arraycode.add("888");
        arraycode.add("901");
        arraycode.add("903");
        arraycode.add("904");
        arraycode.add("908");
        arraycode.add("909");
        arraycode.add("910");
        arraycode.add("912");
        arraycode.add("913");
        arraycode.add("914");
        arraycode.add("916");
        arraycode.add("918");
        arraycode.add("919");
        arraycode.add("920");
        arraycode.add("925");
        arraycode.add("928");
        arraycode.add("929");
        arraycode.add("931");
        arraycode.add("934");
        arraycode.add("936");
        arraycode.add("937");
        arraycode.add("939");
        arraycode.add("940");
        arraycode.add("941");
        arraycode.add("949");
        arraycode.add("951");
        arraycode.add("956");
        arraycode.add("959");
        arraycode.add("970");
        arraycode.add("971");
        arraycode.add("972");
        arraycode.add("973");
        arraycode.add("978");
        arraycode.add("979");
        arraycode.add("980");
        arraycode.add("984");
        arraycode.add("985");
        arraycode.add("989");


        spinarray = new ArrayList<>();
//        spinarray.add("Select Area Code");
        spinarray.add("202-District of Columbia");
        spinarray.add("205-Alabama");
        spinarray.add("206-Washington");
        spinarray.add("208-Idaho");
        spinarray.add("209-California");
        spinarray.add("210-Texas");
        spinarray.add("213-California");
        spinarray.add("215-Pennsylvania");
        spinarray.add("216-Ohio");
        spinarray.add("217-Illinois");
        spinarray.add("218-Minnesota");
        spinarray.add("219-Indianna");
        spinarray.add("220-Ohio");
        spinarray.add("224-Illinois");
        spinarray.add("225-Louisiana");
        spinarray.add("226-Ontario");
        spinarray.add("228-Mississippi");
        spinarray.add("231-Michigan");
        spinarray.add("239-Florida");
        spinarray.add("240-Maryland");
        spinarray.add("248-Michigan");
        spinarray.add("251-Alabama");
        spinarray.add("252-North Carolina");
        spinarray.add("253-Washington");
        spinarray.add("254-Texas");
        spinarray.add("256-Alabama");
        spinarray.add("260-Indianna");
        spinarray.add("262-Wisconsin");
        spinarray.add("267-Pennsylvania");
        spinarray.add("269-Michigan");
        spinarray.add("270-Kentucky");
        spinarray.add("272-Pennsylvania");
        spinarray.add("276-Virginia");
        spinarray.add("281-Texas");
        spinarray.add("301-Maryland");
        spinarray.add("302-Delaware");
        spinarray.add("303-Colorado");
        spinarray.add("307-Wyoming");
        spinarray.add("309-Illinois");
        spinarray.add("310-California");
        spinarray.add("312-Illinois");
        spinarray.add("313-Michigan");
        spinarray.add("314-Missouri");
        spinarray.add("315-New York");
        spinarray.add("317-Indianna");
        spinarray.add("318-Louisiana");
        spinarray.add("319-Iowa");
        spinarray.add("320-Minnesota");
        spinarray.add("321-Florida");
        spinarray.add("323-California");
        spinarray.add("325-Texas");
        spinarray.add("331-Illinois");
        spinarray.add("334-Alabama");
        spinarray.add("336-North Carolina");
        spinarray.add("337-Louisiana");
        spinarray.add("339-Massachusetts");
        spinarray.add("346-Texas");
        spinarray.add("351-Massachusetts");
        spinarray.add("352-Florida");
        spinarray.add("360-Washington");
        spinarray.add("361-Texas");
        spinarray.add("364-Kentucky");
        spinarray.add("385-Utah");
        spinarray.add("386-Florida");
        spinarray.add("401-Rhode Island");
        spinarray.add("402-Nebraska");
        spinarray.add("405-Oklahoma");
        spinarray.add("406-Montana");
        spinarray.add("407-Florida");
        spinarray.add("409-Texas");
        spinarray.add("410-Maryland");
        spinarray.add("412-Pennsylvania");
        spinarray.add("413-Massachusetts");
        spinarray.add("414-Wisconsin");
        spinarray.add("417-Missouri");
        spinarray.add("419-Ohio");
        spinarray.add("423-Tennessee");
        spinarray.add("424-California");
        spinarray.add("425-Washington");
        spinarray.add("430-Texas");
        spinarray.add("432-Texas");
        spinarray.add("434-Virginia");
        spinarray.add("435-Utah");
        spinarray.add("440-Ohio");
        spinarray.add("443-Maryland");
        spinarray.add("458-Oregon");
        spinarray.add("469-Texas");
        spinarray.add("470-Georgia");
        spinarray.add("475-Connecticut");
        spinarray.add("478-Georgia");
        spinarray.add("480-Arizona");
        spinarray.add("484-Pennsylvania");
        spinarray.add("501-Arkansas");
        spinarray.add("502-Kentucky");
        spinarray.add("504-Louisiana");
        spinarray.add("505-New Mexico");
        spinarray.add("507-Minnesota");
        spinarray.add("508-Massachusetts");
        spinarray.add("509-Washington");
        spinarray.add("510-California");
        spinarray.add("512-Texas");
        spinarray.add("513-Ohio");
        spinarray.add("515-Iowa");
        spinarray.add("516-New York");
        spinarray.add("517-Michigan");
        spinarray.add("518-New York");
        spinarray.add("520-Arizona");
        spinarray.add("530-California");
        spinarray.add("531-Nebraska");
        spinarray.add("534-Wisconsin");
        spinarray.add("539-Oklahoma");
        spinarray.add("540-Virginia");
        spinarray.add("551-New Jersey");
        spinarray.add("559-California");
        spinarray.add("561-Florida");
        spinarray.add("562-California");
        spinarray.add("563-Iowa");
        spinarray.add("567-Ohio");
        spinarray.add("570-Pennsylvania");
        spinarray.add("571-Virginia");
        spinarray.add("573-Missouri");
        spinarray.add("574-Indianna");
        spinarray.add("575-New Mexico");
        spinarray.add("580-Oklahoma");
        spinarray.add("585-New York");
        spinarray.add("586-Michigan");
        spinarray.add("601-Mississippi");
        spinarray.add("605-South Dakota");
        spinarray.add("606-Kentucky");
        spinarray.add("607-New York");
        spinarray.add("608-Wisconsin");
        spinarray.add("609-New Jersey");
        spinarray.add("610-Pennsylvania");
        spinarray.add("612-Minnesota");
        spinarray.add("614-Ohio");
        spinarray.add("615-Tennessee");
        spinarray.add("616-Michigan");
        spinarray.add("617-Massachusetts");
        spinarray.add("618-Illinois");
        spinarray.add("619-California");
        spinarray.add("620-Kansas");
        spinarray.add("626-California");
        spinarray.add("628-California");
        spinarray.add("629-Tennessee");
        spinarray.add("630-Illinois");
        spinarray.add("631-New York");
        spinarray.add("636-Missouri");
        spinarray.add("641-Iowa");
        spinarray.add("646-New York");
        spinarray.add("650-California");
        spinarray.add("651-Minnesota");
        spinarray.add("657-California");
        spinarray.add("660-Missouri");
        spinarray.add("661-California");
        spinarray.add("662-Mississippi");
        spinarray.add("667-Maryland");
        spinarray.add("669-California");
        spinarray.add("678-Georgia");
        spinarray.add("680-New York");
        spinarray.add("682-Texas");
        spinarray.add("701-North Dakota");
        spinarray.add("702-Nevada");
        spinarray.add("704-North Carolina");
        spinarray.add("706-Georgia");
        spinarray.add("707-California");
        spinarray.add("708-Illinois");
        spinarray.add("712-Iowa");
        spinarray.add("714-California");
        spinarray.add("716-New York");
        spinarray.add("717-Pennsylvania");
        spinarray.add("724-Pennsylvania");
        spinarray.add("725-Nevada");
        spinarray.add("726-Texas");
        spinarray.add("727-Florida");
        spinarray.add("731-Tennessee");
        spinarray.add("732-New Jersey");
        spinarray.add("734-Michigan");
        spinarray.add("737-Texas");
        spinarray.add("740-Ohio");
        spinarray.add("747-California");
        spinarray.add("754-Florida");
        spinarray.add("757-Virginia");
        spinarray.add("758-Saint Lucia");
        spinarray.add("760-California");
        spinarray.add("762-Georgia");
        spinarray.add("763-Minnesota");
        spinarray.add("765-Indianna");
        spinarray.add("769-Mississippi");
        spinarray.add("772-Florida");
        spinarray.add("773-Illinois");
        spinarray.add("774-Massachusetts");
        spinarray.add("775-Nevada");
        spinarray.add("781-Massachusetts");
        spinarray.add("786-Florida");
        spinarray.add("800-Toll Free");
        spinarray.add("803-South Carolina");
        spinarray.add("804-Virginia");
        spinarray.add("805-California");
        spinarray.add("806-Texas");
        spinarray.add("808-Hawaii");
        spinarray.add("810-Michigan");
        spinarray.add("812-Indianna");
        spinarray.add("813-Florida");
        spinarray.add("814-Pennsylvania");
        spinarray.add("815-Illinois");
        spinarray.add("816-Missouri");
        spinarray.add("817-Texas");
        spinarray.add("818-California");
        spinarray.add("828-North Carolina");
        spinarray.add("830-Texas");
        spinarray.add("831-California");
        spinarray.add("832-Texas");
        spinarray.add("838-New York");
        spinarray.add("843-South Carolina");
        spinarray.add("845-New York");
        spinarray.add("847-Illinois");
        spinarray.add("850-Florida");
        spinarray.add("854-South Carolina");
        spinarray.add("855-Toll Free");
        spinarray.add("856-New Jersey");
        spinarray.add("857-Massachusetts");
        spinarray.add("858-California");
        spinarray.add("859-Kentucky");
        spinarray.add("860-Connecticut");
        spinarray.add("862-New Jersey");
        spinarray.add("863-Florida");
        spinarray.add("864-South Carolina");
        spinarray.add("865-Tennessee");
        spinarray.add("866-Toll Free");
        spinarray.add("870-Arkansas");
        spinarray.add("872-Illinois");
        spinarray.add("877-Toll Free");
        spinarray.add("878-Pennsylvania");
        spinarray.add("888-Toll Free");
        spinarray.add("901-Tennessee");
        spinarray.add("903-Texas");
        spinarray.add("904-Florida");
        spinarray.add("908-New Jersey");
        spinarray.add("909-California");
        spinarray.add("910-North Carolina");
        spinarray.add("912-Georgia");
        spinarray.add("913-Kansas");
        spinarray.add("914-New York");
        spinarray.add("916-California");
        spinarray.add("918-Oklahoma");
        spinarray.add("919-North Carolina");
        spinarray.add("920-Wisconsin");
        spinarray.add("925-California");
        spinarray.add("928-Arizona");
        spinarray.add("929-New York");
        spinarray.add("931-Tennessee");
        spinarray.add("934-New York");
        spinarray.add("936-Texas");
        spinarray.add("937-Ohio");
        spinarray.add("939-Puerto Rico");
        spinarray.add("940-Texas");
        spinarray.add("941-Florida");
        spinarray.add("949-California");
        spinarray.add("951-California");
        spinarray.add("956-Texas");
        spinarray.add("959-Connecticut");
        spinarray.add("970-Colorado");
        spinarray.add("971-Oregon");
        spinarray.add("972-Texas");
        spinarray.add("973-New Jersey");
        spinarray.add("978-Massachusetts");
        spinarray.add("979-Texas");
        spinarray.add("980-North Carolina");
        spinarray.add("984-North Carolina");
        spinarray.add("985-Louisiana");
        spinarray.add("989-Michigan");


//        ArrayAdapter genadapter = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.textspinn, spinarray);
//        spinarcod.setAdapter(genadapter);


        linspin = (LinearLayout) rootview.findViewById(R.id.linspin);
        // linspin.setVisibility(View.GONE);

        txttoll = (TextView) rootview.findViewById(R.id.txttoll);
        txtareaa = (TextView) rootview.findViewById(R.id.txtareaa);

        grpsrch.clearCheck();
        rdtoll.setChecked(true);

        if (rdtoll.isChecked()) {
            getTollList();
            rdtollarea = "TollFree";
            linspin.setVisibility(View.GONE);
            recycletoll.setVisibility(View.VISIBLE);
            recyclearea.setVisibility(View.GONE);
            txtareaa.setVisibility(View.GONE);
        }

        loadJSONFromAsset();
        grpsrch.setOnCheckedChangeListener(radiochange);
        btnsignext.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
//
//        btnsignext.setColorFilter(Color.argb(225, 229,231,233));

        btnsignext.setOnClickListener(clickListener);
        txttoll.setText("Please Choose Number");
        btnsignext.setEnabled(false);

        if (txttoll.getText().toString().equals("Please Choose Number") || txtareaa.getText().toString().equals("Please Choose Number"))

        {
            btnsignext.setEnabled(false);
            btnsignext.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);

        } else {
            btnsignext.setEnabled(true);
        }
        return rootview;
    }


    public ArrayList<SampleModel> loadJSONFromAsset() {
        lisss = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("product.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONArray m_jArry = new JSONArray(json);

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                SampleModel model = new SampleModel();
                model.setVal(jo_inside.getString("Val"));
                model.setDisCode(jo_inside.getString("DisplayName"));
                model.setDisName(jo_inside.getString("DisplayDesc"));

                Log.e("get modval", jo_inside.getString("DisplayName") + "\n" + jo_inside.getString("DisplayDesc"));
                //Add your values in your `ArrayList` as below:
                lisss.add(model);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lisss;
    }


    public void getTollList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.show();
//        pDialog.setCancelable(false);
        String stcookie = Config.cokkiee;
        final String[] tolldata = new String[1];
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("Name", "SearchType");
            jsonobject_one.put("Value", "TollFree");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<SignToll> retCall = easyApi.gettollfree(jsonobject_one, stcookie);
//     ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<SignToll>() {
            @Override
            public void onResponse(Call<SignToll> call, Response<SignToll> response) {
                Log.e("ressbodyyyy----->", response.body().getMessage() + "");
                pDialog.dismiss();
                itmlist = new ArrayList<>();
                itmlist.clear();

                if (response.body().getMessage().equals("OK")) {
                    //   Toast.makeText(getContext(), "signup", Toast.LENGTH_LONG).show();

                    Log.e("data  respp----->", response.body().getData() + "");
                    List<String> jsonArray = response.body().getData();
//                    itmlist.clear();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        tolldata[0] = (response.body().getData().get(i));
                        itmlist.add(tolldata[0]);
                        Log.e("lisss--->", tolldata[0] + "");
                    }

                    if (itmlist.size() > 0) {
                        tollNumAdapter = new TollNumAdapter(getContext(), itmlist);

                        recycletoll.setAdapter(tollNumAdapter);
                        norcrd.setVisibility(View.GONE);
                        recyclearea.setVisibility(View.GONE);
                        recycletoll.setVisibility(View.VISIBLE);
                    } else {
                        norcrd.setVisibility(View.VISIBLE);
                        recyclearea.setVisibility(View.GONE);
                        recycletoll.setVisibility(View.GONE);
                    }
                    btnsignext.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
//
//                    btnsignext.setColorFilter(Color.argb(225, 229,231,233));
                    btnsignext.setEnabled(false);

                    tollNumAdapter.setItemClickCallback(new TollNumAdapter.ItemClickCallback() {
                        @Override
                        public void onItemClick(int p) {
                            Log.e("item poss--------->??", tollNumAdapter.getSelectedItemForToll() + "");
//                            tollNumAdapter.getSelectedItem();
                            String s = tollNumAdapter.getSelectedItemForToll() + "";
                            String s1 = s.substring(0, 3);
                            String s2 = s.substring(3, 6);
                            String s3 = s.substring(6, 10);
                            String mains = "Choose (" + s1 + ")" + s2 + "-" + s3;
                            txttoll.setText(mains);

                            txtareaa.setText("");
                            txttoll.setVisibility(View.VISIBLE);
                            txtareaa.setVisibility(View.GONE);
                            btnsignext.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
//                            btnsignext.setColorFilter(Color.argb(225, 226, 56, 63));
                            btnsignext.setEnabled(true);
//                            Toast.makeText(getContext(),
//                                    "selected-->" + p,
//                                    Toast.LENGTH_LONG).show();

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignToll> call, Throwable t) {
                Log.e("faillier----->", call + "");

                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }


    public void CountrySpinner() {

        spinarcod.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinarray));

        spinarcod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {

//                section_spinn.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,stringSec1));

                stuidd = (String) parent.getItemAtPosition(pos);

                spinnstring = (String) arraycode.get(pos);
                Log.d("clslsls%%%", spinnstring + "===" + stuidd);
                getPhoneAreaCode();
//                dataa.clear();
            }
        });
    }

    RadioGroup.OnCheckedChangeListener radiochange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            if (checkedId == R.id.rdtoll) {
                linspin.setVisibility(View.GONE);
                radiofaaxnum = tollNumAdapter.radtiovalue + "";
                txttoll.setText("Please Choose Number");
                txtareaa.setText("");
                rdtollarea = "TollFree";
                getTollList();

                recycletoll.setVisibility(View.VISIBLE);
                recyclearea.setVisibility(View.GONE);
                txttoll.setVisibility(View.VISIBLE);
                btnsignext.setEnabled(false);
                btnsignext.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
                txtareaa.setVisibility(View.GONE);
            } else if (checkedId == R.id.rdareacod) {
//                getCountryCode();
                CountrySpinner();
                rdtollarea = "AreaCode";
                recycletoll.setVisibility(View.GONE);
                recyclearea.setVisibility(View.VISIBLE);
                radiofaaxnum = areaAdaptt.radtiovalue + "";
                txttoll.setText("");
                txtareaa.setText("Please Choose Number");

                linspin.setVisibility(View.VISIBLE);
                btnsignext.setEnabled(false);
                btnsignext.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);

                txttoll.setVisibility(View.GONE);
                txtareaa.setVisibility(View.VISIBLE);
            }
        }
    };


    public void getCountryCode() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;
        final String[] areadata = new String[1];
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<SignToll> retCall = easyApi.getAreaCode(stcookie);
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<SignToll>() {
            @Override
            public void onResponse(Call<SignToll> call, Response<SignToll> response) {
                Log.e("ressbodyyyy----->", response.body().getMessage() + "");
                pDialog.dismiss();
                final List<String> itmlist = new ArrayList<>();

                if (response.body().getMessage().equals("OK")) {
//                    Toast.makeText(getContext(), "signup", Toast.LENGTH_LONG).show();
//                    itmlist.add("Select Area Code");
                    Log.e("data  respp----->", response.body().getData() + "");
                    List<String> jsonArray = response.body().getData();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        areadata[0] = (response.body().getData().get(i));
                        itmlist.add(areadata[0]);
                    }
                    Log.e("lisss--->", itmlist.size() + "");

//
                    ArrayList<String> lis = new ArrayList<>();
//                    for (int i1 = 0; i1 < jsonArray.size(); i1++) {
//
//                        for (int i = 0; i < lisss.size(); i++) {
//
//                            if (jsonArray.get(i1).contains(lisss.get(i).getDisCode())) {
//
//                                Log.e("satic liss", lisss.get(i).getDisName() + "-" + lisss.get(i).getDisCode());
//                            }
//                        }
////
//                    }

//
                    for (SampleModel person2 : lisss) {
                        // Loop arrayList1 items
                        for (int i = 0; i < jsonArray.size(); i++) {
                            if (jsonArray.get(i).equalsIgnoreCase(person2.getDisCode())) {

                                lis.add(lisss.get(i).getDisCode() + "-" + lisss.get(i).getDisName());

                                Log.e("\n strmatchhh", response.body().getData().get(i) + "---" + person2.getDisName());
                            }
                        }
                    }

                    Log.e("> DATA LIS ", lis.size() + "\n" + lis.toArray());


//                        if (!found) {
//////                            resultarray.add(person2.getDisName());
////                        }
////                        Log.e("result array",resultarray+"");
//
//
//                    }

//                    Iterator<String> itr = arraycode.iterator();
//
//                    while (itr.hasNext()) {
//                        String list_two_element = itr.next();
//
//                        if (itmlist.contains(list_two_element)) {
//
//                            Log.e("\n match vall", list_two_element);
//
//
//                            array3 = new ArrayList<String>(itmlist.size()); // Make a new list
//                            for (int i = 0; i < itmlist.size(); i++) { // Loop through every name/phone number combo
//                                array3.add(itmlist.get(i) + "-" + spinarray.get(i)); // Concat the two, and add it
//                            }
//
//                        }

//                    }

                    spinarcod.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lis));

                    spinarcod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int pos, long id) {

//                section_spinn.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,stringSec1));

                            stuidd = (String) parent.getItemAtPosition(pos);

                            spinnstring = (String) arraycode.get(pos);
                            Log.d("clslsls%%%", spinnstring + "===" + stuidd);
                            getPhoneAreaCode();
//                dataa.clear();
                        }
                    });

//                    ArrayAdapter genadapter = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.textspinn, itmlist);
//                    spinarcod.setAdapter(genadapter);


//                    tollNumAdapter = new TollNumAdapter(getContext(), itmlist);
//
//                    recycletoll.setAdapter(tollNumAdapter);


                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignToll> call, Throwable t) {
                Log.e("faillier----->", call + "");

                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void getPhoneAreaCode() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        String stcookie = Config.cokkiee;

        final String[] areadata = new String[1];
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONObject method_one = new JSONObject();
        try {
            method_one.put("Name", "SearchType");
            method_one.put("Value", "AreaCode");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sinitem = spinnstring;
//                spinarcod.getSelectedItem().toString();
        Log.e("selllll----->", sinitem + "");

        JSONObject method_two = new JSONObject();
        try {
            method_two.put("Name", "SearchAreaCode");
            method_two.put("Value", sinitem);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<SignToll> retCall = easyApi.getAreaCodeNumber(method_one, method_two, stcookie);
//   ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<SignToll>() {
            @Override
            public void onResponse(Call<SignToll> call, Response<SignToll> response) {
                Log.e("getArea----->", response.body().getMessage() + "");
                pDialog.dismiss();
                itmlistArea = new ArrayList<>();

                if (response.body().getMessage().equals("OK")) {
                    //    Toast.makeText(getContext(), "signup", Toast.LENGTH_LONG).show();

                    Log.e("data  respp----->", response.body().getData() + "");
                    final List<String> jsonArray = response.body().getData();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        areadata[0] = (response.body().getData().get(i));
                        itmlistArea.add(areadata[0]);
                        Log.e("arealis--->", areadata[0] + "");
                    }

                    if (itmlistArea.size() > 0) {
                        recyclearea.setVisibility(View.VISIBLE);
                        recycletoll.setVisibility(View.GONE);
                        norcrd.setVisibility(View.GONE);
                        areaAdaptt = new AreaCdNumAdapter(getContext(), itmlistArea);

                        recyclearea.setAdapter(areaAdaptt);
//                        btnsignext.setColorFilter(Color.argb(225, 229,231,233));
                        btnsignext.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
//
                        btnsignext.setEnabled(false);
                        areaAdaptt.setItemClickCallback(new AreaCdNumAdapter.ItemClickCallback() {
                            @Override
                            public void onItemClick(int p) {
                                Log.e("item poss--------->??", areaAdaptt.getSelectedItem() + "");
                                areaAdaptt.getSelectedItem();
                                txttoll.setText("");

                                String s = areaAdaptt.getSelectedItem() + "";
                                String s1 = s.substring(0, 3);
                                String s2 = s.substring(3, 6);
                                String s3 = s.substring(6, 10);
                                String mains = "Choose (" + s1 + ")" + s2 + "-" + s3;
                                txtareaa.setText(mains);

//                                txtareaa.setText(areaAdaptt.getSelectedItem());
                                txttoll.setVisibility(View.GONE);
                                txtareaa.setVisibility(View.VISIBLE);
                                btnsignext.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
//
//                                btnsignext.setColorFilter(Color.argb(225, 226, 56, 63));
                                btnsignext.setEnabled(true);
                                //                            Toast.makeText(getContext(),
//                                    "selected-->" + p,
//                                    Toast.LENGTH_LONG).show();

                            }
                        });


                    } else {

                        recycletoll.setVisibility(View.GONE);
                        recyclearea.setVisibility(View.GONE);
                        norcrd.setVisibility(View.VISIBLE);
                    }
//                    tollNumAdapter.setOnItemClickListener(new TollNumAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(String myitmd) {
//                            Log.e("item poss--------->", myitmd+"");
//                        }
//
////                        @Override
////                        public void onItemClick(List<String> item, int pos) {
////                            Log.e("item poss--------->", pos + "--------"+ item.get(pos));
////
////                        }
//                    });


                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignToll> call, Throwable t) {
                //  Log.e("faillier----->", call + "");

                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            radiofaaxnum = "";

            if (rdtollarea.equals("TollFree")) {
                //   tollNumAdapter.notifyDataSetChanged();
                numblast = txttoll.getText().toString();
                radiofaaxnum = tollNumAdapter.radtiovalue + "";

                strorifaxtoll = tollNumAdapter.origradnumb + "";
                Log.e("congooo toll>>??", tollNumAdapter.radtiovalue + "");


                Log.e("val>>??", rdtollarea + "");
                Log.e("areacod>>??", spinnstring + "");

//                if (radiofaaxnum.equals(null) || radiofaaxnum.equals("null") || radiofaaxnum.equals("")) {
//                    Toast.makeText(getContext(), "Plase choose any code", Toast.LENGTH_LONG).show();
//
//                }

                if (numblast.equals(null) || numblast.equals("null") || numblast.equals("") || numblast.equals("Please Choose Number")) {
                    Toast.makeText(getContext(), "Plase choose any code", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getContext(), "congoo", Toast.LENGTH_LONG).show();
                    Fragment fragment = new YourProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", "SearchType");
                    bundle.putString("Value", rdtollarea);
                    bundle.putString("Areacode", "");
                    bundle.putString("AreaName", "");
                    bundle.putString("Faxnum", radiofaaxnum);
                    bundle.putString("OrigFaxnum", strorifaxtoll);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.mainfrm, fragment)
                            .commit();
                }

            } else if (rdtollarea.equals("AreaCode")) {
                numblast = txtareaa.getText().toString();
                radiofaaxnum = areaAdaptt.radtiovalue + "";
                strorifaxtoll = areaAdaptt.origradtiovalue + "";
                Log.e("congooo area>>??", areaAdaptt.radtiovalue + "");
                Log.e("val>>??", rdtollarea + "");
                Log.e("areacod>>??", spinnstring + "--" + stuidd);

                if (spinnstring.equals("") || spinnstring.equals(null)) {
                    Toast.makeText(getContext(), "Select Area code", Toast.LENGTH_LONG).show();
                }
//                else if (radiofaaxnum.equals("null") || radiofaaxnum.equals("") || radiofaaxnum.equals(null)) {
//                    Toast.makeText(getContext(), "Select Number", Toast.LENGTH_LONG).show();
//
//                }
                else if (numblast.equals("null") || numblast.equals("") || numblast.equals(null)|| numblast.equals("Please Choose Number")) {
                    Toast.makeText(getContext(), "Select Any Number", Toast.LENGTH_LONG).show();

                } else {
                    Fragment fragment = new YourProfileFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString("Name", "SearchType");
                    bundle.putString("Value", rdtollarea);
                    bundle.putString("Areacode", spinnstring + "");
                    bundle.putString("AreaName", stuidd + "");
                    bundle.putString("Faxnum", radiofaaxnum);
                    bundle.putString("OrigFaxnum", strorifaxtoll);

                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.mainfrm, fragment)
                            .commit();
                    //    Toast.makeText(getContext(), "Area congo", Toast.LENGTH_LONG).show();

                }
            }


//
//
//            if (radiofaaxnum == null ) {
////                TollFree
////                        AreaCode
//
//
//
//                Toast.makeText(getContext(), "Plase choose any code", Toast.LENGTH_LONG).show();
//
//            } else {
//
//                if (spinnstring.equals("Select Area Code")) {
//                    Toast.makeText(getContext(), "Select Area code", Toast.LENGTH_LONG).show();
//
//                } else {
////                    Fragment fragment = new YourProfileFragment();
////                    Bundle bundle = new Bundle();
////                    bundle.putString("Name", "SearchType");
////                    bundle.putString("Value", rdtollarea);
////                    bundle.putString("Areacode", spinnstring + "");
////                    bundle.putString("Faxnum", radiofaaxnum);
////
////                    fragment.setArguments(bundle);
////                    getFragmentManager().beginTransaction()
////                            .replace(R.id.mainfrm, fragment)
////                            .commit();
//                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
//                }
//            }

        }
    };

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
               getActivity().finish();
                return true;
            }
        }

        return false;
    }

}

