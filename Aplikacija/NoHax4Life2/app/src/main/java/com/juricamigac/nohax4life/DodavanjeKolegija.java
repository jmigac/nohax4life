package com.juricamigac.nohax4life;

import androidx.appcompat.app.AppCompatActivity;

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

    private Spinner dropDownNaciniIzvodenjaNastave;
    private EditText etNazivKolegija;
    private EditText etBrojIzostanaka;
    private Button btnUnosKolegija;
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

    private void postaviVM() {
        if(CurrentActivity.getActivity() instanceof OnKolegijAdded){
            onKolegijAdded = (OnKolegijAdded) CurrentActivity.getActivity();
        }else{
            throw new RuntimeException(getContext().toString()
                    + " must implement OnKolegijAdded");
        }
    }

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
                    KolegijDAL.KreirajKolegij(getApplicationContext(),kolegij);
                    finish();
                }
            }
        });
    }

    private void postaviGUI() {
        dropDownNaciniIzvodenjaNastave = findViewById(R.id.spNaciniIzvodenja1);
        etNazivKolegija = findViewById(R.id.etNazivKolegija1);
        etBrojIzostanaka = findViewById(R.id.etBrojIzostanaka1);
        btnUnosKolegija = findViewById(R.id.btnUnosKolegija1);
    }

    private void postaviNacinIzvodenjaNastave() {
        List<String> nacini = new ArrayList<String> ();
        nacini.add("Predavanje");
        nacini.add("Seminar");
        nacini.add("Laboratorijske vježbe");
        nacini.add("Auditorne vježbe");
        nacini.add("Demonstrature");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,nacini);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownNaciniIzvodenjaNastave.setAdapter(adapter);

    }
}
