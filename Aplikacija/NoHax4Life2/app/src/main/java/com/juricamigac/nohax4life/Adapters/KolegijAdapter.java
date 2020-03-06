package com.juricamigac.nohax4life.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.nohax4life.R;

import java.util.ArrayList;
import java.util.List;

import interfaces.OnKolegijClick;

public class KolegijAdapter extends RecyclerView.Adapter<KolegijAdapter.KolegijHolder> {

    private List<Kolegij> kolegiji = new ArrayList<>();
    private Context context;
    private OnKolegijClick onKolegijClick;

    public void setContext(Context context){
        this.context = context;
    }

    public void setKolegiji(List<Kolegij> prosljedeniKolegiji){
        kolegiji = prosljedeniKolegiji;
        notifyDataSetChanged();
    }
    public KolegijAdapter(OnKolegijClick onKolegijClick){
        this.onKolegijClick = onKolegijClick;
    }
    @NonNull
    @Override
    public KolegijHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kolegij_item,parent,false);
        return new KolegijAdapter.KolegijHolder(itemView,onKolegijClick);
    }

    @Override
    public void onBindViewHolder(@NonNull KolegijHolder holder, int position) {
        Kolegij trenutniKolegij = kolegiji.get(position);
        String izostanci = "";
        int brojIzostanaka = MyDatabase.getInstance(context).getIzostanakDAO().dohvatiBrojIzostanakaOdredenogKolegija(trenutniKolegij.getId());
        int brojMogucihIzostanaka = trenutniKolegij.getBrojIzostanaka();
        izostanci = brojIzostanaka+"/"+brojMogucihIzostanaka;
        holder.tvNazivKolegija.setText(trenutniKolegij.getNaziv());
        holder.tvIzostanci.setText(izostanci);
        holder.tvNacinIzvodenja.setText(trenutniKolegij.getNazivIzvodenjaKolegija());
        holder.idKolegija = trenutniKolegij.getId();
    }

    @Override
    public int getItemCount() {
        return kolegiji.size();
    }

    public class KolegijHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int idKolegija;
        private TextView tvNazivKolegija;
        private TextView tvIzostanci;
        private TextView tvNacinIzvodenja;
        private View pogled;
        private OnKolegijClick onKolegijClickListener;


        public KolegijHolder(@NonNull View itemView, OnKolegijClick onKolegijClickListener) {
            super(itemView);
            pogled = itemView;
            tvNazivKolegija = pogled.findViewById(R.id.tvRazlogIzostanka);
            tvIzostanci = pogled.findViewById(R.id.tvDatumIzostanka);
            tvNacinIzvodenja = pogled.findViewById(R.id.tvNacinIzvodenja);
            this.onKolegijClickListener = onKolegijClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onKolegijClickListener.onKolegijClick(getAdapterPosition());
        }

    }
}
