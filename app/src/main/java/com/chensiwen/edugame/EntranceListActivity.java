package com.chensiwen.edugame;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.chensiwen.edugame.databinding.ActivityEntranceListBinding;

public class EntranceListActivity extends BaseAppCompatActivity {

    public class BindHanlders {
        public void onClickNumbers(View view) {
            startActivity(new Intent(EntranceListActivity.this, NumbersActivity.class));
        }

        public void onClickAVD(View view) {
            startActivity(new Intent(EntranceListActivity.this, AnimatedVectorDrawableActivity.class));
        }
        public void onClickRxJava(View view) {
            startActivity(new Intent(EntranceListActivity.this, RxJavaExampleActivity.class));
        }
    }

    private ActivityEntranceListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_entrance_list, null, false);
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        BindHanlders handlers = new BindHanlders();
        binding.setHandlers(handlers);

    }

}
