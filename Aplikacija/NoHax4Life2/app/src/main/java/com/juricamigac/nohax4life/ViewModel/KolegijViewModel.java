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

    /**
     * Lista kolegija koji se prikazuju u recycleListi, ali su u formatu LiveData
     * tako da se mogu preko observera pratiti ukoliko dode do promjene da se odredeno azuriranje inicijalizira.
     */
    public LiveData<List<Kolegij>> kolegijiLiveData;
    /**
     * Kontekst modela, aktivnosti s koje je pozvan ViewModel
     */
    private Context context;

    public KolegijViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    /**
     * Funkcija za asynkrono kreiranje kolegija.
     * @param kolegij objekt kolegija koji zelimo spremiti u bazu podataka.
     */
    public void unosKolegija(Kolegij kolegij){
        KolegijDAL.KreirajKolegij(context,kolegij);
    }

    /**
     * Funkcija za asynkrono brisanje kolegija
     * @param kolegij kolegij koji zelimo izbrisati iz baze.
     */
    public void izbrisiKolegij(Kolegij kolegij){
        KolegijDAL.IzbrisiKolegij(context,kolegij);
    }

    /**
     * Funkcija za asynkrono azuriranje kolegija.
     * @param kolegij kolegij koji zelimo azurirati s istim.
     */
    public void azurirajKolegij(Kolegij kolegij){
        KolegijDAL.AzurirajKolegij(context,kolegij);
    }

    /**
     * Funkcija za dohvacanje odredenog kolegija.
     * @param kolegij Kolegij s identifikatorom kojeg zelimo dohvatiti.
     * @return vraca objekt Kolegij iz baze podataka.
     */
    public Kolegij dohvatiKolegij(Kolegij kolegij){
       return KolegijDAL.ReadById(context,kolegij.getId());
    }

    /**
     * Funkcija za dohvacanje određenog kolegija tipa LiveData.
     * @param id identifikator kolegija
     * @return Objekt LiveData odredenog kolegija.
     */
    public LiveData<Kolegij> dohvatiKolegijLIVE(int id){
        return KolegijDAL.DohvatiKolegijLIVE(id,context);
    }

    /**
     * Funkcija za dohvacanje LiveData liste kolegija.
     * @return vraca listu kolegija ucahurenih u tip LiveData.
     */
    public LiveData<List<Kolegij>> dohvatiSveKolegijeLIVE(){
        kolegijiLiveData = KolegijDAL.DohvatiSveKolegijeLive(context);
        return kolegijiLiveData;
    }

    /**
     * Funkcija za brisanje svih izostanaka odredenog kolegija.
     * @param kolegij Kolegij za koji zelimo izbrisati izostanke.
     */
    public void izbrisiIzostankeKolegija(Kolegij kolegij){
        IzostanakDAL.IzbrisiIzostankeKolegijaOdredenogKolegija(context,kolegij);
    }
}
