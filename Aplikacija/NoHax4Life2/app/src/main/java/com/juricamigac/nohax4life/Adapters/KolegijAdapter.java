package com.juricamigac.nohax4life.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.juricamigac.core.entiteti.Kolegij;
import com.juricamigac.database.MyDatabase;
import com.juricamigac.nohax4life.MainActivity;
import com.juricamigac.nohax4life.R;
import com.juricamigac.repozitorij.IzostanakDAL;

import java.util.ArrayList;
import java.util.List;

import interfaces.OnKolegijClick;

public class KolegijAdapter extends RecyclerView.Adapter<KolegijAdapter.KolegijHolder> {

    private List<Kolegij> kolegiji = new ArrayList<>();
    private Context context;
    private OnKolegijClick onKolegijClick;
    private Observer<Integer> obsTrenutniBrojIzostanaka;

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
    public void onBindViewHolder(@NonNull final KolegijHolder holder, final int position) {
        final Kolegij trenutniKolegij = kolegiji.get(position);
        String izostanci = "";
        int brojIzostanaka = MyDatabase.getInstance(context).getIzostanakDAO().dohvatiBrojIzostanakaOdredenogKolegija(trenutniKolegij.getId());
        int brojMogucihIzostanaka = trenutniKolegij.getBrojIzostanaka();
        izostanci = brojIzostanaka+"/"+brojMogucihIzostanaka;


        IzostanakDAL.DohvatiBrojIzostanakaLIVE(context,trenutniKolegij.getId()).observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String izostanci = "";
                int brojIzostanaka = MyDatabase.getInstance(context).getIzostanakDAO().dohvatiBrojIzostanakaOdredenogKolegija(kolegiji.get(position).getId());
                int brojMogucihIzostanaka = kolegiji.get(position).getBrojIzostanaka();
                izostanci = brojIzostanaka + "/" + brojMogucihIzostanaka;
                holder.tvIzostanci.setText(izostanci);
                try {
                    float omjer = ((float)brojIzostanaka / (float)brojMogucihIzostanaka)*100;

                    if(omjer < 35){
                        holder.iVOmjerBrojaIzostanaka.setBackgroundColor(Color.rgb(92,184,92));
                    }
                    if(omjer >=35 && omjer <75){
                        //rgb(240,173,78)
                        holder.iVOmjerBrojaIzostanaka.setBackgroundColor(Color.rgb(240,173,78));
                    }
                    if(omjer>=75 && omjer<=100){
                        //rgb(217,83,79)
                        holder.iVOmjerBrojaIzostanaka.setBackgroundColor(Color.rgb(217,83,79));
                    }
                }catch (ArithmeticException ae){
                    Log.e("Exception",ae.getMessage());
                }
            }
        });

        holder.tvNazivKolegija.setText(trenutniKolegij.getNaziv());
        holder.tvIzostanci.setText(izostanci);
        holder.tvNacinIzvodenja.setText(trenutniKolegij.getNazivIzvodenjaKolegija());
    }

    public Kolegij getKolegijAtPosition(int position){
        return kolegiji.get(position);
    }
    public void removeKolegijAtPosition(int position){
        kolegiji.remove(position);
    }

    @Override
    public int getItemCount() {
        return kolegiji.size();
    }

    public class KolegijHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNazivKolegija;
        private TextView tvIzostanci;
        private TextView tvNacinIzvodenja;
        private View pogled;
        private OnKolegijClick onKolegijClickListener;

        private RelativeLayout relativeLayout;
        private ImageView iVOmjerBrojaIzostanaka;

        public KolegijHolder(@NonNull View itemView, OnKolegijClick onKolegijClickListener) {
            super(itemView);
            pogled = itemView;
            relativeLayout = pogled.findViewById(R.id.kolegijRelativeLayout);
            tvNazivKolegija = pogled.findViewById(R.id.tvRazlogIzostanka);
            tvIzostanci = pogled.findViewById(R.id.tvDatumIzostanka);
            tvNacinIzvodenja = pogled.findViewById(R.id.tvNacinIzvodenja);
            iVOmjerBrojaIzostanaka = pogled.findViewById(R.id.iVOmjerBrojaIzostanaka);
            this.onKolegijClickListener = onKolegijClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onKolegijClickListener.onKolegijClick(getAdapterPosition());
        }

    }
}
