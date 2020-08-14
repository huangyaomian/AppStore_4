package com.hym.appstore.common.rx;


import android.util.Log;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.common.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxHttpResponseCompat {

    public static  <T>ObservableTransformer<BaseBean<T>,T> compatResult(){
        return new ObservableTransformer<BaseBean<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseBean<T>> baseBeanObservable) {
                return baseBeanObservable.flatMap(new Function<BaseBean<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(final BaseBean<T> tBaseBean) throws Exception {
                        Log.d("ObservableTransformer", "apply: " + tBaseBean.getStatus());
                        if (tBaseBean.success()){
                            return Observable.create(new ObservableOnSubscribe<T>() {
                                @Override
                                public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                                    try {
                                        subscriber.onNext(tBaseBean.getData());
                                        subscriber.onComplete();
                                    } catch (Exception e) {
                                        subscriber.onError(e);
                                    }

                                }
                            });
                        }
                        else{
                            //错误的时候会抛出异常然后再某一个地方集中处理
                            return Observable.error(new ApiException(tBaseBean.getStatus(), tBaseBean.getMessage()));

                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }

    /*public static <T> ObservableTransformer<BaseBean<T>,T> compatResult(){
        return new ObservableTransformer<BaseBean<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseBean<T>> baseBeanObservable) {
                return baseBeanObservable.flatMap(new Function<BaseBean<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull final BaseBean<T> tBaseBean) throws Exception {
                        if (tBaseBean.success()) {
                            return Observable.create(new ObservableOnSubscribe<T>() {
                                @Override
                                public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                                    try {
                                        Log.d("ObservableTransformer","请求成功返回给下级的next");
                                        if (tBaseBean.getData() == null){
                                            Log.d("ObservableTransformer","getData為null");
                                        }
                                        subscriber.onNext(tBaseBean.getData());
                                        subscriber.onComplete();
                                    } catch (Exception e) {
                                        subscriber.onError(e);
                                    }
                                }
                            });
                        } else {
                            return Observable.error(new ApiException(tBaseBean.getStatus(), tBaseBean.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }*/

    /**
     * http请求结果处理方法
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseBean<T>, Optional<T>> handle_result() {
        return upstream -> upstream
                .flatMap(new Function<BaseBean<T>, ObservableSource<Optional<T>>>() {
                             @Override
                             public ObservableSource<Optional<T>> apply(@NonNull BaseBean<T> tBaseBean) throws Exception {

                                 if (tBaseBean.success()) {
                                     // result.transform() 就是将返回结果进行包装
                                     return createHttpData(tBaseBean.transform());
                                 } else {
                                     // 发送请求失败的信息
                                     Log.d("hymmm", "apply:未登錄-- " + tBaseBean.getStatus());
                                     return Observable.error(new ApiException(tBaseBean.getStatus(), tBaseBean.getMessage()));
                                 }

                             }
                         }
                ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    private static <T> Observable<Optional<T>> createHttpData(Optional<T> t) {

        return Observable.create(e -> {
            try {
                e.onNext(t);
                e.onComplete();
            } catch (Exception exc) {
                e.onError(exc);
            }
        });
    }



}

