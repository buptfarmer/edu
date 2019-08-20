package com.chensiwen.edugame.job;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

/**
 * @author chencheng
 * @since 2018/11/2
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    public MyJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("CCCC", "onStartJob() called with: params = [" + params + "]");
        // UtilityMethods.showToast(this,params.getExtras().getString("json"));
        Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("CCCC", "onStopJob() called with: params = [" + params + "]");
        return false;
    }
}
