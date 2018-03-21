package com.chensiwen.edugame;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chensiwen.edugame.databinding.ActivityConstraintLayoutBinding;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ConstraintLayoutActivity extends BaseAppCompatActivity {
    private static final String TAG = "RxJavaExampleActivity";

    private static class UrlModel {
        public String url;
        public int result;

        public UrlModel(String url) {
            this.url = url;
        }
    }

    //private Observable<String> mRepeatedOb = Observable.just("1");
    private Observable<String> mRepeatedOb = Observable.just("1", "2", "3", "4");

    //private Observable<String> mRepeatedOb;
    public class BindHanlders {
        public void onClickNumbers(View view) {
        }

        public void onClickTest1(View view) {
            Log.d(TAG, "onClickTest1: ");
            String[] urls = {"1", "2","3","4"};
            Observable<String> aOb = Observable.fromArray(urls);
            //mRepeatedOb = Observable.fromCallable(getStringCallable(1));
            //Observable<String> mRepeatedOb = Observable.fromFuture(executor.submit(getStringCallable(1)));
//            binding.rxTest1.append("clicked!");
//            binding.rxTest1.append("\n");
            mRepeatedOb.map(new Function<String, UrlModel>() {
                @Override
                public UrlModel apply(String s) throws Exception {
                    UrlModel result = new UrlModel(s);
                    return result;
                }
            }).subscribeOn(Schedulers.newThread())
                    //.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUrlObserver());
        }

        public void onClickTest2(View view) {
            Log.d(TAG, "onClickTest2: ");
            // why this will crash, with java.util.concurrent.ExecutionException: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
//            mRepeatedOb = Observable.fromFuture(executor.submit(getStringCallable2(2)));
//            binding.rxTest2.append("clicked!");
//            binding.rxTest2.append("\n");
            mRepeatedOb.subscribeOn(Schedulers.newThread())
                    //.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        }

        public void onClickTest3(View view) {
            Log.d(TAG, "onClickTest3: ");
            // why this will crash, with java.util.concurrent.ExecutionException: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //try {
                    //    getStringCallable(3).call();
                    //} catch (Exception e) {
                    //    e.printStackTrace();
                    //}
//                    executor.submit(getStringCallable(3));
                }
            }).start();
        }

    }

    private void creatingObservables() {
        final String url = "";
        Observable<String> ob = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                // do something with url;
                // if (succ)
                e.onNext(url);
                e.onComplete();
                // else
                e.onError(null);
            }
        });
        ob.subscribe(getObserver());

        Observable<String> ob2 = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                if (true) {
                    return new ObservableSource<String>() {
                        @Override
                        public void subscribe(Observer<? super String> observer) {

                        }
                    };
                } else {
                    return Observable.just(url);
                }
            }
        });
        Observable<Long> ob3 = Observable.timer(1, TimeUnit.SECONDS);
    }

    private ActivityConstraintLayoutBinding binding;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_constraint_layout, null, false);
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        BindHanlders handlers = new BindHanlders();
        binding.setHandlers(handlers);

    }

    private Observer<UrlModel> getUrlObserver() {
        return new Observer<UrlModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UrlModel value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
//                binding.rxTest1.append("onSubscribe!");
//                binding.rxTest1.append("" + Thread.currentThread().getId());
//                binding.rxTest1.append("\n");
            }

            @Override
            public void onNext(String value) {

//                binding.rxTest1.append("onNext!" + value);
//                binding.rxTest1.append("" + Thread.currentThread().getId());
//                binding.rxTest1.append("\n");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
//                binding.rxTest1.append("onError!");
//                binding.rxTest1.append("" + Thread.currentThread().getId());
//                binding.rxTest1.append("\n");
            }

            @Override
            public void onComplete() {
//                binding.rxTest1.append("onComplete!");
//                binding.rxTest1.append("" + Thread.currentThread().getId());
//                binding.rxTest1.append("\n");

            }
        };
    }
}
