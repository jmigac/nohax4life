package com.juricamigac.nohax4life;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.repozitorij.KolegijDAL;

import java.util.ArrayList;
import java.util.List;

import Managers.CurrentActivity;
import interfaces.OnKolegijAdded;

import static java.security.AccessController.getContext;

public class DodavanjeKolegija extends AppCompatActivity {
    /**
     * Varijabla za upravljanje elementom forme, popisom svih nacina upravljanja nastave.
     */
    private Spinner dropDownNaciniIzvodenjaNastave;
    /**
     * Varijabla za upravljanjem elementom forme za unos naziva kolegija.
     */
    private EditText etNazivKolegija;
    /**
     *Varijabla za upravljanje elementom forme, za unos broja izostanka na kolegiju.
     */
    private EditText etBrojIzostanaka;
    /**
     * Varijabla za upravljanje elementom forme, za upravljanje radnjama na gumbu za unos novog kolegija.
     */
    private Button btnUnosKolegija;
    /**
     * Varijabla za upravljanje instanciranim suceljem OnKolegijAdded koji razgovara s MainActivityem.
     */
    private OnKolegijAdded onKolegijAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_kolegija);

        postaviGUI();
        postaviNacinIzvodenjaNastave();
        postaviVM();
        trigerNaGumb();

    }

    /**
     * Funkcija postavlja instancirano sucelje onKolegijAdded klasom MainActivity kad ona impelementira navedeno rjesenje.
     */
    private void postaviVM() {
        if(CurrentActivity.getActivity() instanceof OnKolegijAdded){
            onKolegijAdded = (OnKolegijAdded) CurrentActivity.getActivity();
        }else{
            throw new RuntimeException(getContext().toString()
                    + " must implement OnKolegijAdded");
        }
    }

    /**
     * Funkcija kreira listener za upravljanje klikom na gumb dodavanje novog kolegija, a prvenstveno provjerava da li naziv kolegija
     * ima sadrzi ikakav unos, kao i broj izostanaka. Te naposljetku zatvara pripratnu aktivnost DodavanjeKolegija.
     */
    private void trigerNaGumb() {
        btnUnosKolegija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNazivKolegija.getText().length()>0
                && etBrojIzostanaka.getText().length()>0){
                    Kolegij kolegij = new Kolegij();
                    kolegij.setNaziv(etNazivKolegija.getText().toString());
                    kolegij.setBrojIzostanaka(Integer.parseInt(etBrojIzostanaka.getText().toString()));
                    kolegij.setNazivIzvodenjaKolegija(dropDownNaciniIzvodenjaNastave.getSelectedItem().toString());
                    onKolegijAdded.notifyChanges(kolegij);
                    ((Activity)getApplicationContext()).finishAffinity();
                }
            }
        });
    }

    /**
     * Funkcija dodaje instancu svakog elementa forme u određenu varijablu.
     */
    private void postaviGUI() {
        dropDownNaciniIzvodenjaNastave = findViewById(R.id.spNaciniIzvodenja1);
        etNazivKolegija = findViewById(R.id.etNazivKolegija1);
        etBrojIzostanaka = findViewById(R.id.etBrojIzostanaka1);
        btnUnosKolegija = findViewById(R.id.btnUnosKolegija1);
    }

    /**
     * Funkcija postavlja hard kodane stringove u array koji će se prikazivati u padajucem izborniku za izbor
     * mogucnosti izvodenja nastave.
     */
    private void postaviNacinIzvodenjaNastave() {
        List<String> nacini = new ArrayList<String> ();
        nacini.add("Predavanje");
        nacini.add("Seminar");
        nacini.add("Laboratorijske vježbe");
        nacini.add("Auditorne vježbe");
        nacini.add("Demonstrature");
        //android.R.layout.simple_spinner_item
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item_view,nacini);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownNaciniIzvodenjaNastave.setAdapter(adapter);

    }
}
