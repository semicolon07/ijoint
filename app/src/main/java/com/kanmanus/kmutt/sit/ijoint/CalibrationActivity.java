package com.kanmanus.kmutt.sit.ijoint;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kanmanus.kmutt.sit.ijoint.models.ResultItem;
import com.kanmanus.kmutt.sit.ijoint.sensor.Orientation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class CalibrationActivity extends Activity implements Orientation.Listener {

    private Orientation mOrientation;

    private ProgressBar progressBar;
    private TextView tvTime, tvAngle;
    private Button btnNext;

    private DecimalFormat df;

    private String tid, date, side, targetAngle, numberOfRound, calibratedAngle, isABF;
    private int exerciseNo = 0;
    private static int FLEXION = 0, EXTENSION = 1, H_FLEXION = 2;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;
    public static final int L_BEEP = R.raw.l_beep;

    private boolean isRecording = false;
    private boolean isFinish = false;

    private ArrayList<ResultItem> resultItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        mOrientation = new Orientation((SensorManager) getSystemService(Activity.SENSOR_SERVICE),
                getWindow().getWindowManager());

        Intent intent = getIntent();
        tid = intent.getStringExtra("tid");
        date = intent.getStringExtra("date");
        side = intent.getStringExtra("side");
        targetAngle = intent.getStringExtra("target_angle");
        numberOfRound = intent.getStringExtra("number_of_round");
        isABF = intent.getStringExtra("is_abf");

        df = new DecimalFormat("0.00");

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAngle = (TextView) findViewById(R.id.tv_angle);
        btnNext = (Button) findViewById(R.id.btn_next);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>(1);
        soundPoolMap.put( L_BEEP, soundPool.load(getApplicationContext(), L_BEEP, 1) );

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
    public void onOrientationChanged(float azimuth, float pitch, float roll) {
        if (!isFinish){
            float angle = roll;

            if (side.equals("l"))
                angle = -angle;

            tvAngle.setText(df.format(angle) + "°");

            if (isRecording) {
                // store tid / time / angle into result item
                ResultItem resultItem = new ResultItem(tid, df.format(angle));
                resultItems.add(resultItem);
            }
        }
    }

    public void startCalibration(View v) {
        Button btn = (Button) v;
        btn.setVisibility(View.GONE);

        new CalibrationTimer(5000, 1000).start();
    }

    class CalibrationTimer {
        private long millisInFuture;
        private long countDownInterval;

        public CalibrationTimer(long pMillisInFuture, long pCountDownInterval) {
            this.millisInFuture = pMillisInFuture;
            this.countDownInterval = pCountDownInterval;
        }

        public void start() {
            isRecording = true;

            final Handler handler = new Handler();
            final Runnable counter = new Runnable(){

                public void run() {
                    if(millisInFuture <= 0) {
                        soundPool.play(soundPoolMap.get(L_BEEP), 0.6f, 0.6f, 1, 0, 1f);

                        tvTime.setText("");
                        tvAngle.setText("Complete");
                        isRecording = false;
                        isFinish = true;

                        btnNext.setVisibility(View.VISIBLE);

                        double sum = 0;
                        Iterator<ResultItem> iter = resultItems.iterator();
                        while (iter.hasNext()){
                            ResultItem item = iter.next();

                            sum += Double.parseDouble(item.angle);
                        }

                        calibratedAngle = "" + (sum/resultItems.size());

                        Toast.makeText(getApplicationContext(), "The calibrated angle is " + calibratedAngle, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int x = (int) (millisInFuture-1000) / 1000;
                        progressBar.setProgress(x*20);

                        tvTime.setText("00:0" + (x));

                        millisInFuture -= countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                    }
                }
            };

            handler.postDelayed(counter, countDownInterval);
        }
    }

    public void next(View v){
        Class targetClass = PerformActivity.class;

        if (isABF.equals("y"))
            targetClass = PerformABFActivity.class;

        Intent i = new Intent(getApplicationContext(), targetClass);
        i.putExtra("tid", tid);
        i.putExtra("date", date);
        i.putExtra("side", side);
        i.putExtra("target_angle", targetAngle);
        i.putExtra("number_of_round", numberOfRound);
        i.putExtra("calibrated_angle", calibratedAngle);
        startActivity(i);
        finish();
    }
}
