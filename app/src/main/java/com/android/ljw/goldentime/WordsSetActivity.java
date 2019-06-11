package com.android.ljw.goldentime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WordsSetActivity extends AppCompatActivity
{
    private SharedPreferences wordsData;
    String words, wordsSOS;
    EditText editText, editTextSOS;
    Button btn_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_set);

        editText = findViewById(R.id.editText);
        editTextSOS = findViewById(R.id.editTextSOS);
        btn_set = findViewById(R.id.btn_set);

        wordsData = getSharedPreferences("wordsData", MODE_PRIVATE);
        load();

        editText.setText(words);
        editTextSOS.setText(wordsSOS);

        btn_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                save();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void save() {
        SharedPreferences.Editor editor = wordsData.edit();

        editor.putString("WORDS", editText.getText().toString());
        editor.putString("WORDS_SOS", editTextSOS.getText().toString());

        editor.apply();
    }

    private void load() {
        words = wordsData.getString("WORDS", "");
        wordsSOS = wordsData.getString("WORDS_SOS", "");
    }
}
