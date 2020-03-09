package com.juricamigac.nohax4life.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;
import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.KolegijDAO_Impl;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.repozitorij.IzostanakDAL;
import com.juricamigac.repozitorij.KolegijDAL;

import java.util.List;

import Managers.CurrentActivity;

public class KolegijViewModel extends AndroidViewModel {

    public LiveData<List<Kolegij>> kolegijiLiveData;
    public LiveData<Izostanak> izostanciLiveData;
    public LiveData<IzostanciKolegija> izostanciKolegijaLiveData;
    private Context context;

    public KolegijViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }
    public void unosKolegija(Kolegij kolegij){
        KolegijDAL.KreirajKolegij(context,kolegij);
    }
    public void izbrisiKolegij(Kolegij kolegij){
        KolegijDAL.IzbrisiKolegij(context,kolegij);
    }
    public void azurirajKolegij(Kolegij kolegij){
        KolegijDAL.AzurirajKolegij(context,kolegij);
    }
    public void dohvatiKolegij(Kolegij kolegij){
        KolegijDAL.ReadById(context,kolegij.getId());
    }
    public LiveData<Kolegij> dohvatiKolegijLIVE(int id){
        return KolegijDAL.DohvatiKolegijLIVE(id,context);
    }
    public LiveData<List<Kolegij>> dohvatiSveKolegijeLIVE(){
        kolegijiLiveData = KolegijDAL.DohvatiSveKolegijeLive(context);
        return kolegijiLiveData;
    }
    public void izbrisiIzostankeKolegija(Kolegij kolegij){
        IzostanakDAL.IzbrisiIzostankeKolegijaOdredenogKolegija(context,kolegij);
    }
}
