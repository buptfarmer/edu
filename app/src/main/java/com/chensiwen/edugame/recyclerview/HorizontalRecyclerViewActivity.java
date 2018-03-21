package com.chensiwen.edugame.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.chensiwen.edugame.BaseAppCompatActivity;
import com.chensiwen.edugame.EduApplication;
import com.chensiwen.edugame.ExplodeItemAnimator;
import com.chensiwen.edugame.NumbersActivityFragment;
import com.chensiwen.edugame.R;
import com.chensiwen.edugame.StatsConstants;
import com.chensiwen.edugame.particle.ExplosionField;
import com.chensiwen.edugame.particle.factory.FallingParticleFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Random;

public class HorizontalRecyclerViewActivity extends BaseAppCompatActivity implements Handler.Callback {
    private static final String TAG = "HorizontalRecyclerViewA";
    private static final int MSG_EXPLOSION_DONE = 1;
    private static final int[] COLORS = new int[]{
            R.color.common_red,
            R.color.common_pink,
            R.color.common_purple,
            R.color.common_deep_purple,
            R.color.common_indigo,
            R.color.common_blue,
            R.color.common_light_blue,
            R.color.common_cyan,
            R.color.common_teal,
            R.color.common_green,
            R.color.common_light_green,
            R.color.common_lime,
            R.color.common_yellow,
            R.color.common_amber,
            R.color.common_orange,
            R.color.common_deep_orange,
            R.color.common_brown,
            R.color.common_grey,
            R.color.common_blue_grey,
    };
    private Handler mHandler = new Handler(Looper.myLooper(), this);

    @Override
    public boolean handleMessage(Message msg) {
        if (MSG_EXPLOSION_DONE == msg.what) {
            int position = msg.arg1;
            mContentList.remove(position);
            mRecyclerAdapter.notifyItemRemoved(position);
            mRecyclerAdapter.notifyItemRangeChanged(position, mRecyclerAdapter.getItemCount());
        }
        return true;
    }

    private RecordRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mContentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        setContentView(R.layout.activity_horizontal_recycler_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerAdapter = new RecordRecyclerAdapter(this, mContentList, mHandler);
        mRecyclerView.setAdapter(mRecyclerAdapter);
//        RecyclerView.ItemAnimator itemAnimator = new ExplodeItemAnimator(this);
//        itemAnimator.setMoveDuration(1000);
//        itemAnimator.setChangeDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
//        mRecyclerView.setItemAnimator(itemAnimator);


        // start animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.layout_item_anim);
        animation.setInterpolator(new OvershootInterpolator());
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        //layoutAnimationController.setInterpolator(new OvershootInterpolator());
        layoutAnimationController.setDelay(0.1f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
    }


    private void prepareData() {
        for (int i = 0; i < 21; i++) {
            mContentList.add(String.valueOf(i));
        }
    }

    static class RecordRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<String> mList;
        private BaseAppCompatActivity context;
        private Handler mHandler;

        public RecordRecyclerAdapter(BaseAppCompatActivity context, ArrayList<String> lists, Handler handler) {
            this.context = context;
            this.mList = lists;
            this.mHandler = handler;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView view = (CardView) LayoutInflater.from(context).inflate(R.layout.horizontal_recyelerview_item, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.update(mList.get(position), position, mHandler);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mRootView;
        public TextView mContent;
        private BaseAppCompatActivity mActivity;
        private ExplosionField mExplosion;
        private Random mRandom;

        public MyViewHolder(BaseAppCompatActivity context, CardView itemView) {
            super(itemView);
            mActivity = context;
            mRootView = itemView;
            mContent = (TextView) itemView.findViewById(R.id.content);
            mExplosion = new ExplosionField(mActivity, new FallingParticleFactory());
        }

        private int getRandomColor() {
            return mActivity.getResources().getColor(COLORS[mRandom.nextInt(COLORS.length)]);
        }

        public void update(final String value, final int position, final Handler handler) {
            Log.i(TAG, String.format("update: value:%s, position:%d", value, position));

            mRandom = new Random(position);
            mContent.setText(value);
            mRootView.setBackgroundColor(getRandomColor());
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (handler != null) {
                        handler.obtainMessage(MSG_EXPLOSION_DONE, position, 0).sendToTarget();
                    }
                    MobclickAgent.onEvent(mActivity, StatsConstants.NUMBERS_CLICK_PREFIX + value);
                }
            });

//            ViewPropertyAnimator animator = mRootView.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(1000);
//            animator.setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                }
//            });
//            animator.start();
        }
    }
}
