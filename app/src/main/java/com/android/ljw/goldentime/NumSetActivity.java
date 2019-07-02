package com.android.ljw.goldentime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.ljw.goldentime.databinding.ActivityNumSetBinding;

public class NumSetActivity extends AppCompatActivity
{
    private SharedPreferences numData;
    ActivityNumSetBinding numSetBinding;
    Button[] plus;
    Button[] minus;
    EditText[] num;
    String[] key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numSetBinding = DataBindingUtil.setContentView(this, R.layout.activity_num_set);

        plus = new Button[]{numSetBinding.btnPlus1, numSetBinding.btnPlus2, numSetBinding.btnPlus3, numSetBinding.btnPlus4, numSetBinding.btnPlus5};
        minus = new Button[]{numSetBinding.btnMinus1, numSetBinding.btnMinus2, numSetBinding.btnMinus3, numSetBinding.btnMinus4, numSetBinding.btnMinus5};
        num = new EditText[]{numSetBinding.editText1, numSetBinding.editText2, numSetBinding.editText3, numSetBinding.editText4, numSetBinding.editText5};
        key = new String[]{"TEXT1", "TEXT2", "TEXT3", "TEXT4", "TEXT5"};

        numData = getSharedPreferences("numData", MODE_PRIVATE);
        load();

        numSetBinding.btnSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                sort();
                save();
                onBackPressed();
            }
        });
        for (int i = 0; i < 5; i++) {
            minus[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < 5; j++) {
                        if (view.getId() == minus[j].getId())
                            num[j].getText().clear();
                    }
                }
            });
        }
        for (int i = 0; i < 5; i++) {
            plus[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    int index = 0;
                    for (int j = 0; j < 5; j++) {
                        if (view.getId() == plus[j].getId()){
                            index = j;
                            break;
                        }
                    }
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, index);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null, null, null);

            cursor.moveToFirst();

            // 1이면 전화번호 획득
            num[requestCode].setText(cursor.getString(1));
            cursor.close();
        }
    }

    private void sort() {
        Log.e("sort", "실행");
        for (int i = 0; i < 4; i++) {
            //string.equals("")가 안되서 TextUtils.isEmpty(string)로 대체했다
            if (TextUtils.isEmpty(num[i].getText())) {
                for (int k = i + 1; k < 5; k++) {
                    if (!TextUtils.isEmpty(num[k].getText())) {
                        num[i].setText(num[k].getText());
                        num[k].getText().clear();
                        break;
                    }
                }
            }
        }
    }

    private void save() {
        SharedPreferences.Editor editor = numData.edit();

        for (int i = 0; i < 5; i++)
            editor.putString(key[i], num[i].getText().toString());

        editor.apply();
    }

    private void load() {
        for (int i = 0; i < 5; i++)
            num[i].setText(numData.getString(key[i], ""));
    }
}
