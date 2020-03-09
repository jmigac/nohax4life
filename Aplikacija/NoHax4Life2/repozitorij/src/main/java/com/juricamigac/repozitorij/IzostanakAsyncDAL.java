package com.juricamigac.repozitorij;

import android.content.Context;
import android.os.AsyncTask;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;
import com.juricamigac.database.MyDatabase;

public class IzostanakAsyncDAL {
    public static class KreirajIzostanakAsync extends AsyncTask<Izostanak,Void,Void>{
        private Context context;

        public KreirajIzostanakAsync(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(Izostanak... izostanaks) {
            MyDatabase.getInstance(context).getIzostanakDAO().unosIzostanka(izostanaks);
            return null;
        }
    }
    public static class KreirajIzostanakKolegijaAsync extends AsyncTask<IzostanciKolegija,Void,Void>{
        private Context context;
        public KreirajIzostanakKolegijaAsync(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(IzostanciKolegija... izostanciKolegijas) {
            MyDatabase.getInstance(context).getIzostanakDAO().unosIzostankaNaKolegiju(izostanciKolegijas);
            return null;
        }
    }
}
