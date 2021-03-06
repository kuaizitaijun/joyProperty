package com.joyhome.nacity.app.util;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.joy.property.R;


/**
 * Created by Admin on 2015-01-10
 */

public class CustomDialogFragment extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View mRootView =  inflater.inflate(R.layout.fragment_dialog, container, false);
//        ProgressBar  mDotsProgressBar = (ProgressBar) mRootView.findViewById(R.id.dotsProgressBar);
        return mRootView ;

    }


    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @NonNull
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.fd_back_color);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.y = 100;
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager == null) return;
         super.show(manager, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {

        return transaction.commitAllowingStateLoss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        if ( getFragmentManager()== null) {
            return;
        }

        super.dismissAllowingStateLoss();
    }

    @Override
    public void dismiss() {
        if ( getFragmentManager()== null) {
            return;
        }

        super.dismiss();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}




