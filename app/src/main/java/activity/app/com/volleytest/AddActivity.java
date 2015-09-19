package activity.app.com.volleytest;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class AddActivity extends ActionBarActivity {

    private static final String NAME_TAG = "user_info[firstName]";
    private static final String LAST_NAME_TAG = "user_info[lastName]";
    private static final String GENDER_TAG = "user_info[gender]";
    private static final String WEIGHT_TAG = "user_info[weight]";
    private static final String HEIGHT_TAG = "user_info[height]";
    private static final String BIRTHDAY_TAG_1 = "user_info[birthdate(1i)]";
    private static final String BIRTHDAY_TAG_2 = "user_info[birthdate(2i)]";
    private static final String BIRTHDAY_TAG_3 = "user_info[birthdate(3i)]";

    String url = "https://fathomless-forest-6070.herokuapp.com/user_infos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        onAdd();
    }

    public void onAdd(){
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) findViewById(R.id.txtName);
                TextView last = (TextView) findViewById(R.id.txtLast);
                TextView gender = (TextView) findViewById(R.id.txtGender);
                TextView weight = (TextView) findViewById(R.id.txtWeight);
                TextView height = (TextView) findViewById(R.id.txtHeight);
                TextView birthday = (TextView) findViewById(R.id.txtBirthday);

                Contacto contact = new Contacto();
                contact.setFirstName(name.getText().toString());
                contact.setLastName(last.getText().toString());
                contact.setGender(gender.getText().toString());
                contact.setWeight(weight.getText().toString());
                contact.setHeight(height.getText().toString());
                contact.setBirthday(birthday.getText().toString());
                postNewContact(AddActivity.this,contact);

            }
        });
    }


    public void postNewContact(Context context,final Contacto contact){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(NAME_TAG, contact.getFirstName());
                params.put(LAST_NAME_TAG, contact.getLastName());
                params.put(GENDER_TAG, contact.getGender());
                params.put(WEIGHT_TAG, contact.getWeight());
                params.put(HEIGHT_TAG, contact.getHeight());

                String birthdayMonth="", birthdayDay="", birthdayYear="";
                String [] splitBirthday = contact.getBirthday().split("/");
                if(splitBirthday.length == 3)
                {
                    birthdayMonth = splitBirthday[0];
                    birthdayDay = splitBirthday[1];
                    birthdayYear = splitBirthday[2];
                }

                if(birthdayMonth != null && birthdayDay != null && birthdayYear != null)
                {
                    params.put(BIRTHDAY_TAG_1, birthdayYear);
                    params.put(BIRTHDAY_TAG_2, birthdayMonth);
                    params.put(BIRTHDAY_TAG_3, birthdayDay);
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
