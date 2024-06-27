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

public class WordAdapter4 extends RecyclerView.Adapter<WordAdapter4.WordHolder4> {

    ArrayList<Word> wordArrayList;
    public WordAdapter4 (ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }
    public void setFilteredList(ArrayList<Word> filteredList){
        this.wordArrayList.clear();
        this.wordArrayList.addAll(filteredList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WordHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new WordHolder4(recyclerRowBinding);

    }


    @Override
    public void onBindViewHolder(@NonNull WordAdapter4.WordHolder4 holder, @SuppressLint({"RecyclerView"}) int position) {
        holder.binding.recyclerViewTextView.setText(wordArrayList.get(position).english);
        holder.binding.recyclerViewTextView2.setText(wordArrayList.get(position).turkish);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DictionaryActivity4.class);
                intent.putExtra("info","oldddd");
                intent.putExtra("wordId",wordArrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }

    public static class WordHolder4 extends RecyclerView.ViewHolder {
        private final RecyclerRowBinding binding;

        public WordHolder4 (RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

