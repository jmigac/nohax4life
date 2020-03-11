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

    private List<Izostanak> izostanci = new ArrayList<>();
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

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

    @Override
    public int getItemCount() {
        return izostanci.size();
    }

    public Izostanak getIzostanakAt(int pozicija) {
        return izostanci.get(pozicija);
    }
    public void removeIzostanakAt(int pozicija){
        izostanci.remove(pozicija);
    }

    public class IzostanakHolder extends RecyclerView.ViewHolder{

        private TextView tvDatumIzostanka;
        private TextView tvRazlog;
        private View pogled;


        public IzostanakHolder(@NonNull View itemView) {
            super(itemView);
            pogled = itemView;
            tvDatumIzostanka = pogled.findViewById(R.id.tvDatumIzostanka);
            tvRazlog = pogled.findViewById(R.id.tvRazlogIzostanka);
        }
    }
}
