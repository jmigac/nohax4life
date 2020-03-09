package com.juricamigac.nohax4life;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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

    private String datumIzostanka;
    private Kolegij kolegijPromatranja;
    private String brojIzostanaka;
    private String nacinIzvodenja;
    private int idKolegija;
    private TextView tvIzostanakNazivKolegija;
    private TextView tvIzostanakNacinIzvodenjaNastave;
    private TextView tvIzostanakBrojIzostanaka;
    private TextView tvDatum;
    private EditText etRazlogIzostanka;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button btnDodajIzostanak;
    private RecyclerView recyclerView;
    private IzostanakAdapter izostanakAdapter;
    private OnKolegijChanged mKolegijChanged;
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
        observajMijenjanjeBrojaIzostanaka();
        akcijaDodavanjaIzostanka();
    }


    private void observajMijenjanjeBrojaIzostanaka() {
        izostanakViewModel.brojIzostanaka.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvIzostanakBrojIzostanaka.setText(integer+"/"+kolegijPromatranja.getBrojIzostanaka());
            }
        });
    }


    private void initViewModel() {
        izostanakViewModel = ViewModelProviders.of(this).get(IzostanakViewModel.class);
    }

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
    private void obavijestiObserver(){
        izostanakAdapter.notifyDataSetChanged();
        mKolegijChanged.NotifyAdapterOnKolegijChanges();
    }
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

    private void postaviPodatkeNaGUI() {
        tvIzostanakNazivKolegija.setText(kolegijPromatranja.getNaziv());
        tvIzostanakNacinIzvodenjaNastave.setText(kolegijPromatranja.getNazivIzvodenjaKolegija());
        tvIzostanakBrojIzostanaka.setText(brojIzostanaka);
    }

    private void inicijalizirajGUI() {
        tvIzostanakNazivKolegija = findViewById(R.id.tvIzostanakNazivKolegija);
        tvIzostanakNacinIzvodenjaNastave = findViewById(R.id.tvIzostanakNacinIzvodenjaNastave);
        tvIzostanakBrojIzostanaka = findViewById(R.id.tvIzostanakBrojIzostanaka);
        tvDatum = findViewById(R.id.tvDatumIzostanka);
        btnDodajIzostanak = findViewById(R.id.btnDodajIzostanak);
        etRazlogIzostanka = findViewById(R.id.etIzostanakRazlogIzostanka);
    }

    private void dohvatiPodatkeBundlea() {
        idKolegija = getIntent().getExtras().getInt("id");
        brojIzostanaka = getIntent().getExtras().getString("izostanak");
        kolegijPromatranja = MyDatabase.getInstance(this).getKolegijDAO().dohvatiKolegij(idKolegija);
        izostanakViewModel.DohvatiBrojIzostanaka(kolegijPromatranja.getId());
        inicijalizirajListener();
    }

    private void inicijalizirajListener() {
        if(CurrentActivity.getActivity() instanceof OnKolegijChanged){
            mKolegijChanged = (OnKolegijChanged) CurrentActivity.getActivity();
        }else{
            throw new RuntimeException(getContext().toString()
                    + " must implement OnKolegijChanged");
        }
    }
}
