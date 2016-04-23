package com.kanmanus.kmutt.sit.ijoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kanmanus.kmutt.sit.ijoint.db.ResultItemDataSource;
import com.kanmanus.kmutt.sit.ijoint.db.TaskDataSource;
import com.kanmanus.kmutt.sit.ijoint.models.ResultItem;
import com.kanmanus.kmutt.sit.ijoint.models.Task;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class TasksActivity extends Activity {

    private SharedPreferences sharedPref;
    private static final String MYPREF = "ijoint_pref";

    private String patientId;

    private LinearLayout loadingLayout;
    private ListView tasksListView;

    private ArrayList<Task> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        sharedPref = getSharedPreferences(MYPREF, 0);
        patientId = sharedPref.getString("patientId", null);
        String patientFirstName = sharedPref.getString("patientFirstName", null);
        String patientLastName = sharedPref.getString("patientLastName", null);

        setTitle(patientFirstName + " " + patientLastName);

        loadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        tasksListView = (ListView) findViewById(R.id.lv_tasks);

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = allTasks.get(i);

                if (task.is_synced.equals("n")) {
                    Intent intent = new Intent(getApplicationContext(), CalibrationActivity.class);
                    intent.putExtra("tid", task.tid);
                    intent.putExtra("date", task.date);
                    intent.putExtra("side", task.side);
                    intent.putExtra("target_angle", task.target_angle);
                    intent.putExtra("number_of_round", task.number_of_round);
                    intent.putExtra("is_abf", task.is_abf);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if (!haveNetworkConnection())
            noInternet();

        new SyncData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            if (haveNetworkConnection()) {
                // synchronize data
                new SyncData().execute();
            } else{
                noInternet();
            }

            return true;
        }
        else if (id == R.id.action_signout){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("patientId", null);
            editor.putString("patientFirstName", null);
            editor.putString("patientLastName", null);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }

    public void noInternet(){
        Toast.makeText(getApplicationContext(), "No Internet Connection. Data cannot be synced to the server.", Toast.LENGTH_SHORT).show();
    }

    private class SyncData extends AsyncTask<String, Void, String> {
        private TaskDataSource taskDataSource;
        private ResultItemDataSource resultItemDataSource;

        @Override
        protected String doInBackground(String... params) {
            taskDataSource = new TaskDataSource(getApplicationContext());
            taskDataSource.open();

            resultItemDataSource = new ResultItemDataSource(getApplicationContext());
            resultItemDataSource.open();

            if (haveNetworkConnection()) {
                uploadTasks();
                downloadTasks();
            }

            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            loadingLayout.setVisibility(View.VISIBLE);
            tasksListView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String result) {
            loadingLayout.setVisibility(View.GONE);
            tasksListView.setVisibility(View.VISIBLE);

            showTask();

            taskDataSource.close();
            resultItemDataSource.close();
        }

        @Override
        protected void onProgressUpdate(Void... values) { }

        public void uploadTasks(){
            // Get all tasks with isSynced = 'f'
            List<Task> finishedTasks = taskDataSource.getFinishedTasks();

            String tasksId = "";
            String performDateTimes = "";
            Iterator<Task> iter = finishedTasks.iterator();
            int i = 1;
            while (iter.hasNext()){
                Task t = iter.next();
                String tid = t.tid;
                String performDateTime = "'" + t.perform_datetime + "'";

                List<ResultItem> resultItemList = resultItemDataSource.getByTid(tid);

                JSONArray resultJSONArray = new JSONArray(Arrays.asList(resultItemList));

                JSONObject json = new JSONObject();

                try {
                    json.put("perform_datetime", performDateTime);
                    json.put("result", resultJSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.kanmanus.com/ijoint/app/upload_result_items.php");

                try {
                    List<NameValuePair> nVP = new ArrayList<NameValuePair>(2);
                    nVP.add(new BasicNameValuePair("json", json.toString()));  //studentJson is the JSON input

                    httpPost.setEntity(new UrlEncodedFormEntity(nVP));
                    client.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tasksId += tid;
                performDateTimes += performDateTime;

                if (i != finishedTasks.size()) {
                    tasksId += ",";
                    performDateTimes += ",";
                }

                taskDataSource.updateIsSynced(tid, "y");
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tid_list", tasksId));
            nameValuePairs.add(new BasicNameValuePair("perform_datetimes", performDateTimes));

            try {
                Function.jsonParse("http://www.kanmanus.com/ijoint/app/update_tasks.php", nameValuePairs);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void downloadTasks(){
            String url = "http://www.kanmanus.com/ijoint/app/get_tasks.php";

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("pid", patientId));

            ArrayList<String> tidList = new ArrayList<String>();

            try {
                JSONArray jArr = new JSONArray(Function.jsonParse(url, nameValuePairs));

                for (int i=0; i<jArr.length(); i++){
                    JSONObject task = jArr.getJSONObject(i);

                    String tid = task.getString("tid");
                    String pid = task.getString("pid");
                    String date = task.getString("date");
                    String side = task.getString("side");
                    String target_angle = task.getString("target_angle");
                    String number_of_round = task.getString("number_of_round");
                    String is_abf = task.getString("is_abf");

                    if (taskDataSource.get(tid) == null){
                        // insert task into sqlite
                        taskDataSource.create(tid, pid, date, side, target_angle, number_of_round, is_abf, "n", "0000-00-00");
                    }
                    else{
                        // update task
                        taskDataSource.edit(tid, pid, date, side, target_angle, number_of_round, is_abf);
                    }

                    tidList.add(tid);
                }

                updateToWeb(tidList);
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void updateToWeb(ArrayList<String> tidList) throws IOException, JSONException {
            String list = "";
            Iterator<String> iter = tidList.iterator();
            int i=1;
            while (iter.hasNext()){
                String s = iter.next();
                list += s;

                if (i != tidList.size())
                    list += ",";

                i++;
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tid_list", list));

            Function.jsonParse("http://www.kanmanus.com/ijoint/app/update_status.php", nameValuePairs);
        }

        public void showTask(){
            allTasks = (ArrayList<Task>) taskDataSource.getAll(patientId);

            tasksListView.setAdapter(new ArrayAdapter<Task>(getApplicationContext(), R.layout.lv_task_ready, allTasks) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = convertView;

                    Task t = getItem(position);

                    LayoutInflater vi = LayoutInflater.from(getContext());
                    if (t.is_synced.equals("n"))
                        v = vi.inflate(R.layout.lv_task_ready, null);
                    else if (t.is_synced.equals("f"))
                        v = vi.inflate(R.layout.lv_task_complete, null);
                    else if (t.is_synced.equals("y"))
                        v = vi.inflate(R.layout.lv_task_synced, null);

                    String side = (t.side.equals("l"))?"Left":"Right";

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date newDate = null;
                    try {
                        newDate = format.parse(t.date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    format = new SimpleDateFormat("dd MMM yyyy");
                    String date = format.format(newDate);

                    if (t != null) {
                        TextView tvExerciseName = (TextView) v.findViewById(R.id.tv_exercise_name);
                        TextView tvDate = (TextView) v.findViewById(R.id.tv_date);
                        TextView tvTargetAngle = (TextView) v.findViewById(R.id.tv_target_angle);
                        TextView tvNumberOfRound = (TextView) v.findViewById(R.id.tv_number_of_round);

                        tvExerciseName.setText(side + " Shoulder Exercise" + ((t.is_abf.equals("y"))?" (ABF)":""));
                        tvDate.setText(date);
                        tvTargetAngle.setText(t.target_angle + "\u00b0");
                        tvNumberOfRound.setText(t.number_of_round);
                    }

                    return v;
                }
            });
        }
    }
}
