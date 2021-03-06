package com.tukuri.ics.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.chuross.library.ExpandableLayout;
import com.tukuri.ics.Config.BaseURL;
import com.tukuri.ics.LoginActivity;
import com.tukuri.ics.Model.Delivery_address_model;

import com.tukuri.ics.AppController;
import com.tukuri.ics.MainActivity;
import com.tukuri.ics.R;

import com.tukuri.ics.AppPreference;
import com.tukuri.ics.ConnectivityReceiver;
import com.tukuri.ics.CustomVolleyJsonRequest;
import com.tukuri.ics.Session_management;


public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();
    private EditText et_phone, et_name, et_pin, et_house,et_landmark, et_city, et_state,et_add_adres_house_no, et_latlng;
    private Button btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity, btn_socity, tx_mmap;
    private FrameLayout Frame_Map;
    private String getsocity = "";
    private Session_management sessionManagement;
    private boolean isEdit = false;
    private String getlocation_id;
    private String getphone;
    private String getname;
    private String getpin;
    private String gethouse;
    private ArrayList<Delivery_address_model> delivery_address_modelList = new ArrayList<Delivery_address_model>();
    String getlandmark,getcity,getstate,gethouseno;
    private String Faddress="---", Flatitude, Flongitude;
    private ScrollView scroll_view;
    private ExpandableLayout expandable_lyt;

    public Add_delivery_address_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add_delivery_address));

        sessionManagement = new Session_management(getActivity());

        scroll_view = view.findViewById(R.id.scrollView1);
        expandable_lyt = view.findViewById(R.id.expandable_lyt);
        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        et_pin = (EditText) view.findViewById(R.id.et_add_adres_pin);
        et_house = (EditText) view.findViewById(R.id.et_add_adres_home);
        et_landmark = (EditText) view.findViewById(R.id.et_add_adres_landmark);
        et_city = (EditText) view.findViewById(R.id.et_add_adres_city);
        et_state = (EditText) view.findViewById(R.id.et_add_adres_state);
        et_add_adres_house_no = (EditText) view.findViewById(R.id.et_add_adres_house_no);
        et_latlng = (EditText) view.findViewById(R.id.et_latlng);

        tv_house = (TextView) view.findViewById(R.id.tv_add_adres_home);

        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);
        btn_update = (Button) view.findViewById(R.id.btn_add_adres_edit);

        Frame_Map = (FrameLayout) view.findViewById(R.id.map_fragment);
        tx_mmap = (TextView) view.findViewById(R.id.tx_mmap);

        androidx.fragment.app.FragmentManager fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment , new Map_Fragment(scroll_view)).commit();

        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);

        String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        Bundle args = getArguments();

        if (args != null) {

            delivery_address_modelList = getArguments().getParcelableArrayList("ListModel");
            Log.e("vxvm", delivery_address_modelList.size() + "");
            if (delivery_address_modelList.size() > 0) {
                String get_name = null, get_phone = null, get_pine = null, get_house = null;
                for (Delivery_address_model delivery_address_model : delivery_address_modelList) {
                    getlocation_id = delivery_address_model.getLocation_id();
                    get_name = delivery_address_model.getReceiver_name();
                    get_phone = delivery_address_model.getReceiver_mobile();
                    get_pine = delivery_address_model.getPincode();
                    get_house = delivery_address_model.getHouse_no();
                }
                if (TextUtils.isEmpty(get_name) && get_name == null) {
                    isEdit = false;
                } else {
                    isEdit = true;
                    //  Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
                    et_name.setText(get_name);
                    et_phone.setText(get_phone);
                    et_pin.setText(get_pine);
                    et_add_adres_house_no.setText(get_house);
//                  sessionManagement.updateSocity(get_socity_name, get_socity_id);
                }
            } else {
            }
        }

        if (!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }

        btn_update.setOnClickListener(this);
        btn_socity.setOnClickListener(this);

        et_latlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Please Click On Map to get your Exact Address Location( Lat/Long )", Toast.LENGTH_LONG).show();
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadcastRec, new IntentFilter("StringAddr"));

      /*  scroll_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scroll_view.getScrollY(); //for verticalScrollView
                if (scrollY == 0){
                    expandable_lyt.expand();
                    Log.e("TOP "," TOP -------------");}
                else{
                    expandable_lyt.collapse();
                    Log.e("Not "," Not XXXXXXXX");}
            }
        });*/

        return view;
    }


    private final BroadcastReceiver localBroadcastRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null)
            {
                Faddress = intent.getStringExtra("Addr");
                Flatitude = intent.getStringExtra("latitiude");
                Flongitude = intent.getStringExtra("longitude");

                et_house.setText(""+Faddress);
                et_latlng.setText(""+Flatitude+" , "+Flongitude);

                //Log.e("FINALY Address in Delivery Fragment :","------------------"+intent.getStringExtra("Addr"));
            }
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        }
        else if (id == R.id.btn_add_adres_socity) {

         String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {

                Bundle args = new Bundle();
                Fragment fm = new Socity_fragment();
                args.putString("pincode", getpincode);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
           tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        getphone = et_phone.getText().toString();
        getname = et_name.getText().toString();
        getpin = et_pin.getText().toString();
        gethouse = et_house.getText().toString();
        getlandmark = et_landmark.getText().toString();
        getcity = et_city.getText().toString();
        getstate = et_state.getText().toString();
        gethouseno = et_add_adres_house_no.getText().toString();
        getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }
        else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }

        if (TextUtils.isEmpty(gethouse)) {
            tv_house.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_house;
            cancel = true;
        }

        if (et_latlng.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter Lat/Long location for delivery...", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
            Toast.makeText(getActivity(), "Please Select Society...", Toast.LENGTH_SHORT).show();
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

               //  String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                String user_id= AppPreference.getUser_Id(getActivity());

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    if (isEdit) {
                        Log.e("CALLL 1"," 1 1111111111111111 ");
                        makeEditAddressRequest(user_id,getlocation_id, getpin, gethouse, getname, getphone,getlandmark,getcity,getstate,gethouseno,getsocity);
                       // makeEditAddressRequest(getlocation_id, getpin, getsocity, gethouse, getname, getphone);
                    } else {
                        Log.e("CALLL 2"," 2222222222222");
                      //  makeAddAddressRequest(user_id, getpin, getsocity, gethouse, getname, getphone);
                        makeAddAddressRequest(user_id, getpin, gethouse, getname, getphone,getlandmark,getcity,getstate,gethouseno,getsocity);
                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */

    private void makeAddAddressRequest(String user_id, String pincode,
                                       String house_add, String receiver_name, String receiver_mobile,
                                       String getlandmark, String getcity, String getstate, String gethouseno, String getsocity) {

        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("socity_id", getsocity);
        params.put("house_address", house_add);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
        params.put("landmark", getlandmark);
        params.put("city", "");
        params.put("state", "");
        params.put("house_no", gethouseno);
        params.put("latitude",Flatitude);
        params.put("longitude",Flongitude);

        Log.e("post_addres",""+params);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Submitting Address...");
        dialog.show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ADDRESS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.e("post_addres_responce",""+response.toString());
                dialog.dismiss();
                try {
                     Boolean status = response.getBoolean("responce");
                    if (status) {
                        int locationid = response.getInt("location_id");
                        Log.e(">>>>>>>> "," lid "+locationid);
                        Bundle arggs = new Bundle();
                        arggs.putString("location_id", ""+locationid);
                        arggs.putString("pincode", response.getString("pincode"));
                        arggs.putString("house_no", response.getString("house_no"));
                        arggs.putString("receiver_name", response.getString("receiver_name"));
                        arggs.putString("receiver_mobile", response.getString("receiver_mobile"));

                        arggs.putString("house_address",response.getString("house_address"));
                        arggs.putString("city","" );
                        arggs.putString("state", "");
                        ((MainActivity) getActivity()).onBackPressed();
                    }else{
                        Toast.makeText(getActivity(), "Please enter or select all fields correctly...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Method to make json object request where json response starts wtih
     */

    private void makeEditAddressRequest(String user_id, final String getLocation_id, final String getpin, final String gethouse,
                                        String getname, String getphone, String getlandmark, final String getcity,
                                        final String getstate, final String gethouseno, String getsocity) {



    // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("location_id", getLocation_id);
        params.put("pincode", getpin);
        params.put("socity_id", getsocity);
        params.put("house_address", gethouse);
        params.put("receiver_name", getname);
        params.put("receiver_mobile", getphone);
        params.put("landmark", getlandmark);
        params.put("city", " ");
        params.put("state", " ");
        params.put("house_no", gethouseno);

        params.put("latitude",Flatitude);
        params.put("longitude",Flongitude);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Submitting Address...");
        dialog.show();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDIT_ADDRESS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.e("edit_address",response.toString());
                dialog.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        getlocation_id = getArguments().getString("location_id");
                        String get_name = getArguments().getString("name");
                        String get_phone = getArguments().getString("mobile");
                        String getpin = getArguments().getString("pincode");
                        String gethouseno = getArguments().getString("house");

                        getArguments().putString("location_id", getLocation_id);
                        getArguments().putString("pincode", getpin);
                        getArguments().putString("house_no", gethouseno);
                        getArguments().putString("receiver_name", get_name);
                        getArguments().putString("receiver_mobile", get_phone);

                        getArguments().putString("house_address", gethouse);
                        getArguments().putString("city", getcity);
                        getArguments().putString("state", getstate);

                        ((MainActivity) getActivity()).onBackPressed();
                    }else{
                        Toast.makeText(getActivity(), "Please enter or select all fields correctly...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("editAdd_error",error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}

