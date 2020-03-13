package com.juricamigac.nohax4life;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.nohax4life.Adapters.KolegijAdapter;
import com.juricamigac.nohax4life.ViewModel.KolegijViewModel;
import com.juricamigac.repozitorij.IzostanakDAL;
import com.juricamigac.repozitorij.KolegijDAL;

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
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import Managers.CurrentActivity;
import interfaces.IChangeColor;
import interfaces.OnKolegijAdded;
import interfaces.OnKolegijChanged;
import interfaces.OnKolegijClick;

import static com.juricamigac.database.MyDatabase.getInstance;

public class MainActivity extends AppCompatActivity implements OnKolegijClick, OnKolegijChanged, OnKolegijAdded {

    /**
     * Varijabla za prohranjivanje objekta za upravljanje lokalnom bazom podataka.
     */
    private MyDatabase bazaPodataka;
    /**
     * Varijabla za upravljanje recycler listom na početnom ekranu.
     */
    private RecyclerView recyclerView;
    /**
     * Varijabla za upravljanje listom svih kolegija koja se ažurira unutar observera
     * za promatranje popisa svih kolegija koji se prikazuju na recycler viewu.
     */
    private List<Kolegij> sviKolegiji;
    /**
     * Varijabla za klasu MVVM strukture, konkretnije ViewModel koja radi svu poslovnu l
     * ogiku vezanu uz CRUD operacije vezane uz entitet Kolegij.
     */
    private KolegijViewModel kolegijViewModel;
    /**
     * Varijabla vezana uz adapter koji se prikazuje na recycler view.
     */
    public KolegijAdapter kolegijAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Postavljanje trenutnog konteksta u posebnu klasu.
         */
        CurrentActivity.setActivity(this);

        /**
         * Inicijalizacija view modela.
         */
        kolegijViewModel = ViewModelProviders.of(this).get(KolegijViewModel.class);

        /**
         * Postavljanje gumba za dodavanje novog kolegija i funkcije onClick koja preusmjerava
         * na aktivnost DodavanjeKolegija.
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,DodavanjeKolegija.class));

            }
        });
        /**
         * Dohvaćanje instance baze podataka.
         */
        bazaPodataka = getInstance(this);
        /**
         * Dohvaćnje početne kolegije na kreiranju aktivnosti MainActivity
         */
        sviKolegiji = bazaPodataka.getKolegijDAO().dohvatiSveKolegije();
        postaviRecycleView();
        makeKolegijEraseable(recyclerView,kolegijAdapter);
    }

    /**
     * Funkcija postavlja recyclerView na aktivnosti MainActivity,
     * kreira objekt KolegijAdapter, dohvaća sve kolegije LIVE preko view modela,
     * postavljaju se dohvaćeni modeli unutar adaptera, i ujedno je kreiran observer
     * koji promatra LiveData listu kolegija, da u slučaju promjene parsa sve kolegije u adapter.
     * @return Vraća novokreirani KolegijAdapter
     */
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
            KolegijDAL.IzbrisiSveKolegije(this);
            IzostanakDAL.IzbrisiSveIzostanke(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Funkcija koja implementira funkciju iz sučelja OnKolegijClick, koja zapravo računa broj
     * trenutnih izostanaka određenog kolegija i dohvaća broj mogućih izostanaka te kreira
     * bundle koji se prosljeđuje novoj aktivnosti koja će se kreirati, DodavanjeIzostanaka.
     * @param position pozicija kliknutog kolegija unutar liste sviKolegiji
     */
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

    /**
     * Funkcija koja je implementirana zbog sučelja OnKolegijChanged koji javlja iz drugih klasa
     * klasi MainActivity da je došlo do promjene određenih podataka, kao pr. broja izostanaka da se
     * mogu u stvarnom vremenu ažurirati na MainActivity.
     */
    @Override
    public void NotifyAdapterOnKolegijChanges() {
        kolegijAdapter.notifyDataSetChanged();
    }

    /**
     * Funkcija koja implementira funkciju sučelja OnKolegijAdded koja unosi novi
     * kolegij pomoću viewModela, i obaviještava adapter o promjenama i o poziciji
     * novog kreiranog kolegija u listi.
     * @param kolegij novokreirani Kolegij koje se treba unijeti u bazu podataka.
     */
    @Override
    public void notifyChanges(Kolegij kolegij) {
        kolegijViewModel.unosKolegija(kolegij);
        kolegijViewModel.dohvatiSveKolegijeLIVE();
        kolegijAdapter.notifyItemInserted(kolegijViewModel.kolegijiLiveData.getValue().size()-1);
        kolegijAdapter.notifyDataSetChanged();
    }

    /**
     * Funkcija omogućava da svaki element adaptera kolegijAdapter bude moguće obrisati potezom ulijevo ili udesno.
     * @param recyclerView RecyclerView objekt nad kojim želimo to omogućiti.
     * @param kolegijAdapter Adapter koji je sadržan unutar recycler viewa.
     */
    public void makeKolegijEraseable(final RecyclerView recyclerView, final KolegijAdapter kolegijAdapter){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Kolegij kolegij = kolegijAdapter.getKolegijAtPosition(viewHolder.getAdapterPosition());
                kolegijAdapter.removeKolegijAtPosition(viewHolder.getAdapterPosition());
                kolegijViewModel.izbrisiKolegij(kolegij);
                kolegijViewModel.izbrisiIzostankeKolegija(kolegij);
                Snackbar.make(recyclerView,"Izbrisan je kolegij "+kolegij.getNaziv(),Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

}
