package com.juricamigac.repozitorij;

import android.content.Context;
import android.os.AsyncTask;

import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;

public class KolegijAsyncDAL {
    public static class KreirajKolegijAsyncTask extends AsyncTask<Kolegij,Void,Void> {
        private Context context;

        public KreirajKolegijAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(Kolegij... kolegijs) {
            MyDatabase.getInstance(context).getKolegijDAO().unosKolegija(kolegijs);
            return null;
        }
    }

    public static class IzbrisiKolegijAsyncTask extends AsyncTask<Kolegij,Void,Void> {
        private Context context;

        public IzbrisiKolegijAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(Kolegij... kolegiji) {
            MyDatabase.getInstance(context).getKolegijDAO().brisanjeKolegija(kolegiji);
            return null;
        }
    }
    public static class AzurirajKolegijAsyncTask extends AsyncTask<Kolegij,Void,Void>{
        private Context context;

        public AzurirajKolegijAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Kolegij... kolegijs) {
            MyDatabase.getInstance(context).getKolegijDAO().azurirajKolegij(kolegijs);
            return null;
        }
    }
}
