package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.views.ControlView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.RadioStatus;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subjects.BehaviorSubject;

public class ControlPresenter extends MvpBasePresenter<ControlView> {
    @Inject
    StationsRelamDB dataBase;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;
    private BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();

    public ControlPresenter(Context context) {
        serviceConnection = new RadioServiceConnection();
        RadioApplication.getComponent().injectsControlPresenter(this);
        dataBase.setDefaultValues(context);
        bindToRadioService(context);
    }

    private void bindToRadioService(Context context) {
        if (!isBind)
            context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public void changePlayState() {
        dataBase.getCurrentStation()
                .subscribe(station -> changePlayStateSubject.onNext(station.getSource()));
    }

    public void onPause(Context context) {
        if (isBind) {
            context.unbindService(serviceConnection);
            isBind = false;
        }
    }

    private class RadioServiceConnection implements ServiceConnection {
        Subscriber<RadioStatus> statusSubscriber = getStatusSubscriber();

        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSubject);
            ((RadioService.RadioBinder) binder).subscribeStatus(statusSubscriber);
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null) getView().showStatus(RadioStatus.isStop);
            isBind = false;
            if (!statusSubscriber.isUnsubscribed()) statusSubscriber.unsubscribe();
        }
    }

    @NonNull
    private Subscriber<RadioStatus> getStatusSubscriber() {
        return new Subscriber<RadioStatus>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) getView().showStatus(RadioStatus.Error);
            }

            @Override
            public void onNext(RadioStatus radioStatus) {
                if (getView() != null) getView().showStatus(radioStatus);
            }
        };
    }
}
