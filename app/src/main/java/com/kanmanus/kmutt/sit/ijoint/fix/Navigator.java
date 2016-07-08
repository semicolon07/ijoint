package com.kanmanus.kmutt.sit.ijoint.fix;

import android.content.Context;
import android.content.Intent;

import com.kanmanus.kmutt.sit.ijoint.activity.CalibrationActivity;
import com.kanmanus.kmutt.sit.ijoint.activity.MainActivity;
import com.kanmanus.kmutt.sit.ijoint.activity.SignInActivity;
import com.kanmanus.kmutt.sit.ijoint.activity.TasksActivity;
import com.kanmanus.kmutt.sit.ijoint.models.Task;
import com.kanmanus.kmutt.sit.ijoint.models.TreatmentModel;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class Navigator {
    private static Navigator INSTANCE = new Navigator();
    private Navigator(){

    }
    public static Navigator getInstance(){
        return INSTANCE;
    }

    public void navigateToTasks(Context context, TreatmentModel treatmentModel){
        Intent intent = TasksActivity.callingIntent(context,treatmentModel);
        context.startActivity(intent);
    }

    public void navigateToLogin(Context context) {
        Intent intent = SignInActivity.callingIntent(context);
        context.startActivity(intent);
    }
    public void navigateToMain(Context context){
        Intent intent = MainActivity.callingIntent(context);
        context.startActivity(intent);
    }

    public void navigateToCalibration(Context context, Task task) {
        Intent intent = CalibrationActivity.callingIntent(context,task);
        context.startActivity(intent);
    }
}
