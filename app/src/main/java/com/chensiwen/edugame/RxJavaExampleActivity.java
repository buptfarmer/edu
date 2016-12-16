package com.chensiwen.edugame;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chensiwen.edugame.databinding.ActivityRxjavaExampleBinding;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaExampleActivity extends BaseAppCompatActivity {
    private static final String TAG = "RxJavaExampleActivity";

    //private Observable<String> mRepeatedOb = Observable.just("1");
    //private Observable<String> mRepeatedOb = Observable.just("1","2", "3","4");
    private Observable<String> mRepeatedOb;
    public class BindHanlders {
        public void onClickNumbers(View view) {
        }

        public void onClickTest1(View view) {
            Log.d(TAG, "onClickTest1: ");
            //mRepeatedOb = Observable.fromCallable(getStringCallable(1));
            mRepeatedOb = Observable.fromFuture(executor.submit(getStringCallable(1)));
            binding.rxTest1.append("clicked!");
            binding.rxTest1.append("\n");
            mRepeatedOb.subscribeOn(Schedulers.newThread())
                    //.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        }

        public void onClickTest2(View view) {
            Log.d(TAG, "onClickTest2: ");
            // why this will crash, with java.util.concurrent.ExecutionException: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
            mRepeatedOb = Observable.fromFuture(executor.submit(getStringCallable2(2)));
            binding.rxTest2.append("clicked!");
            binding.rxTest2.append("\n");
            mRepeatedOb.subscribeOn(Schedulers.newThread())
                    //.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        }

    }

    private Callable<String> getStringCallable(final int index) {
        binding.rxTest1.append("getStringCallable!");
        binding.rxTest1.append("\n");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                binding.rxTest1.append("call!");
                //try {
                //    Thread.sleep(500);
                //}catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
                binding.rxTest1.append("" + Thread.currentThread().getId());
                binding.rxTest1.append("\n");
                binding.rxTest1.setText(binding.rxTest1.getText() + "set text \n");
                return String.valueOf(index);
            }
        };
    }

    private Callable<String> getStringCallable2(final int index) {
        binding.rxTest2.append("getStringCallable!");
        binding.rxTest2.append("\n");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                binding.rxTest2.append("call!");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                binding.rxTest2.append("" + Thread.currentThread().getId());
                binding.rxTest2.append("\n");
                binding.rxTest2.setText(binding.rxTest2.getText() + "set text \n");
                return String.valueOf(index);
            }
        };
    }

    private ActivityRxjavaExampleBinding binding;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_rxjava_example, null, false);
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        BindHanlders handlers = new BindHanlders();
        binding.setHandlers(handlers);

    }

    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                binding.rxTest1.append("onSubscribe!");
                binding.rxTest1.append("" + Thread.currentThread().getId());
                binding.rxTest1.append("\n");
            }

            @Override
            public void onNext(String value) {
                binding.rxTest1.append("onNext!" + value);
                binding.rxTest1.append("" + Thread.currentThread().getId());
                binding.rxTest1.append("\n");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                binding.rxTest1.append("onError!");
                binding.rxTest1.append("" + Thread.currentThread().getId());
                binding.rxTest1.append("\n");
            }

            @Override
            public void onComplete() {
                binding.rxTest1.append("onComplete!");
                binding.rxTest1.append("" + Thread.currentThread().getId());
                binding.rxTest1.append("\n");

            }
        };
    }
}
