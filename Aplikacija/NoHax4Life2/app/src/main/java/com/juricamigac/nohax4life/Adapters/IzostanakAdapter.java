package com.juricamigac.nohax4life.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.nohax4life.R;

import java.util.ArrayList;
import java.util.List;

import interfaces.OnKolegijClick;

public class IzostanakAdapter extends RecyclerView.Adapter<IzostanakAdapter.IzostanakHolder> {

    /**
     * Lista svih izostanaka.
     */
    private List<Izostanak> izostanci = new ArrayList<>();
    /**
     * Kontekst iz kojeg se poziva adapter.
     */
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    /**
     * Funkcija za postavljanje izostanaka koji da se nalaze u recyclerViewu.
     * @param prosljedeniIzostanci Lista Izostanaka koji se trebaju nalaziti u recyclerViewu.
     */
    public void setIzostanci(List<Izostanak> prosljedeniIzostanci){
        izostanci = prosljedeniIzostanci;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IzostanakHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.izostanak_item,parent,false);
        return new IzostanakAdapter.IzostanakHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IzostanakHolder holder, int position) {
        Izostanak trenutniIzostanak = izostanci.get(position);
        holder.tvDatumIzostanka.setText(trenutniIzostanak.getDatumIzostanka());
        holder.tvRazlog.setText(trenutniIzostanak.getRazlogIzostanka());
    }

    /**
     * Funkcija za dohvacanje broja izostanaka.
     * @return cjelobrojnu vrijednost broja izostanaka.
     */
    @Override
    public int getItemCount() {
        return izostanci.size();
    }


    /**
     * Funkcija vraca Izostanak na odredenoj poziciji.
     * @param pozicija cjelobrojna vrijednost pozicije.
     * @return Izostanak na k-toj poziciji.
     */
    public Izostanak getIzostanakAt(int pozicija) {
        return izostanci.get(pozicija);
    }

    /**
     * Funkcija briše Izostanak na k-toj poziciji.
     * @param pozicija pozicija s koje zelimo izbrisati Izostanak.
     */
    public void removeIzostanakAt(int pozicija){
        izostanci.remove(pozicija);
    }

    public class IzostanakHolder extends RecyclerView.ViewHolder{
        /**
         * Varijabla za upravljanje elementom forme, prikaz tekstualnog zapisa datuma izostanka unutar viewholdera izostanka.
         */
        private TextView tvDatumIzostanka;
        /**
         * Varijabla za upravljanje elementom forme, prikaz tekstualnog zapisa razloga izostanka unutar viewholdera izostanka.
         */
        private TextView tvRazlog;
        /**
         * Varijabla za upravljanje elementima forme, koja sprema trenutni pogled unutar adaptera.
         */
        private View pogled;


        public IzostanakHolder(@NonNull View itemView) {
            super(itemView);
            pogled = itemView;
            tvDatumIzostanka = pogled.findViewById(R.id.tvDatumIzostanka);
            tvRazlog = pogled.findViewById(R.id.tvRazlogIzostanka);
        }
    }
}
