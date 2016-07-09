package com.kanmanus.kmutt.sit.ijoint.net;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class DefaultSubscriber <T> extends rx.Subscriber<T> {
    @Override public void onCompleted() {
        // no-op by default.
    }

    @Override public void onError(Throwable e) {
        // no-op by default.
    }

    @Override public void onNext(T t) {
        // no-op by default.
    }
}
