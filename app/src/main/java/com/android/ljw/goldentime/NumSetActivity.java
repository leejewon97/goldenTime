package com.android.ljw.goldentime;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.ljw.goldentime.databinding.ActivityNumSetBinding;

public class NumSetActivity extends AppCompatActivity
{
    ActivityNumSetBinding numSetBinding;
    Button[] minus;
    EditText[] num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numSetBinding = DataBindingUtil.setContentView(this, R.layout.activity_num_set);

        minus = new Button[]{numSetBinding.btnMinus1, numSetBinding.btnMinus2, numSetBinding.btnMinus3, numSetBinding.btnMinus4, numSetBinding.btnMinus5};
        num = new EditText[]{numSetBinding.editText1, numSetBinding.editText2, numSetBinding.editText3, numSetBinding.editText4, numSetBinding.editText5};

        numSetBinding.btnSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        for (int i = 0; i < 5; i++) {
            minus[i].setOnClickListener(btnListener);
        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            for (int i = 0; i < 5; i++){
                if (view.getId() == minus[i].getId()){
                    delete(num[i]);
                }
            }
        }
    };

    private void delete(EditText editText) {
        editText.setText("");
    }
}
