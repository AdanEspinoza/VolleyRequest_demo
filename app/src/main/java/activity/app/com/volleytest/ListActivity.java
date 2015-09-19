package activity.app.com.volleytest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ListActivity extends ActionBarActivity implements Animation.AnimationListener{
    TextView txtResponse;
    RequestQueue queue;
    String urlFill = "https://fathomless-forest-6070.herokuapp.com/user_infos.json";
    String urlDelete = "https://fathomless-forest-6070.herokuapp.com/user_infos/";
    String urlSearch = "https://fathomless-forest-6070.herokuapp.com/user_infos/search";
    String urlFind = "https://fathomless-forest-6070.herokuapp.com/user_infos/findUsers";

    ArrayList<Contacto> contactos;
    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "firstName";
    private static final String TAG_LAST = "lastName";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_BIRTHDAY = "birthdate";
    private static final String TAG_URL = "url";
    private static final String NAME_TAG = "user_info[firstName]";
    ListView listContacts;
    MyArrayAdapter myAdapter;
    Contacto contact;

    private String SEARCH_OPENED = "SEARCH_OPENED";
    private String SEARCH_QUERY="SEARCH_QUERY";
    private boolean mSearchOpened;
    private String mSearchQuery;
    private EditText mSearchEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        contactos = new ArrayList<>();
        txtResponse = (TextView)findViewById(R.id.txtResponse);

        if (savedInstanceState == null) {
            mSearchOpened = false;
            mSearchQuery = "";
        } else {
            mSearchOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            mSearchQuery = savedInstanceState.getString(SEARCH_QUERY);
        }
        if (mSearchOpened) {
            openSearchBar(mSearchQuery);
        }

        fillList();
        onDeleteSwipe();


    }

    public void fillList(){
        queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(urlFill,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            //txtResponse.setText(response.toString());
                            Contacto oneContact;
                            for (int i = 0; i < response.length(); i++) {
                                oneContact = new Contacto();
                                JSONObject c = (JSONObject) response.get(i);
                                //JSONObject c = response.getJSONObject(i);
                                oneContact.setId(c.getString(TAG_ID));
                                oneContact.setFirstName(c.getString(TAG_NAME));
                                oneContact.setLastName(c.getString(TAG_LAST));
                                oneContact.setGender(c.getString(TAG_GENDER));
                                oneContact.setWeight(c.getString(TAG_WEIGHT));
                                oneContact.setHeight(c.getString(TAG_HEIGHT));
                                oneContact.setBirthday(c.getString(TAG_BIRTHDAY));
                                oneContact.setUrl(c.getString(TAG_URL));
                                contactos.add(oneContact);
                            }
                            if (contactos.size() > 0) {
                                listContacts = (ListView) findViewById(R.id.listContacts);
                                myAdapter = new MyArrayAdapter(ListActivity.this, android.R.layout.simple_list_item_1, contactos);
                                listContacts.setAdapter(myAdapter);
                            }
                        }catch (JSONException j){}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtResponse.setText("Error");
            }
        });
        queue.add(jsObjRequest);
    }

    public void deleteRequest(String id){
        queue = Volley.newRequestQueue(this);
        urlDelete = urlDelete+id;
        Toast.makeText(ListActivity.this,"Deleting id "+urlDelete,Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsObjRequest = new  JsonObjectRequest(Request.Method.DELETE,urlDelete,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ListActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }


                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(jsObjRequest);
    }


    public void onDeleteSwipe(){
        if(listContacts==null) listContacts = (ListView) findViewById(R.id.listContacts);
        final SwipeDetector swipeDetector = new SwipeDetector();
        listContacts.setOnTouchListener(swipeDetector);
        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                        contact = contactos.get(position);
                        Toast.makeText(ListActivity.this, "Deleting id " + contact.getId(), Toast.LENGTH_SHORT).show();
                        Animation swipe = AnimationUtils.loadAnimation(ListActivity.this, R.anim.swipe);
                        view.startAnimation(swipe);
                        swipe.setAnimationListener(ListActivity.this);
                    }
                }
            }
        });
    }

    public void onSearchJsonPOST(String nameTOsearch){
        try {
            JSONObject obj = new JSONObject();
            obj.put(NAME_TAG, nameTOsearch);
            queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, urlFind, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ListActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            queue.add(jsObjRequest);
        }catch (JSONException j){}
    }

    public void onSearchRequest(final String nameTOsearch){
        queue = Volley.newRequestQueue(this);
        Toast.makeText(ListActivity.this, "Search: "+nameTOsearch, Toast.LENGTH_SHORT).show();
        StringRequest sr = new StringRequest(Request.Method.POST,urlFind, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(ListActivity.this, "Arrojo: "+response.length(), Toast.LENGTH_SHORT).show();
                Contacto oneContact;
                oneContact = new Contacto();
                //JSONObject c = (JSONObject) response;
                //JSONObject c = response.getJSONObject(i);
//                oneContact.setId(c.getString(TAG_ID));
//                oneContact.setFirstName(c.getString(TAG_NAME));
//                oneContact.setLastName(c.getString(TAG_LAST));
//                oneContact.setGender(c.getString(TAG_GENDER));
//                oneContact.setWeight(c.getString(TAG_WEIGHT));
//                oneContact.setHeight(c.getString(TAG_HEIGHT));
//                oneContact.setBirthday(c.getString(TAG_BIRTHDAY));
//                oneContact.setUrl(c.getString(TAG_URL));
//                contactos.add(oneContact);

//                if (contactos.size() > 0) {
//                    listContacts = (ListView) findViewById(R.id.listContacts);
//                    myAdapter = new MyArrayAdapter(ListActivity.this, android.R.layout.simple_list_item_1, contactos);
//                    listContacts.setAdapter(myAdapter);
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(NAME_TAG, nameTOsearch);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SEARCH_OPENED, mSearchOpened);
        outState.putString(SEARCH_QUERY, mSearchQuery);
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onCreateContact();
                return true;
            case R.id.action_search:
                if (mSearchOpened) {
                    closeSearchBar();
                } else {
                    openSearchBar(mSearchQuery);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreateContact(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        myAdapter.remove(contact);
        deleteRequest(contact.getId());
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }



    private void closeSearchBar() {
        getSupportActionBar().setDisplayShowCustomEnabled(false);

        mSearchOpened = false;

    }

    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search);

        // Search edit text field setup.
        mSearchEt = (EditText) actionBar.getCustomView().findViewById(R.id.etSearch);
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(ListActivity.this, "Closing", Toast.LENGTH_SHORT).show();
                //onSearchRequest(mSearchEt.getText().toString());
                onSearchJsonPOST(mSearchEt.getText().toString());

            }
        });


        mSearchEt.setText(queryText);
        mSearchEt.requestFocus();
        mSearchOpened = true;

    }
}
