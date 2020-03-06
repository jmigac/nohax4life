package com.juricamigac.nohax4life;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class AddKolegij extends Fragment {

    private Spinner dropDownNaciniIzvodenjaNastave;
    private EditText etNazivKolegija;
    private EditText etBrojIzostanaka;
    private Button btnUnosKolegija;
    private OnKolegijAdded onKolegijAdded;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postaviGUI();
        postaviNacinIzvodenjaNastave();
        postaviVM();
        trigerNaGumb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_kolegij, container, false);
        return view;
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
                    KolegijDAL.KreirajKolegij(v.getContext(),kolegij);
                    onKolegijAdded.notifyChanges();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void postaviGUI() {
        dropDownNaciniIzvodenjaNastave = view.findViewById(R.id.spNaciniIzvodenja1);
        etNazivKolegija = view.findViewById(R.id.etNazivKolegija1);
        etBrojIzostanaka = view.findViewById(R.id.etBrojIzostanaka1);
        btnUnosKolegija = view.findViewById(R.id.btnUnosKolegija1);
    }

    private void postaviNacinIzvodenjaNastave() {
        List<String> nacini = new ArrayList<String>();
        nacini.add("Predavanje");
        nacini.add("Seminar");
        nacini.add("Laboratorijske vježbe");
        nacini.add("Auditorne vježbe");
        nacini.add("Demonstrature");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrentActivity.getActivity(),android.R.layout.simple_spinner_item,nacini);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownNaciniIzvodenjaNastave.setAdapter(adapter);

    }

}
