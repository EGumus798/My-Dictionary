package com.enesgumus.mydictionary;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestActivity3 extends AppCompatActivity {

    private List<Word> questions;  // Soruların listesi
    private int userScore = 0;

    private int currentQuestionIndex;  // Şu anki sorunun indeksi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // MainActivity'de tanımlı veritabanından kelimeleri al
        DatabaseHelper3 dbHelper = new DatabaseHelper3(this);
        List<Word> words = dbHelper.getAllWords();

        // Rastgele soruları seç ve karıştır
        questions = getRandomQuestions(words, 5);  // Örneğin, 5 soru seçildi

        currentQuestionIndex = 0;  // İlk soru
        Button choiceButton1 = findViewById(R.id.choiceButton1);
        Button choiceButton2 = findViewById(R.id.choiceButton2);
        Button choiceButton3 = findViewById(R.id.choiceButton3);
        Button choiceButton4 = findViewById(R.id.choiceButton4);


        startQuiz();
    }
    private List<Word> getRandomQuestions(List<Word> words, int numberOfQuestions) {
        // Eğer veritabanında yeterince kelime yoksa, hepsini soru olarak seç
        if (words.size() <= numberOfQuestions) {
            return words;
        }

        // Kelimeleri karıştır
        Collections.shuffle(words);

        // İstenen sayıda rastgele soruyu seç
        return words.subList(0, numberOfQuestions);
    }
    public void startQuiz() {
        if (currentQuestionIndex < questions.size()) {
            // Test devam ediyor
            resetButtonColors();
            Word currentWord = questions.get(currentQuestionIndex);
            displayQuestion(currentWord);
        } else {
            // Test bitti
            finish();
            displayUserScore();
        }
    }
    private void displayUserScore() {
        Toast.makeText(this, "Finished Test, Your Score: " + userScore, Toast.LENGTH_LONG).show();
    }

    public void displayQuestion(Word word) {
        // Soruyu göstermek için gerekli işlemleri yapın
        // Örneğin, soruyu ekrana yazdırabilirsiniz
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText("Translate: " + word.getEnglish());

        // Şıkları oluştur ve karıştır
        List<String> choices = getRandomChoices(word);
        Collections.shuffle(choices);
        resetButtonColors();
        // Şıkları ekrana yazdır
        Button choiceButton1 = findViewById(R.id.choiceButton1);
        Button choiceButton2 = findViewById(R.id.choiceButton2);
        Button choiceButton3 = findViewById(R.id.choiceButton3);
        Button choiceButton4 = findViewById(R.id.choiceButton4);

        if (choices.size() >= 4) {  // choices listesinin boyutunu kontrol et
            choiceButton1.setText(choices.get(0));
            choiceButton2.setText(choices.get(1));
            choiceButton3.setText(choices.get(2));
            choiceButton4.setText(choices.get(3));
        } else {
            // choices listesinde yeterli sayıda şık yoksa, burada hata mesajı verilir.
            Toast.makeText(this, "Hata: Yeterli sayıda şık bulunamadı", Toast.LENGTH_SHORT).show();
            finish();  // Testi sonlandır
        }

        // Butonlara tıklama olaylarını ekle
        choiceButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(choices.get(0));
            }
        });

        choiceButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(choices.get(1));
            }
        });

        choiceButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(choices.get(2));
            }
        });

        choiceButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(choices.get(3));
            }
        });

    }

    // getRandomChoices metodu
    public List<String> getRandomChoices(Word correctWord) {
        List<String> choices = new ArrayList<>();

        // Doğru cevabı ekle
        choices.add(correctWord.getTurkish());

        // Diğer kelimeleri veritabanından rastgele seç
        DatabaseHelper3 dbHelper = new DatabaseHelper3(this);
        List<Word> allWords = dbHelper.getAllWords();

        // Doğru cevabı içeren kelimenin veritabanındaki indexini bul
        int correctWordIndex = allWords.indexOf(correctWord);

        // Doğru cevap dışında rastgele 3 kelime seç
        for (int i = 0; i < 3; i++) {
            int randomIndex;
            do {
                randomIndex = new Random().nextInt(allWords.size());
            } while (randomIndex == correctWordIndex || choices.contains(allWords.get(randomIndex).getTurkish()));

            choices.add(allWords.get(randomIndex).getTurkish());
        }

        return choices;
    }


    // checkAnswer metodu
    public void checkAnswer(String selectedChoice) {
        Button choiceButton1 = findViewById(R.id.choiceButton1);
        Button choiceButton2 = findViewById(R.id.choiceButton2);
        Button choiceButton3 = findViewById(R.id.choiceButton3);
        Button choiceButton4 = findViewById(R.id.choiceButton4);

        // Kullanıcının seçimini kontrol etmek için gerekli işlemleri yapın
        Word currentWord = questions.get(currentQuestionIndex);

        if (selectedChoice.equals(choiceButton1.getText())) {
            if (currentWord.getTurkish().equals(choiceButton1.getText())) {
                // Doğru cevap
                choiceButton1.setBackgroundResource(R.color.green);
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                userScore++;
            } else {
                // Yanlış cevap
                choiceButton1.setBackgroundResource(R.color.red);
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        } else if (selectedChoice.equals(choiceButton2.getText())) {
            if (currentWord.getTurkish().equals(choiceButton2.getText())) {
                // Doğru cevap
                choiceButton2.setBackgroundResource(R.color.green);
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                userScore++;
            } else {
                // Yanlış cevap
                choiceButton2.setBackgroundResource(R.color.red);
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        } else if (selectedChoice.equals(choiceButton3.getText())) {
            if (currentWord.getTurkish().equals(choiceButton3.getText())) {
                // Doğru cevap
                choiceButton3.setBackgroundResource(R.color.green);
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                userScore++;
            } else {
                // Yanlış cevap
                choiceButton3.setBackgroundResource(R.color.red);
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        } else if (selectedChoice.equals(choiceButton4.getText())) {
            if (currentWord.getTurkish().equals(choiceButton4.getText())) {
                // Doğru cevap
                choiceButton4.setBackgroundResource(R.color.green);
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                userScore++;
            } else {
                // Yanlış cevap
                choiceButton4.setBackgroundResource(R.color.red);
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        }

        // Bir sonraki soruya geç
        currentQuestionIndex++;


    }
    public void onNextButtonClick(View view) {
        // Bir sonraki soruya geç

        // Kontrol et: Eğer tüm soruları geçtiyse testi bitir
        if (currentQuestionIndex < questions.size()) {
            Word nextWord = questions.get(currentQuestionIndex);
            displayQuestion(nextWord);
        } else {
            finish();
            startQuiz();
        }
    }

    private void resetButtonColors() {
        Button choiceButton1 = findViewById(R.id.choiceButton1);
        Button choiceButton2 = findViewById(R.id.choiceButton2);
        Button choiceButton3 = findViewById(R.id.choiceButton3);
        Button choiceButton4 = findViewById(R.id.choiceButton4);

        // Renkleri sıfırla
        choiceButton1.setBackgroundResource(android.R.drawable.btn_default);
        choiceButton2.setBackgroundResource(android.R.drawable.btn_default);
        choiceButton3.setBackgroundResource(android.R.drawable.btn_default);
        choiceButton4.setBackgroundResource(android.R.drawable.btn_default);
    }

}
