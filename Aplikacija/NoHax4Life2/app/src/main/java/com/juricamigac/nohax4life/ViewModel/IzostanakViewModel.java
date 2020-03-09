package com.juricamigac.nohax4life.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;
import com.juricamigac.repozitorij.IzostanakDAL;
import com.juricamigac.repozitorij.KolegijDAL;

import java.util.List;

public class IzostanakViewModel extends AndroidViewModel {

    public LiveData<List<Izostanak>> sviIzostanci;
    public LiveData<Integer> brojIzostanaka;
    private Context context;
    public IzostanakViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }
    public void UnosIzostanka(Izostanak... izostanaci){
        IzostanakDAL.KreirajIzostanakAsync(context,izostanaci);
    }
    public void UnosIzostankaKolegija(IzostanciKolegija... izostanciKolegija){
        IzostanakDAL.KreirajIzostanakKolegijaAsync(context,izostanciKolegija);
    }
    public void DohvatSvihIzostanaka(){

    }
    public void AzuriranjeIzostanka(){

    }
    public void BrisanjeIzostanka(){

    }
    public LiveData<List<Izostanak>> DohvatiSveIzostankeLIVE(int id){
        sviIzostanci = IzostanakDAL.DohvatiSveIzostankeLIVE(context,id);
        return sviIzostanci;
    }
    public LiveData<Integer> DohvatiBrojIzostanaka(int id){
        brojIzostanaka = IzostanakDAL.DohvatiBrojIzostanakaLIVE(context,id);
        return brojIzostanaka;
    }
}
