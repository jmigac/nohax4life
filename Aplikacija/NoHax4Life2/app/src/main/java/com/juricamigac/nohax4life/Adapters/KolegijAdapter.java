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

    /**
     * Lista svih kolegija među kojima će svaki element biti prikazan u viewHolderu.
     */
    private List<Kolegij> kolegiji = new ArrayList<>();
    /**
     * Kontekst iz kojeg se kreira adapter.
     */
    private Context context;
    /**
     * Varijabla za pristup sučelju kako bi se mogli prenijeti podaci klasi koja poziva adapter.
     */
    private OnKolegijClick onKolegijClick;

    /**
     * Funkcija za postavljanje konteksta iz kojeg se poziva adapter.
     * @param context trenutni kontekst aktivnosti gdje se nalazi recyclerView.
     */
    public void setContext(Context context){
        this.context = context;
    }

    /**
     * Funkcija za postavljanje kolegija koji će se nalaziti unutar adaptera, recyclerViewa.
     * @param prosljedeniKolegiji Kolegiji koji će se nalazitu unutar viewHoldera, tj. adaptera.
     */
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
                /**
                 * Broj trenutnih izostanaka s kolegija.
                 */
                int brojIzostanaka = MyDatabase.getInstance(context).getIzostanakDAO().dohvatiBrojIzostanakaOdredenogKolegija(kolegiji.get(position).getId());
                /**
                 * Maksimalan broj dozvoljenih izostanaka s određenog kolegija.
                 */
                int brojMogucihIzostanaka = kolegiji.get(position).getBrojIzostanaka();
                izostanci = brojIzostanaka + "/" + brojMogucihIzostanaka;
                holder.tvIzostanci.setText(izostanci);
                try {
                    /**
                     * Omjer preko kojeg se izračunava te kasnije postavlja određena boja pozadine za vizualnu
                     * reprezentaciju broja izostanaka određenog kolegija.
                     *  - Crvena 75% ili više izostanaka s kolegija.
                     *  - Žuta više od 35% i manje od 75% izostanaka s kolegija.
                     *  - Zelena manje od 35% izostanaka.
                     */
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

    /**
     * Funkcija za dohvaćanje objekta Kolegij na određenoj poziciji unutar liste.
     * @param position Pozicija objekta unutar liste.
     * @return objekt Kolegij.
     */
    public Kolegij getKolegijAtPosition(int position){
        return kolegiji.get(position);
    }

    /**
     * Funkcija za brisanje kolegija na određenoj poziciji.
     * @param position pozicija s koje želimo izbrisati kolegij iz liste.
     */
    public void removeKolegijAtPosition(int position){
        kolegiji.remove(position);
    }

    /**
     * Funkcija za dohvaćanje broja kolegija unutar liste.
     * @return cjelobrojnu vrijednost broja izostanaka.
     */
    @Override
    public int getItemCount() {
        return kolegiji.size();
    }

    public class KolegijHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /**
         * Varijabla za upravljanje elementom forme, prikaz tekstualnog zapisa naziva kolegija.
         */
        private TextView tvNazivKolegija;
        /**
         * Varijabla za upravljanje elementom forme, prikaz tekstualnog zapisa o broju (omjeru) izostanaka.
         */
        private TextView tvIzostanci;
        /**
         * Varijabla za upravljanje elementom forme, prikaz tekstualnog zapisa o načinu izvođenja kolegija.
         */
        private TextView tvNacinIzvodenja;
        /**
         * Varijabla za upravljanje elementima forme.
         */
        private View pogled;
        /**
         * Varijabla za sučelja za prosljeđivanje podataka nadklasi koja je pozvala adapter.
         */
        private OnKolegijClick onKolegijClickListener;

        /**
         * Varijabla za upravljanje elementom forme, prikaz slikovnog dijela broja izostanaka.
         */
        private ImageView iVOmjerBrojaIzostanaka;

        public KolegijHolder(@NonNull View itemView, OnKolegijClick onKolegijClickListener) {
            super(itemView);
            pogled = itemView;
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
