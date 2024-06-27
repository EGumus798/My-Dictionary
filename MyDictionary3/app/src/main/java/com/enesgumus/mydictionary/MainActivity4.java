package com.enesgumus.mydictionary;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.enesgumus.mydictionary.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {

    private ActivityMainBinding binding;
    ArrayList<Word> wordArrayList;
    WordAdapter4 wordAdapter4;
    private SearchView searchView;
    private ImageButton btnStartTest;
    private ArrayList<Word> originalWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        searchView = findViewById(R.id.searchView);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    hideKeyboard();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterList(newText);
                    return true;
                }
            });



            searchView.setIconifiedByDefault(false);
            searchView.setFocusable(false);
            searchView.setIconified(true);

        wordArrayList = new ArrayList<>();
        originalWordList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordAdapter4 = new WordAdapter4(wordArrayList);
        binding.recyclerView.setAdapter(wordAdapter4);
        getData();
        originalWordList.addAll(wordArrayList);
        btnStartTest = findViewById(R.id.btnStartTest);

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTestActivity4();
            }
        });
    }
    private void startTestActivity4() {
        Intent intent = new Intent(MainActivity4.this, TestActivity4.class);
        startActivity(intent);
    }

    private void hideKeyboard() {  View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filterList(String text) {
        ArrayList<Word> filteredList = new ArrayList<>();
        for (Word word : originalWordList){
            if (word.getEnglish().toLowerCase().contains(text.toLowerCase()) ||
                    word.getTurkish().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(word);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            wordAdapter4.setFilteredList(filteredList);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getData(){
        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Wordssss", MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM wordssss", null);
            int englishIx = cursor.getColumnIndex("englishname");
            int turkishIx = cursor.getColumnIndex("turkishname");
            int idIx = cursor.getColumnIndex("id");
            while (cursor.moveToNext()) {
                String english = cursor.getString(englishIx);
                String turkish = cursor.getString(turkishIx);
                int id = cursor.getInt(idIx);
                Word word = new Word(english,turkish,id);
                wordArrayList.add(word);
            }
            wordAdapter4.notifyDataSetChanged();

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dictionary_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_word){
            Intent intent = new Intent(this,DictionaryActivity4.class);
            intent.putExtra("info","newwww");
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

}