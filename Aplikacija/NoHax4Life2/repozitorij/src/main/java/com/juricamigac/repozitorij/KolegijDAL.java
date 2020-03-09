package com.juricamigac.repozitorij;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;

import java.util.List;

public class KolegijDAL {
    public static LiveData<List<Kolegij>> DohvatiSveKolegijeLIVE(Context context){
        return MyDatabase.getInstance(context).getKolegijDAO().dohvatiSveKolegijeLIVE();
    }
    public static void KreirajKolegij(Context context,Kolegij... kolegiji){
        new KolegijAsyncDAL.KreirajKolegijAsyncTask(context).execute(kolegiji);
    }

    public static void IzbrisiKolegij(Context context, Kolegij... kolegiji){
        new KolegijAsyncDAL.IzbrisiKolegijAsyncTask(context).execute(kolegiji);
    }

    public static void AzurirajKolegij(Context context, Kolegij... kolegiji){
        new KolegijAsyncDAL.AzurirajKolegijAsyncTask(context).execute(kolegiji);
    }
    public static Kolegij ReadById(Context context,int id){
        return MyDatabase.getInstance(context).getKolegijDAO().dohvatiKolegij(id);
    }
    public static LiveData<Kolegij> DohvatiKolegijLIVE(int id, Context context){
        return MyDatabase.getInstance(context).getKolegijDAO().dohvatiKolegijLIVE(id);
    }
    public static LiveData<List<Kolegij>> DohvatiSveKolegijeLive(Context context){
        return MyDatabase.getInstance(context).getKolegijDAO().dohvatiSveKolegijeLIVE();
    }
    public static void IzbrisiSveKolegije(Context context){
        MyDatabase.getInstance(context).getKolegijDAO().izbrisiSveKolegije();
    }


}
