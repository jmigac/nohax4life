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
     * tako da se mogu preko observera pratiti ukoliko dođe do promjene da se određeno ažuriranje inicijalizira.
     */
    public LiveData<List<Kolegij>> kolegijiLiveData;
    /**
     * Kontekts modela, aktivnosti s koje je pozvan ViewModel
     */
    private Context context;

    public KolegijViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    /**
     * Funkcija za asynkrono kreiranje kolegija.
     * @param kolegij objekt kolegija koji želimo spremiti u bazu podataka.
     */
    public void unosKolegija(Kolegij kolegij){
        KolegijDAL.KreirajKolegij(context,kolegij);
    }

    /**
     * Funkcija za asynkrono brisanje kolegija
     * @param kolegij kolegij koji želimo izbrisati iz baze.
     */
    public void izbrisiKolegij(Kolegij kolegij){
        KolegijDAL.IzbrisiKolegij(context,kolegij);
    }

    /**
     * Funkcija za asynkrono ažuriranje kolegija.
     * @param kolegij kolegij koji želimo ažurirati s istim.
     */
    public void azurirajKolegij(Kolegij kolegij){
        KolegijDAL.AzurirajKolegij(context,kolegij);
    }

    /**
     * Funkcija za dohvaćanje određenog kolegija.
     * @param kolegij Kolegij s identifikatorom kojeg želimo dohvatiti.
     * @return vraća objekt Kolegij iz baze podataka.
     */
    public Kolegij dohvatiKolegij(Kolegij kolegij){
       return KolegijDAL.ReadById(context,kolegij.getId());
    }

    /**
     * Funkcija za dohvaćanje određenog kolegija tipa LiveData.
     * @param id identifikator kolegija
     * @return Objekt LiveData određenog kolegija.
     */
    public LiveData<Kolegij> dohvatiKolegijLIVE(int id){
        return KolegijDAL.DohvatiKolegijLIVE(id,context);
    }

    /**
     * Funkcija za dohvaćanje LiveData liste kolegija.
     * @return vraća listu kolegija učahurenih u tip LiveData.
     */
    public LiveData<List<Kolegij>> dohvatiSveKolegijeLIVE(){
        kolegijiLiveData = KolegijDAL.DohvatiSveKolegijeLive(context);
        return kolegijiLiveData;
    }

    /**
     * Funkcija za brisanje svih izostanaka određenog kolegija.
     * @param kolegij Kolegij za koji želimo izbrisati izostanke.
     */
    public void izbrisiIzostankeKolegija(Kolegij kolegij){
        IzostanakDAL.IzbrisiIzostankeKolegijaOdredenogKolegija(context,kolegij);
    }
}
