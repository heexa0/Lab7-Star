package com.example.lab7.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lab7.R;
import com.example.lab7.beans.Star;
import com.example.lab7.service.StarService;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder>
        implements Filterable {

    private List<Star> stars;
    private List<Star> starsFilter;
    private final Context context;
    private final NewFilter mFilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFilter = new ArrayList<>(stars);
        this.mFilter = new NewFilter(this);
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.star_item, parent, false);

        final StarViewHolder holder = new StarViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                Star currentStar = starsFilter.get(position);

                // Gonfler la vue du popup
                View popup = LayoutInflater.from(context)
                        .inflate(R.layout.star_edit_item, null, false);

                final ImageView popupImg = popup.findViewById(R.id.img);
                final RatingBar popupBar = popup.findViewById(R.id.ratingBar);
                final TextView  popupId  = popup.findViewById(R.id.idss);

                // Pré-remplir avec les données de la star cliquée
                Glide.with(context)
                        .load(currentStar.getImg())
                        .into(popupImg);
                popupBar.setRating(currentStar.getStar());
                popupId.setText(String.valueOf(currentStar.getId()));

                // Afficher le dialog
                new AlertDialog.Builder(context)
                        .setTitle("Notez :")
                        .setMessage("Donnez une note entre 1 et 5 :")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float nouvelleNote = popupBar.getRating();
                                int starId = Integer.parseInt(
                                        popupId.getText().toString());

                                Star starToUpdate =
                                        StarService.getInstance().findById(starId);
                                if (starToUpdate != null) {
                                    starToUpdate.setStar(nouvelleNote);
                                    StarService.getInstance().update(starToUpdate);
                                }
                                int pos = holder.getAdapterPosition();
                                if (pos != RecyclerView.NO_POSITION) {
                                    notifyItemChanged(pos);
                                }
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Star s = starsFilter.get(position);

        Glide.with(context)
                .asBitmap()
                .load(s.getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.img);

        holder.name.setText(s.getName().toUpperCase());
        holder.stars.setRating(s.getStar());
        holder.idss.setText(String.valueOf(s.getId()));
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    // ── ViewHolder ──────────────────────────────────────────
    public class StarViewHolder extends RecyclerView.ViewHolder {

        TextView  idss;   // R.id.ids   → caché
        TextView  name;   // R.id.name
        ImageView img;    // R.id.img
        RatingBar stars;  // R.id.stars

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss  = itemView.findViewById(R.id.ids);
            img   = itemView.findViewById(R.id.img);
            name  = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
        }
    }

    // ── Filtre ──────────────────────────────────────────────
    public class NewFilter extends Filter {

        private final RecyclerView.Adapter mAdapter;

        public NewFilter(RecyclerView.Adapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filtered = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filtered.addAll(stars);
            } else {
                String pattern = charSequence.toString()
                        .toLowerCase().trim();
                for (Star s : stars) {
                    if (s.getName().toLowerCase().startsWith(pattern)) {
                        filtered.add(s);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;
            results.count  = filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence,
                                      FilterResults filterResults) {
            starsFilter = (List<Star>) filterResults.values;
            mAdapter.notifyDataSetChanged();
        }
    }
}