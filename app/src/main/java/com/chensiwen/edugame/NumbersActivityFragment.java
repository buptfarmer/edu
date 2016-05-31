package com.chensiwen.edugame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chensiwen.edugame.particle.ExplosionField;
import com.chensiwen.edugame.particle.ParticleListener;
import com.chensiwen.edugame.particle.factory.FallingParticleFactory;
import com.chensiwen.edugame.particle.factory.VerticalAscentFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class NumbersActivityFragment extends BaseFragment {
    private static final String TAG = "NumbersActivityFragment";
    private View mRootView;

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

        ExplosionField explosionField5 = new ExplosionField(getActivity(), new FallingParticleFactory());
        mRootView.findViewById(R.id.text_particle).setOnClickListener(new ParticleListener(explosionField5) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                Toast.makeText(getActivity(), "text_particle clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mRootView.findViewById(R.id.layout_particle).setOnClickListener(new ParticleListener(explosionField5) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                Toast.makeText(getActivity(), "layout_particle clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected String getUmengTag() {
        return TAG;
    }
}
