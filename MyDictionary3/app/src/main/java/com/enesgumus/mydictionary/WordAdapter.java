package com.enesgumus.mydictionary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enesgumus.mydictionary.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {

    ArrayList<Word> wordArrayList;
    public WordAdapter(ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }
    public void setFilteredList(ArrayList<Word> filteredList){
        this.wordArrayList.clear();
        this.wordArrayList.addAll(filteredList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new WordHolder(recyclerRowBinding);

    }


    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordHolder holder, @SuppressLint({"RecyclerView"}) int position) {
        holder.binding.recyclerViewTextView.setText(wordArrayList.get(position).english);
        holder.binding.recyclerViewTextView2.setText(wordArrayList.get(position).turkish);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DictionaryActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("wordId",wordArrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }

    public static class WordHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowBinding binding;

        public WordHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
