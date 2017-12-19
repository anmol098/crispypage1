package c.crispypage.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import c.crispypage.R;
import c.crispypage.activity.Configuration;
import c.crispypage.activity.Constants;
import c.crispypage.activity.MainActivity;
import c.crispypage.activity.Upload;
import android.widget.ArrayAdapter;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import static android.app.Activity.RESULT_OK;

/**
 * Created by kjaganmohan on 08/12/17.
 */

public class Home extends Fragment  {


    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCopies;
    private EditText editTextAddress;
    private EditText editTextContact;
    private EditText editTextSuggest;
     public Button buttonUpload, buttonAddDoc;
    //this is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    String fileTextUrl;
    private Activity rootView;


    //the firebase objects for storage and database
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    TextView textViewStatus;
    ProgressBar progressBar;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String[] BINDING = {"Spiral Binding", "Soft Binding", "Strip File", "Folder", "Lamination"};
    String[] ORIENTATION={"One Sided","Both Sided"};
    String[] CHOICE = {"Black & White","Colored"};
    String[] AREA = {"Nungambakkam","Kilpauk","Kattankulathur","Potheri"};



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        editTextEmail = (EditText) view.findViewById(R.id.email);
        editTextName = (EditText) view.findViewById(R.id.name);
        editTextCopies = (EditText) view.findViewById(R.id.copy);
        editTextAddress = (EditText) view.findViewById(R.id.address);
        editTextContact = (EditText) view.findViewById(R.id.contact);
        editTextSuggest = (EditText) view.findViewById(R.id.suggest);

        buttonUpload = (Button) view.findViewById(R.id.submit);
        buttonAddDoc = (Button) view.findViewById(R.id.select);

        /*buttonUpload.setOnClickListener(this);
        buttonAddDoc.setOnClickListener(this);*/

        //getting firebase objects
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);


        ArrayAdapter<String> bindingAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, BINDING);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                view.findViewById(R.id.Binding_Spinner);
        materialDesignSpinner.setAdapter(bindingAdapter);
        materialDesignSpinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), "Selected: " + position, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        ArrayAdapter<String> orientationAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, ORIENTATION);
        MaterialBetterSpinner materialDesignSpinner1 = (MaterialBetterSpinner)
                view.findViewById(R.id.Orientation_Spinner);
        materialDesignSpinner1.setAdapter(orientationAdapter);
        materialDesignSpinner1.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), "Selected: " + position, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, CHOICE);
        MaterialBetterSpinner materialDesignSpinner2 = (MaterialBetterSpinner)
                view.findViewById(R.id.Choice_Spinner);
        materialDesignSpinner2.setAdapter(choiceAdapter);
        materialDesignSpinner2.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), "Selected: " + position, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, AREA);
        MaterialBetterSpinner materialDesignSpinner3 = (MaterialBetterSpinner)
                view.findViewById(R.id.Area_Spinner);
        materialDesignSpinner3.setAdapter(areaAdapter);
        materialDesignSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), "Selected: " + position, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        buttonUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Submit();
            }
        });
        buttonAddDoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                verifyStoragePermissions(getActivity());
                getPDF();
            }
        });


        return view;

    }


    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user\
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE


            );
        }
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
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            } else {
                Toast.makeText(getActivity(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static String getOrderId(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMHHmmss");
        String currentDateTime = dateFormat.format(new Date()); // Find todays date
        String id="CP_"+currentDateTime;

        return id;
    }
    private void uploadFile(Uri data) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");

                        Upload upload = new Upload(getOrderId(), taskSnapshot.getDownloadUrl().toString());
                        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                        fileTextUrl = taskSnapshot.getDownloadUrl().toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        textViewStatus.setText((int) progress + "% Uploading...");
                    }
                });

    }

    private void Submit() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Please wait...",false,false);
        final String orderID = getOrderId();
        final String userTime = getCurrentTimeStamp();
        final String userName = editTextName.getText().toString().trim();
        final String userEmail = editTextEmail.getText().toString().trim();
        final String userCopies = editTextCopies.getText().toString().trim();
        final String userAddress = editTextAddress.getText().toString().trim();
        final String userContact = editTextContact.getText().toString().trim();
        final String userSuggest = editTextSuggest.getText().toString().trim();
        final String fileUrl = fileTextUrl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.ADD_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(Configuration.KEY_ACTION,"insert");
                params.put(Configuration.KEY_ID,orderID);
                params.put(Configuration.KEY_TIME,userTime);
                params.put(Configuration.KEY_NAME,userName);
                params.put(Configuration.KEY_EMAIL,userEmail);
                params.put(Configuration.KEY_COPIES,userCopies);
                params.put(Configuration.KEY_ADDRESS,userAddress);
                params.put(Configuration.KEY_CONTACT,userContact);
                params.put(Configuration.KEY_SUGGEST,userSuggest);
                params.put(Configuration.KEY_URL,fileUrl);

                return params;
            }

        };

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }
    /*@Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.submit:
                Submit();
                break;
            case R.id.select:
                verifyStoragePermissions(getActivity());
                getPDF();
                break;

        }
}*/
}
