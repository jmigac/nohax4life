package com.juricamigac.nohax4life;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.nohax4life.Adapters.KolegijAdapter;
import com.juricamigac.nohax4life.ViewModel.KolegijViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import Managers.CurrentActivity;
import interfaces.OnKolegijAdded;
import interfaces.OnKolegijChanged;
import interfaces.OnKolegijClick;

import static com.juricamigac.database.MyDatabase.getInstance;

public class MainActivity extends AppCompatActivity implements OnKolegijClick, OnKolegijChanged, OnKolegijAdded {

    private MyDatabase bazaPodataka = null;
    private RecyclerView recyclerView;
    private List<Kolegij> sviKolegiji;
    private KolegijViewModel kolegijViewModel;
    public KolegijAdapter kolegijAdapter;
    private int pozicijaNovogUpisa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CurrentActivity.setActivity(this);

        //VIEW MODEL

        kolegijViewModel = ViewModelProviders.of(this).get(KolegijViewModel.class);

        //

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this,DodavanjeKolegija.class));
            }
        });


        bazaPodataka = getInstance(this);

        sviKolegiji = bazaPodataka.getKolegijDAO().dohvatiSveKolegije();
        if(sviKolegiji.size()>0){
            for(Kolegij k:sviKolegiji){
                Log.e("Kolegij",k.getNaziv());
            }
        }else{
            Kolegij kolegij1 = new Kolegij();
            kolegij1.setNaziv("SPIS");
            kolegij1.setNazivIzvodenjaKolegija("Predavanje");
            kolegij1.setBrojIzostanaka(3);
            Kolegij kolegij2 = new Kolegij();
            kolegij2.setNaziv("EMP");
            kolegij2.setNazivIzvodenjaKolegija("Laboratorijske vježbe");
            kolegij2.setBrojIzostanaka(3);
            Kolegij kolegij3 = new Kolegij();
            kolegij3.setNaziv("NWTIS");
            kolegij3.setNazivIzvodenjaKolegija("Laboratorijske vježbe");
            kolegij3.setBrojIzostanaka(3);
            bazaPodataka.getKolegijDAO().unosKolegija(kolegij1,kolegij2,kolegij3);
        }
        postaviRecycleView();

    }


    private KolegijAdapter postaviRecycleView() {
        recyclerView = findViewById(R.id.recycleLista);
        kolegijAdapter = new KolegijAdapter(this);
        kolegijViewModel.dohvatiSveKolegijeLIVE();
        kolegijAdapter.setKolegiji(kolegijViewModel.dohvatiSveKolegijeLIVE().getValue());
        kolegijViewModel.kolegijiLiveData.observe(this, new Observer<List<Kolegij>>() {
            @Override
            public void onChanged(List<Kolegij> kolegijs) {
                kolegijAdapter.setKolegiji(kolegijs);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(kolegijAdapter);
        kolegijAdapter.setContext(this);
        return kolegijAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onKolegijClick(int position) {
        Kolegij odabranKolegij = sviKolegiji.get(position);
        String izostanci = "";
        int brojIzostanaka = MyDatabase.getInstance(this).getIzostanakDAO().dohvatiBrojIzostanakaOdredenogKolegija(odabranKolegij.getId());
        int brojMogucihIzostanaka = odabranKolegij.getBrojIzostanaka();
        izostanci = brojIzostanaka+"/"+brojMogucihIzostanaka;

        Log.e("Kolegij","KLIK na "+odabranKolegij.getNaziv());
        Bundle bundle = new Bundle();
        bundle.putString("naziv",odabranKolegij.getNaziv());
        bundle.putString("izostanak",izostanci);
        bundle.putString("nacin",odabranKolegij.getNazivIzvodenjaKolegija());
        bundle.putInt("id",odabranKolegij.getId());
        Intent i = new Intent(MainActivity.this, DodavanjeIzostanka.class);
        i.putExtras(bundle);
        CurrentActivity.setActivity(this);
        startActivity(i);
    }

    @Override
    public void NotifyAdapterOnKolegijChanges() {
        kolegijAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyChanges() {
        kolegijAdapter.notifyItemInserted(pozicijaNovogUpisa+1);
        kolegijAdapter.notifyDataSetChanged();
    }

}
