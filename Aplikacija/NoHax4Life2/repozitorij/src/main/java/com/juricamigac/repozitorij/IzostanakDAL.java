package com.juricamigac.repozitorij;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;
import com.juricamigac.database.MyDatabase;

import java.util.List;

public class IzostanakDAL {
    public static List<Izostanak> DohvatiSveIzostankeKolegija(int id, Context context){
        return MyDatabase.getInstance(context).getIzostanakDAO().dohvatiSveIzostankeKolegija(id);
    }
    public static void KreirajIzostanakAsync(Context context,Izostanak... izostanaci){
        new IzostanakAsyncDAL.KreirajIzostanakAsync(context).execute(izostanaci);
    }
    public static void KreirajIzostanakKolegijaAsync(Context context, IzostanciKolegija... izostanciKolegija){
        new IzostanakAsyncDAL.KreirajIzostanakKolegijaAsync(context).execute(izostanciKolegija);
    }
    public static LiveData<List<Izostanak>> DohvatiSveIzostankeLIVE(Context context,int id){
        return MyDatabase.getInstance(context).getIzostanakDAO().dohvatiSveIzostankeKolegijaLIVE(id);
    }

    public static LiveData<Integer> DohvatiBrojIzostanakaLIVE(Context context, int id) {
        return MyDatabase.getInstance(context).getIzostanakDAO().dohvatiBrojIzostanakaOdredenogKolegijaLIVE(id);
    }
}
