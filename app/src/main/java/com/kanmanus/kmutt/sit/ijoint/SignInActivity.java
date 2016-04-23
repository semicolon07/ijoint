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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
            String url = "http://www.kanmanus.com/ijoint/app/sign_in.php";

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));

            try {
                JSONObject jObject = new JSONObject(Function.jsonParse(url, nameValuePairs));

                boolean isPassed = jObject.getBoolean("status");
                String pid = jObject.getString("pid");
                String firstName = jObject.getString("pFirstName");
                String lastName = jObject.getString("pLastName");

                if (isPassed) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("patientId", pid);
                    editor.putString("patientFirstName", firstName);
                    editor.putString("patientLastName", lastName);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Username/Password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
