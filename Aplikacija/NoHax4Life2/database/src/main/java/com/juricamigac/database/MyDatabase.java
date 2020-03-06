package com.juricamigac.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;
import com.juricamigac.core.entiteti.Kolegij;

@Database(version = MyDatabase.VERSION,entities = {Kolegij.class, Izostanak.class, IzostanciKolegija.class},exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public static final int VERSION = 1;
    public static final String NAME = "NoHax4Life";
    private static MyDatabase INSTANCE = null;

    public synchronized static MyDatabase getInstance(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MyDatabase.class,
                    MyDatabase.NAME
            ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract KolegijDAO getKolegijDAO();
    public abstract IzostanakDAO getIzostanakDAO();
}
