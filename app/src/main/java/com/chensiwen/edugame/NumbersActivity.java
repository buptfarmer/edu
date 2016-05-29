package com.chensiwen.edugame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Reply;

import java.util.List;

public class NumbersActivity extends BaseAppCompatActivity {
    private static final String TAG = "NumbersActivity";
    private FeedbackAgent mAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(getApplicationContext(), StatsConstants.NUMBERS_CLICK_FAT);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initData();
    }

    private void initData() {
        // fix gradle application id & pkg name caused crash: http://bbs.umeng.com/thread-8071-1-1.html
        com.umeng.fb.util.Res.setPackageName(R.class.getPackage().getName());
        mAgent = new FeedbackAgent(this);
        mAgent.getDefaultConversation().sync(new SyncListener() {
            @Override
            public void onReceiveDevReply(List<Reply> list) {
            }

            @Override
            public void onSendUserReply(List<Reply> list) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_numbers, menu);
        if (Constants.DEBUG) {
            Log.i(TAG, "onCreateOptionsMenu: click");
        }
        MobclickAgent.onEvent(getApplicationContext(), StatsConstants.NUMBERS_CLICK_MENU);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            MobclickAgent.onEvent(getApplicationContext(), StatsConstants.NUMBERS_CLICK_SETTINGS);
            return true;
        } else if (id == R.id.action_feedback) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mAgent.updateUserInfo();
                }
            }).start();
            mAgent.startFeedbackActivity();
        }

        return super.onOptionsItemSelected(item);
    }
}
