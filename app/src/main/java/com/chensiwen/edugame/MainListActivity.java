package com.chensiwen.edugame;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainListActivity extends AppCompatActivity {
    private static final String TAG = "MainListActivity";
    private static final String INTENT_LIST_ACTION = "com.chensiwen.edugame.MAIN";
    private RecyclerView mMainListRecyclerView;
    private List<ResolveInfo> resolveInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        initData();
        initView();
    }

    private void initView() {
        mMainListRecyclerView = findViewById(R.id.main_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMainListRecyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(this, resolveInfos);
        mMainListRecyclerView.setAdapter(myAdapter);
        mMainListRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int margin = Utils.dp2Px(5);
                outRect.set(margin, margin, margin, margin);
            }
        });
        myAdapter.notifyDataSetChanged();

    }

    private void initData() {
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(new Intent(INTENT_LIST_ACTION), PackageManager.MATCH_ALL);
        Log.d(TAG, "initData: ");
        if (BuildConfig.DEBUG) Log.d(TAG, "resolveInfos:" + resolveInfos);
        if (resolveInfos != null) {
            this.resolveInfos.addAll(resolveInfos);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.main_list_item_title);
            icon = itemView.findViewById(R.id.main_list_item_icon);

        }

        public void bind(final ResolveInfo resolveInfo) {
            title.setText(resolveInfo.activityInfo.loadLabel(title.getContext().getPackageManager()));
            icon.setImageDrawable(resolveInfo.activityInfo.loadIcon(title.getContext().getPackageManager()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String activityName = resolveInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                    String pkgName = resolveInfo.activityInfo.packageName; // 获得应用程序的包名
                    // 为应用程序的启动Activity 准备Intent
                    Intent launchIntent = new Intent();
                    launchIntent.setComponent(new ComponentName(pkgName,
                            activityName));
                    v.getContext().startActivity(launchIntent);
                }
            });

        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<ResolveInfo> resolveInfos;
        private Context context;

        public MyAdapter(Context context, List<ResolveInfo> resolveInfos) {
            this.context = context;
            this.resolveInfos = resolveInfos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(resolveInfos.get(position));
        }

        @Override
        public int getItemCount() {
            return resolveInfos.size();
        }
    }
}
