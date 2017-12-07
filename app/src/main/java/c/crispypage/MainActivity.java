package c.crispypage;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static c.crispypage.Configuration.ADD_USER_URL;
import static c.crispypage.Configuration.KEY_ACTION;
import static c.crispypage.Configuration.KEY_ADDRESS;
import static c.crispypage.Configuration.KEY_CONTACT;
import static c.crispypage.Configuration.KEY_COPIES;
import static c.crispypage.Configuration.KEY_EMAIL;
import static c.crispypage.Configuration.KEY_NAME;
import static c.crispypage.Configuration.KEY_SUGGEST;
import static c.crispypage.Configuration.KEY_TIME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCopies;
    private EditText editTextAddress;
    private EditText editTextContact;
    private EditText editTextSuggest;
    private Button buttonUpload, buttonAddDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextName = (EditText) findViewById(R.id.name);
        editTextCopies = (EditText) findViewById(R.id.copy);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextContact = (EditText) findViewById(R.id.contact);
        editTextSuggest = (EditText) findViewById(R.id.suggest);

        buttonUpload = (Button) findViewById(R.id.submit);
        buttonAddDoc = (Button) findViewById(R.id.select);

        buttonUpload.setOnClickListener(this);
        buttonAddDoc.setOnClickListener(this);
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void Submit() {
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        final String userTime = getCurrentTimeStamp();
        final String userName = editTextName.getText().toString().trim();
        final String userEmail = editTextEmail.getText().toString().trim();
        final String userCopies = editTextCopies.getText().toString().trim();
        final String userAddress = editTextAddress.getText().toString().trim();
        final String userContact = editTextContact.getText().toString().trim();
        final String userSuggest = editTextSuggest.getText().toString().trim();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,ADD_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ACTION,"insert");
                params.put(KEY_TIME,userTime);
                params.put(KEY_NAME,userName);
                params.put(KEY_EMAIL,userEmail);
                params.put(KEY_COPIES,userCopies);
                params.put(KEY_ADDRESS,userAddress);
                params.put(KEY_CONTACT,userContact);
                params.put(KEY_SUGGEST,userSuggest);

                return params;
            }

        };

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpload){
            Submit();
        }
        if(v == buttonAddDoc){
           // showFileChooser();
        }

    }
}
