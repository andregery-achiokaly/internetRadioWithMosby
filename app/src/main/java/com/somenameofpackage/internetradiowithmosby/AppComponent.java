package com.somenameofpackage.internetradiowithmosby;

import com.somenameofpackage.internetradiowithmosby.model.RepositoryModule;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.presenter.AudioWavePresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.ControlPresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.RadioActivityPresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsListPresenter;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface AppComponent {
    void injectsRadioService(RadioService radioService);
    void injectsAudioWavePresenter(AudioWavePresenter audioWavePresenter);
    void injectsControlPresenter(ControlPresenter controlPresenter);
    void injectsStationsListPresenter(StationsListPresenter stationsListPresenter);
    void injectsStationsRelamDB(StationsRelamDB stationsRelamDB);
    void injectsRadioActivityPresenter(RadioActivityPresenter radioActivityPresenter);
}
