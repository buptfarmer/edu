package com.chensiwen.edugame;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chensiwen.edugame.particle.ExplosionField;
import com.chensiwen.edugame.particle.ParticleListener;
import com.chensiwen.edugame.particle.factory.FallingParticleFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class NumbersActivityFragment extends BaseFragment {
    private static final String TAG = "NumbersActivityFragment";
    private View mRootView;

    private RecordRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mContentList = new ArrayList<>();

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
        mRecyclerAdapter = new RecordRecyclerAdapter((BaseAppCompatActivity) getActivity(), mContentList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void prepareData() {
        for (int i = 0; i < 10; i++) {
            mContentList.add(String.valueOf(i));
        }
    }

    @Override
    protected String getUmengTag() {
        return TAG;
    }


    static class RecordRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<String> mList;
        private BaseAppCompatActivity context;

        public RecordRecyclerAdapter(BaseAppCompatActivity context, ArrayList<String> lists) {
            this.context = context;
            this.mList = lists;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView view = (CardView) LayoutInflater.from(context).inflate(R.layout.main_ui_waterfall_item, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.update(mList.get(position));
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

        public MyViewHolder(BaseAppCompatActivity context, CardView itemView) {
            super(itemView);
            mActivity = context;
            mRootView = itemView;
            mContent = (TextView) itemView.findViewById(R.id.content);
            mExplosion = new ExplosionField(mActivity, new FallingParticleFactory());
        }

        public void update(final String value) {
            mContent.setText(value);
            mRootView.setOnClickListener(new ParticleListener(mExplosion) {
                @Override
                public void onExplodeEnd() {

                }

                @Override
                public void onClick(View v) {
                    super.onClick(v);
                    MobclickAgent.onEvent(mActivity, StatsConstants.NUMBERS_CLICK_PREFIX + value);
                }
            });
        }
    }
}
