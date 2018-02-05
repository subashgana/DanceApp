package net.alhazmy13.mediapicker.rxjava.image.observable;

import android.content.Context;
import android.util.Log;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
abstract class ImagePickerBaseObservable implements Observable.OnSubscribe<List<String>> {

    private static final String TAG = "ImagePicker";
    public Context context;

    public ImagePickerBaseObservable(Context context) {
        this.context = context;
    }

    @Override
    public void call(final Subscriber subscriber) {
        Log.d(TAG, "call() called with: " + "subscriber = [" + subscriber + "]");
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                subscriber.unsubscribe();
                onUnsubscribed();
            }
        }));
    }

    public abstract void registerImagePickerObservable();


    abstract protected void onUnsubscribed();



}