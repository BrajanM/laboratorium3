package com.laboratorium.wilczek.laboratorium3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sylwia on 1/6/2018.
 */

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.ViewHolder> {

    private ArrayList<String> dogs = new ArrayList<>();
    private RecyclerViewClickListener listener;

    DogsListAdapter(ArrayList<String> dogs, RecyclerViewClickListener listener) {
        this.dogs = dogs;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, null);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDogBreed(dogs.get(position));
    }

    @Override
    public int getItemCount() {
        return dogs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener listener;

        @BindView(R.id.breed_tv)
        TextView breed_tv;

        @BindView(R.id.show_breed)
        Button showBreed;

        ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            showBreed.setOnClickListener(this);
        }

        void setDogBreed(String dogBreed) {
            breed_tv.setText(dogBreed);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), breed_tv.getText().toString());
        }
    }
}
