package com.kanmanus.kmutt.sit.ijoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kanmanus.kmutt.sit.ijoint.db.ResultItemDataSource;
import com.kanmanus.kmutt.sit.ijoint.db.TaskDataSource;
import com.kanmanus.kmutt.sit.ijoint.models.ResultItem;
import com.kanmanus.kmutt.sit.ijoint.models.Task;
import com.kanmanus.kmutt.sit.ijoint.net.HttpManager;
import com.kanmanus.kmutt.sit.ijoint.sensor.Orientation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class PerformABFActivity extends Activity implements Orientation.Listener {

    private TaskDataSource taskDataSource;
    private ResultItemDataSource resultItemDataSource;
    private ArrayList<ResultItem> resultItems;

    private Orientation mOrientation;
    private TextView tvSide, tvTargetAngle, tvNumberOfRound, tvTime, tvAngle;
    private LinearLayout uploadingLayout;
    private DecimalFormat df;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;
    public static final int S_BEEP = R.raw.s_beep;

    private String tid, side, targetAngle, numberOfRound, calibratedAngle;

    private boolean isRecording = false;
    private long begin;
    private String performDateTime;

    private boolean isUp = true;
    private int score = 0;
    private String exercise_type;
    private String azimuthAngle;
    private String pitchAngle;
    private String rollAngle;
    private String isABF;
    private String YES = "y";
    private String NO = "n";
    private String LEFT = "l";
    private String magneticRoll;
    private String EXERCISE_ERROR = "n";
    private String EXERCISE_STATE = "1";
    private String EXERCISE_START = "1";
    private String EXERCISE_TARGET_80 = "2";
    private String EXERCISE_SUCCESS = "3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform);

        Intent intent = getIntent();
        tid = intent.getStringExtra("tid");
        String date = intent.getStringExtra("date");
        side = intent.getStringExtra("side");
        targetAngle = intent.getStringExtra("target_angle");
        numberOfRound = intent.getStringExtra("number_of_round");
        calibratedAngle = intent.getStringExtra("calibrated_angle");
        exercise_type = intent.getStringExtra("exercise_type");
        azimuthAngle = intent.getStringExtra("azimuthAngle");
        pitchAngle = intent.getStringExtra("pitchAngle");
        rollAngle = intent.getStringExtra("rollAngle");
        isABF = intent.getStringExtra("isABF");
        magneticRoll = intent.getStringExtra("magneticRoll");
        mOrientation = new Orientation((SensorManager) getSystemService(Activity.SENSOR_SERVICE),
                getWindow().getWindowManager());

        TextView tvHeadLine = (TextView) findViewById(R.id.headline);
        String title = "Perform Task";
        if(isABF.equals(YES)){
            title = "Perform ABF Task";
        }
        tvHeadLine.setText(title);

        tvSide = (TextView) findViewById(R.id.side_value);
        tvTargetAngle = (TextView) findViewById(R.id.target_angle_value);
        tvNumberOfRound = (TextView) findViewById(R.id.number_of_round_value);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAngle = (TextView) findViewById(R.id.tv_angle);

        uploadingLayout = (LinearLayout) findViewById(R.id.uploadingLayout);

        tvSide.setText((side.equals("l")?"Left":"Right"));
        tvTargetAngle.setText(targetAngle + "°");
        tvNumberOfRound.setText(score + "/" + numberOfRound);

        df = new DecimalFormat("0.00");

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>(1);
        soundPoolMap.put( S_BEEP, soundPool.load(getApplicationContext(), S_BEEP, 1) );

        taskDataSource = new TaskDataSource(getApplicationContext());
        taskDataSource.open();

        resultItemDataSource = new ResultItemDataSource(getApplicationContext());
        resultItemDataSource.open();

        resultItems = new ArrayList<ResultItem>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientation.startListening(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientation.stopListening();
    }

    @Override
    public void onOrientationChanged(float azimuth, float pitch, float roll,float magRoll) {
        double angle = 0;
        double errorAngle = 0;
        double angleTv = 0;
        //Log.d("Orientation","azimuth:"+azimuth+",pitch:"+pitch+",roll:"+roll) ;
        if(Task.EXTENSION.equals(exercise_type)){
            errorAngle = pitch - Double.parseDouble(pitchAngle);
            angle = azimuth - Double.parseDouble(calibratedAngle);
            angleTv = azimuth - Double.parseDouble(azimuthAngle);
            if (!side.equals(LEFT)) {
                angle = Double.parseDouble(calibratedAngle) - azimuth;
                angleTv = Double.parseDouble(azimuthAngle) - azimuth;
            }
        }
        else if(Task.HORIZONTAL.equals(exercise_type)){
            errorAngle = azimuth - Double.parseDouble(azimuthAngle);
            angle = pitch - Double.parseDouble(calibratedAngle);
            angleTv = pitch - Double.parseDouble(pitchAngle);
        }

        else if(Task.FLEXION.equals(exercise_type)){
            errorAngle = magRoll - Double.parseDouble(magneticRoll);
//            if(surfaceType == Surface.ROTATION_0){
//                roll = 180 - roll;
//            }else if(surfaceType == Surface.ROTATION_90){
//                roll = 180 + roll;
//            }
            angle = pitch - Double.parseDouble(calibratedAngle);
            angleTv = pitch - Double.parseDouble(pitchAngle);
        }


        String azimuthStr = "" + df.format(azimuth);
        String pitchStr = "" + df.format(pitch);
        String rollStr = "" + df.format(roll);
        String rawAngleStr = "" + df.format(angle);


        String angleStr = df.format(angle);
        //tvAngle.setText(angleStr+"°");
        tvAngle.setText(df.format(angleTv)+ "°");
        //Log.d("errorAngle",""+errorAngle);
        if (isRecording) {
            long current = System.currentTimeMillis();
            String time = "" + (current - begin);

            Date date = new Date(current-begin);
            DateFormat formatter = new SimpleDateFormat("mm:ss");
            String dateFormatted = formatter.format(date);

            tvTime.setText(dateFormatted);

            // store tid / time / angle into result item
            ResultItem resultItem = resultItemDataSource.create(tid, time, angleStr, rawAngleStr, azimuthStr, pitchStr, rollStr);
            resultItems.add(resultItem);
            Log.d("errorAngle",""+errorAngle);
            if(checkExerciseType(errorAngle) ){
                EXERCISE_ERROR = NO;
                if (EXERCISE_STATE.equals(EXERCISE_START) && Double.parseDouble(angleStr) > Double.parseDouble(targetAngle)*0.8){ // 80 percent of target
                    if(isABF.equals(YES)){
                        soundPool.play(soundPoolMap.get(S_BEEP), 0.6f, 0.6f, 1, 0, 1f);
                    }
                    EXERCISE_STATE = EXERCISE_TARGET_80;
                }
                if (EXERCISE_STATE.equals(EXERCISE_TARGET_80) && Double.parseDouble(angleStr) > Double.parseDouble(targetAngle)){
                    if(isABF.equals(YES)){
                        soundPool.play(soundPoolMap.get(S_BEEP), 0.6f, 0.6f, 1, 0, 1f);
                    }
                    score++;
                    EXERCISE_STATE = EXERCISE_SUCCESS;
                    tvNumberOfRound.setText(score + "/" + numberOfRound);
                }
                if (EXERCISE_STATE.equals(EXERCISE_SUCCESS) && Double.parseDouble(angleStr) < 5) {
                    if(isABF.equals(YES)){
                        soundPool.play(soundPoolMap.get(S_BEEP), 0.6f, 0.6f, 1, 0, 1f);
                    }
                    if(numberOfRound.equals(String.valueOf(score))){
                        isRecording = false;
                        tvAngle.setVisibility(View.GONE);
                        uploadingLayout.setVisibility(View.VISIBLE);

                        taskDataSource.updateIsSynced(tid, "f");
                        taskDataSource.updatePerformDateTime(tid, performDateTime);

                        new UploadToWeb().execute();
                    }
                    EXERCISE_STATE = EXERCISE_START;
                }
            }else{
                if(isABF.equals(YES) && EXERCISE_ERROR.equals(NO)){
                    soundPool.play(soundPoolMap.get(S_BEEP), 0.6f, 0.6f, 1, 0, 1f);
                    EXERCISE_ERROR = YES;
                }
            }
        }
    }

    public boolean checkExerciseType( double angleCheck){
        boolean chk = true;
        if(Task.FLEXION.equals(exercise_type)){
            if(angleCheck <  - 3 || angleCheck > + 3){
                chk = false;
            }
        }else{
            if(angleCheck <  - 9 || angleCheck > + 9){// error exercise type for input error 5 percent of around (360 degree)
                chk = false;
            }
        }
        return chk;
    }
    public void startExercise(View v){
        Button btn = (Button) v;

        if (!isRecording){  // Start Exercise
            Date cDate = new Date();
            performDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
            EXERCISE_STATE = EXERCISE_START;
            isRecording = true;
            btn.setBackgroundResource(R.drawable.btn_stop);
            btn.setText("Stop Exercise");

            begin = System.currentTimeMillis();
        }
        else{   // Stop Exercise
            isRecording = false;
            btn.setVisibility(View.GONE);
            tvAngle.setVisibility(View.GONE);
            uploadingLayout.setVisibility(View.VISIBLE);

            taskDataSource.updateIsSynced(tid, "f");
            taskDataSource.updatePerformDateTime(tid, performDateTime);

            new UploadToWeb().execute();
        }
    }

    private class UploadToWeb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (haveNetworkConnection())
                saveToWebDB();

            return "Executed";
        }

        @Override
        protected void onPreExecute() { }

        @Override
        protected void onPostExecute(String result) {
            if (haveNetworkConnection())
                Toast.makeText(getApplicationContext(), "Your data is saved and sycned to the web site.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "No Internet Connection. Your data is saved but it has not been synced to the web site yet.", Toast.LENGTH_SHORT).show();

            goBackToHomePage();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        public void saveToWebDB(){
            JSONArray resultJSONArray = new JSONArray();

            Iterator<ResultItem> iter = resultItems.iterator();
            while (iter.hasNext()){
                ResultItem resultItem = iter.next();
                resultJSONArray.put(resultItem.getJSONObject());
            }

            JSONObject json = new JSONObject();

            try {
                json.put("score", score);
                json.put("perform_datetime", performDateTime);
                json.put("result", resultJSONArray);
                HttpManager.getInstance().getService().uploadResultItems(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void goBackToHomePage(){
        Intent i = new Intent(getApplicationContext(), TasksActivity.class);
        startActivity(i);
        finish();
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }
}
