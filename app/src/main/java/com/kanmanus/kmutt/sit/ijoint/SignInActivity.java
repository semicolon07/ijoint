package com.kanmanus.kmutt.sit.ijoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kanmanus.kmutt.sit.ijoint.models.SignInResponse;
import com.kanmanus.kmutt.sit.ijoint.net.HttpManager;

import java.io.IOException;

import retrofit2.Call;


public class SignInActivity extends Activity {

    private SharedPreferences sharedPref;
    private static final String MYPREF = "ijoint_pref";

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        String DB_PATH;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            DB_PATH = getApplicationContext().getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
//        }
//        else {
//            DB_PATH = getApplicationContext().getFilesDir().getPath() + getApplicationContext().getPackageName() + "/databases/";
//        }
//        try {
//            MySQLiteHelper.writeToSD(DB_PATH);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        sharedPref = getSharedPreferences(MYPREF, 0);
        String patientId = sharedPref.getString("patientId", null);
        String patientFirstName = sharedPref.getString("patientFirstName", null);
        String patientLastName = sharedPref.getString("patientLastName", null);

        if (patientId != null){
            Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn(View v){
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);

        if (haveNetworkConnection()) {
            new ChkAuth().execute();
        }
        else{
            Toast.makeText(getApplicationContext(), "The Internet connection is required for logging in.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }

    private class ChkAuth extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            signIn();

            return "Executed";
        }

        @Override
        protected void onPreExecute() { }

        @Override
        protected void onPostExecute(String result) { }

        @Override
        protected void onProgressUpdate(Void... values) { }

        public void signIn(){
            Call<SignInResponse> call = HttpManager.getInstance().getService().signIn(username.getText().toString(),password.getText().toString());
            SignInResponse response = null;
            try {
                response = call.execute().body();

                if (response.isStatus()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("patientId", response.getPid());
                    editor.putString("patientFirstName", response.getFirstName());
                    editor.putString("patientLastName", response.getLastName());
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Username/Password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
