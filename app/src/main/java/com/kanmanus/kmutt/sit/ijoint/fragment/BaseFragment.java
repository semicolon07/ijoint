package com.kanmanus.kmutt.sit.ijoint.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.fix.Navigator;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Semicolon07 on 7/7/2016 AD.
 */

public class BaseFragment extends Fragment {
    private Snackbar snackbar;
    protected Navigator navigator;

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = Navigator.getInstance();
    }

    protected void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showError(Throwable e){
        //showToast("Error instance ="+e.getClass().getName());
        showToast(String.format(getString(R.string.error),e.getMessage()));
    }
    protected void showRetryConnect(Throwable e, View.OnClickListener onRetryClickListener){
        if(e instanceof ConnectException || e instanceof SocketTimeoutException){
            snackbar = Snackbar
                    .make(getView(), getString(R.string.cannot_connect_server), Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", onRetryClickListener);

            snackbar.show();
        }
    }
    protected void removeRetryConnectView(){
        if(snackbar!=null && snackbar.isShown())
            snackbar.dismiss();
    }
}
