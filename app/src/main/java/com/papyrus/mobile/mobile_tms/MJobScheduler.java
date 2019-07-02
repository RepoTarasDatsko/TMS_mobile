package com.papyrus.mobile.mobile_tms;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class MJobScheduler extends JobService implements LoginCompleted {

    private static final String TAG = MJobScheduler.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        SharedPreferences mPrefs =  getSharedPreferences("Global", 0);

        if(!mPrefs.getBoolean("SaveAllCompleted", true)) {
            String[] myTaskParams = {"SaveAll", mPrefs.getString("NameD", ""), mPrefs.getString("Password", "")};
            new DataLoader(this).execute(myTaskParams);
            jobFinished(jobParameters, false);
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.d(TAG, "onStopJob() was called");
        return false;
    }
    public void onTaskComplete(SoapObject Result) {
        //String spinnerItems = Result.getProperty("Result").toString();

        if(Result != null) {

            String ok = Result.getProperty("OK").toString();
            if (ok.equals("OK"))
            {
                SharedPreferences mPrefs =  getSharedPreferences("Global", 0);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putBoolean("SaveAllCompleted", true);
                prefsEditor.apply();

            }
        }
    }
}


