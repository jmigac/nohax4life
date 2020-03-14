package com.juricamigac.nohax4life;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;
import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.nohax4life.Adapters.IzostanakAdapter;
import com.juricamigac.nohax4life.Adapters.KolegijAdapter;
import com.juricamigac.nohax4life.ViewModel.IzostanakViewModel;
import com.juricamigac.repozitorij.IzostanakDAL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Managers.CurrentActivity;
import interfaces.OnKolegijChanged;

import static java.security.AccessController.getContext;

public class DodavanjeIzostanka extends AppCompatActivity {

    /**
     * Varijabla za prikazivanje odabranog datuma na formi.
     */
    private String datumIzostanka;
    /**
     * Varijabla koja sprema prosljedeni objekt Kolegija za koji se unose izostanci.
     */
    private Kolegij kolegijPromatranja;
    /**
     * Varijabla koja sprema u String trenutni broj izostanaka.
     */
    private String brojIzostanaka;
    /**
     * Varijabla koja sprema idKolegija koji se trenutno promatra takoder unutar varijable kolegijPromatranja.
     */
    private int idKolegija;
    /**
     * Varijabla za upravljanje elementom forme, za prikazivanje teksta, naziva kolegija.
     */
    private TextView tvIzostanakNazivKolegija;
    /**
     * Varijabla za upravljanje elementom forme za prikazivanje nacina izvodenja nastave.
     */
    private TextView tvIzostanakNacinIzvodenjaNastave;
    /**
     * Varijabla za upravljanje elementom forme za ispis broja izostanka.
     */
    private TextView tvIzostanakBrojIzostanaka;
    /**
     * Varijabla za upravljanje elementom forme za ispis datuma.
     */
    private TextView tvDatum;
    /**
     * Varijabla za upravljanje elementom forme, za unos razloga izostanka.
     */
    private EditText etRazlogIzostanka;
    /**
     * Varijabla za upravljanje listenerom za pokretanje dialoga za odabir datuma.
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    /**
     * Varijabla za upravljanje elementom forme, gumb za pokretanje kreriranja novog izostanka.
     */
    private Button btnDodajIzostanak;
    /**
     * Varijabla za upravljanje elementom forme, recyclerviewom koji prikazuje sve izostanke određenog kolegija.
     */
    private RecyclerView recyclerView;
    /**
     * Varijabla za upravljanje adapterom koji se prikazuje unutar recyclerviewa
     */
    private IzostanakAdapter izostanakAdapter;
    /**
     * Varijabla za upravljanje instancom sucelja za prosljeđivanje informacija preko sucelja aktivnosti MainActivity.
     */
    private OnKolegijChanged mKolegijChanged;
    /**
     * Varijabla za upravljanje ViewModelom aktivnosti DodavanjeIzostanka.
     */
    private IzostanakViewModel izostanakViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_izostanka);
        initViewModel();
        dohvatiPodatkeBundlea();
        inicijalizirajGUI();
        postaviPodatkeNaGUI();
        postaviDefaultDatum();
        postaviRecycleView();
        makeIzostanakEraseable(recyclerView,izostanakAdapter);
        observajMijenjanjeBrojaIzostanaka();
        akcijaDodavanjaIzostanka();
    }

    /**
     * Funkcija koja unutar sebe ima observer koja promatra trenutni broj izostanka
     * da može varijablu za prikaz na formi ažurirati u realnom vremenu.
     */
    private void observajMijenjanjeBrojaIzostanaka() {
        izostanakViewModel.brojIzostanaka.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvIzostanakBrojIzostanaka.setText(integer+"/"+kolegijPromatranja.getBrojIzostanaka());
            }
        });
    }

    /**
     * Funkcija za instanciranje objekta viewModela aktivnosti DodavanjeIzostanka.
     */
    private void initViewModel() {
        izostanakViewModel = ViewModelProviders.of(this).get(IzostanakViewModel.class);
    }

    /**
     * Funkcija postavlja recyclerView na aktivnosti DodavanjeIzostanka,
     * kreira objekt izostanakAdapter, dohvaća sve kolegije LIVE preko view modela,
     * postavljaju se dohvaceni modeli unutar adaptera, i ujedno je kreiran observer
     * koji promatra LiveData listu izostanka, da u slucaju promjene parsa sve izostanke u adapter.
     */
    private void postaviRecycleView() {
        recyclerView = findViewById(R.id.recycleViewIzostanci);
        izostanakAdapter = new IzostanakAdapter();
        izostanakViewModel.DohvatiSveIzostankeLIVE(kolegijPromatranja.getId());
        izostanakViewModel.sviIzostanci.observe(this, new Observer<List<Izostanak>>() {
            @Override
            public void onChanged(List<Izostanak> izostanci) {
                izostanakAdapter.setIzostanci(izostanci);
            }
        });
        izostanakAdapter.setIzostanci(MyDatabase.getInstance(this).getIzostanakDAO().dohvatiSveIzostankeKolegija(kolegijPromatranja.getId()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(izostanakAdapter);
        izostanakAdapter.setContext(this);
    }

    /**
     * Funkcija za dodavanje listenera na gumb, koji dodaje novi Izostanak i IzostanakKolegija.
     */
    private void akcijaDodavanjaIzostanka() {

        btnDodajIzostanak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Izostanak izostanak = new Izostanak();
                izostanak.setDatumIzostanka(datumIzostanka);
                izostanak.setRazlogIzostanka(etRazlogIzostanka.getText().toString());
                long id = MyDatabase.getInstance(v.getContext()).getIzostanakDAO().unosIzostanka(izostanak)[0];
                Log.e("Kolegij",datumIzostanka);
                Log.e("Kolegij",etRazlogIzostanka.getText().toString());
                Log.e("Kolegij",String.valueOf(id));
                IzostanciKolegija izostanciKolegija = new IzostanciKolegija();
                izostanciKolegija.setIdIzostanka((int)id);
                izostanciKolegija.setIdKolegij(kolegijPromatranja.getId());
                //MyDatabase.getInstance(v.getContext()).getIzostanakDAO().unosIzostankaNaKolegiju(izostanciKolegija);

                //izostanakViewModel.UnosIzostanka(izostanak);
                izostanakViewModel.UnosIzostankaKolegija(izostanciKolegija);

                obavijestiObserver();
            }
        });
    }

    /**
     * Funkcija koja objavjestava adapter o promjenama nad izostancima i preko funkcije za NotifyAdapterOnKolegijChanges
     * obavjestava se adapter na MainActivityu za promjene nad unesenim
     */
    private void obavijestiObserver(){
        izostanakAdapter.notifyDataSetChanged();
        mKolegijChanged.NotifyAdapterOnKolegijChanges();
    }

    /**
     * Funkcija postavlja defaultni datum kao sadašnji dan.
     */
    private void postaviDefaultDatum() {
        Date trenutniDatum = new Date();
        trenutniDatum.getTime();

        tvDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int godina = cal.get(Calendar.YEAR);
                int mjesec = cal.get(Calendar.MONTH);
                int dan = cal.get(Calendar.DAY_OF_MONTH);

                datumIzostanka = dan+"/"+mjesec+"/"+godina;
                tvDatum.setText(datumIzostanka);

                DatePickerDialog dialog = new DatePickerDialog(
                        DodavanjeIzostanka.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        godina,mjesec,dan);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datumIzostanka = dayOfMonth+"/"+month+"/"+year;
                tvDatum.setText(datumIzostanka);
                btnDodajIzostanak.setEnabled(true);
            }
        };

    }

    /**
     * Funkcija postavlja tekstualne podatke na elemente forme.
     */
    private void postaviPodatkeNaGUI() {
        tvIzostanakNazivKolegija.setText(kolegijPromatranja.getNaziv());
        tvIzostanakNacinIzvodenjaNastave.setText(kolegijPromatranja.getNazivIzvodenjaKolegija());
        tvIzostanakBrojIzostanaka.setText(brojIzostanaka);
    }

    /**
     * Funkcija postavlja instance određenih elemenata forme na određene pripadne varijable.
     */
    private void inicijalizirajGUI() {
        tvIzostanakNazivKolegija = findViewById(R.id.tvIzostanakNazivKolegija);
        tvIzostanakNacinIzvodenjaNastave = findViewById(R.id.tvIzostanakNacinIzvodenjaNastave);
        tvIzostanakBrojIzostanaka = findViewById(R.id.tvIzostanakBrojIzostanaka);
        tvDatum = findViewById(R.id.tvDatumIzostanka);
        btnDodajIzostanak = findViewById(R.id.btnDodajIzostanak);
        etRazlogIzostanka = findViewById(R.id.etIzostanakRazlogIzostanka);
    }

    /**
     * Funkcija dohvaća varijable iz bundle i postavlja ih u određene varijable.
     */
    private void dohvatiPodatkeBundlea() {
        idKolegija = getIntent().getExtras().getInt("id");
        brojIzostanaka = getIntent().getExtras().getString("izostanak");
        kolegijPromatranja = MyDatabase.getInstance(this).getKolegijDAO().dohvatiKolegij(idKolegija);
        izostanakViewModel.DohvatiBrojIzostanaka(kolegijPromatranja.getId());
        inicijalizirajListener();
    }

    /**
     * Funkcija instancira varijablu sucelja.
     */
    private void inicijalizirajListener() {
        if(CurrentActivity.getActivity() instanceof OnKolegijChanged){
            mKolegijChanged = (OnKolegijChanged) CurrentActivity.getActivity();
        }else{
            throw new RuntimeException(getContext().toString()
                    + " must implement OnKolegijChanged");
        }
    }

    /**
     * Funkcija omogucava da svaki element adaptera kolegijAdapter bude moguce obrisati potezom ulijevo ili udesno.
     * @param recyclerView RecyclerView objekt nad kojim zelimo to omoguciti.
     * @param izostanakAdapter Adapter koji je sadrzan unutar recycler viewa.
     */
    public void makeIzostanakEraseable(final RecyclerView recyclerView, final IzostanakAdapter izostanakAdapter){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Izostanak izostanak= izostanakAdapter.getIzostanakAt(viewHolder.getAdapterPosition());
                izostanakAdapter.removeIzostanakAt(viewHolder.getAdapterPosition());
                izostanakViewModel.BrisanjeIzostankaKolegija(kolegijPromatranja.getId(),izostanak.getId());
                izostanakViewModel.BrisanjeIzostanka(izostanak.getId());
                Snackbar.make(recyclerView,"Izbrisan je izostanak.",Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
