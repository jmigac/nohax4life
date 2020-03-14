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

    /**
     * Lista izostanaka učahurena u LiveData za daljnje promatranje, "observe".
     */
    public LiveData<List<Izostanak>> sviIzostanci;
    /**
     * LiveData koji sadrži broj izostanaka kao Integer.
     */
    public LiveData<Integer> brojIzostanaka;
    /**
     * Kontekst iz kojeg se poziva ViewModel
     */
    private Context context;

    public IzostanakViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    /**
     * Funkcija za Asynkroni unos novog izostanka.
     * @param izostanaci izostanak ili niz istih koji se žele spremiti u bazu podataka.
     */
    public void UnosIzostanka(Izostanak... izostanaci){
        IzostanakDAL.KreirajIzostanakAsync(context,izostanaci);
    }

    /**
     * Funkcija za asynkroni unos izostanka kolegija, jednog ili više istih.
     * @param izostanciKolegija jedan ili niz objekata tipa IzostanciKolegija koji se žele unijeti u bazu podataka.
     */
    public void UnosIzostankaKolegija(IzostanciKolegija... izostanciKolegija){
        IzostanakDAL.KreirajIzostanakKolegijaAsync(context,izostanciKolegija);
    }


    public void DohvatSvihIzostanaka(){

    }


    public void AzuriranjeIzostanka(){

    }

    /**
     * Funkcija za brisanje određenog izostanka.
     * @param idIzostanka identifikator izostanka za brisanje.
     */
    public void BrisanjeIzostanka(int idIzostanka){
        IzostanakDAL.BrisanjeIzostanka(context,idIzostanka);
    }

    /**
     * Funkcija za brisanje određenog IzostankaKolegija.
     * @param idKolegija identifikator kolegija.
     * @param idIzostanka identifikator izostanka.
     */
    public void BrisanjeIzostankaKolegija(int idKolegija,int idIzostanka){
        IzostanakDAL.BrisanjeIzostankaKolegija(context,idKolegija,idIzostanka);
    }

    /**
     * Funkcija za dohvaćanje liste izostanka učahurenih u LiveData.
     * @param id identifikator izostanka
     * @return listu izostanka učahurenih u LiveData
     */
    public LiveData<List<Izostanak>> DohvatiSveIzostankeLIVE(int id){
        sviIzostanci = IzostanakDAL.DohvatiSveIzostankeLIVE(context,id);
        return sviIzostanci;
    }

    /**
     * Funkcija dohvaća livedata integer
     * @param id identifikator izostanka
     * @return LiveData integer
     */
    public LiveData<Integer> DohvatiBrojIzostanaka(int id){
        brojIzostanaka = IzostanakDAL.DohvatiBrojIzostanakaLIVE(context,id);
        return brojIzostanaka;
    }
}
