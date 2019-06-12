package com.android.ljw.goldentime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NumSetActivity extends AppCompatActivity
{
    EditText editText1, editText2, editText3, editText4, editText5;
    Button btn_plus1, btn_plus2, btn_plus3, btn_plus4, btn_plus5;
    Button btn_minus1, btn_minus2, btn_minus3, btn_minus4, btn_minus5;
    Button btn_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_set);

        btn_set = findViewById(R.id.btn_set);

        btn_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void delete(EditText editText) {
        editText.setText("");
    }
}
