package com.chensiwen.edugame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.chensiwen.edugame.particle.ExplosionField;
import com.chensiwen.edugame.particle.ParticleListener;
import com.chensiwen.edugame.particle.factory.FallingParticleFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class NumbersActivityFragment extends BaseFragment implements Handler.Callback {
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
    private static final String TAG = "NumbersActivityFragment";
    private View mRootView;

    private RecordRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mContentList = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.myLooper(), this);

    public NumbersActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_numbers, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareData();

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerAdapter = new RecordRecyclerAdapter((BaseAppCompatActivity) getActivity(), mContentList, mHandler);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        //mRecyclerView.setAnimation();
        //mRecyclerView.setItemAnimator();
        //Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.umeng_fb_dialog_enter_anim);
        //mRecyclerView.setLayoutAnimation(new GridLayoutAnimationController(anim));

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.layout_item_anim);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setInterpolator(new BounceInterpolator());
        layoutAnimationController.setDelay(0.1f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
    }

    private void prepareData() {
        for (int i = 0; i < 21; i++) {
            mContentList.add(String.valueOf(i));
        }
    }

    @Override
    protected String getUmengTag() {
        return TAG;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (MSG_EXPLOSION_DONE == msg.what) {
            int position = msg.arg1;
            mContentList.remove(position);
            mRecyclerAdapter.notifyDataSetChanged();
        }
        return true;
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
            CardView view = (CardView) LayoutInflater.from(context).inflate(R.layout.numbers_game_item, parent, false);
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
            mRandom = new Random(position);
            mContent.setText(value);
            mRootView.setBackgroundColor(getRandomColor());
            mRootView.setOnClickListener(new ParticleListener(mExplosion) {
                @Override
                public void onExplodeEnd() {
                    int color = getRandomColor();
                    mRootView.setBackgroundColor(color);
                    if (handler != null) {
                        handler.obtainMessage(MSG_EXPLOSION_DONE, position, 0).sendToTarget();
                    }
                }

                @Override
                public void onClick(View v) {
                    super.onClick(v);
                    EduApplication.getSpeechSynthesizer().speak(value);
                    MobclickAgent.onEvent(mActivity, StatsConstants.NUMBERS_CLICK_PREFIX + value);
                }
            });
        }
    }
}
