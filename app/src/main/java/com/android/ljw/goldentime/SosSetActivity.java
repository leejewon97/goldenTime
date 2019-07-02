package com.android.ljw.goldentime;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ljw.goldentime.databinding.ActivitySosSetBinding;

public class SosSetActivity extends AppCompatActivity
{
    private SharedPreferences numData;
    ActivitySosSetBinding sosSetBinding;
    Button[] nums;
    String[] key;
    String sosNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sosSetBinding = DataBindingUtil.setContentView(this, R.layout.activity_sos_set);

        nums = new Button[]{sosSetBinding.button, sosSetBinding.button2, sosSetBinding.button3, sosSetBinding.button4, sosSetBinding.button5};
        key = new String[]{"TEXT1", "TEXT2", "TEXT3", "TEXT4", "TEXT5"};

        numData = getSharedPreferences("numData", MODE_PRIVATE);
        sosNum = numData.getString("SOS", "");

        load();

        for (int i = 0; i < 5; i++) {
            nums[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Log.e("click", "onClick");
                    sosNum = numData.getString("SOS", "");
                    SharedPreferences.Editor editor = numData.edit();

                    for (int j = 0; j < 5; j++) {
                        if (view.getId() == nums[j].getId() && !nums[j].getText().equals("")) {
                            if (sosNum.equals("")) {
                                for (int k = 0; k < 5; k++) {
                                    nums[k].setEnabled(false);
                                }
                                nums[j].setEnabled(true);
                                editor.putString("SOS", nums[j].getText().toString());
                            } else {
                                for (int k = 0; k < 5; k++) {
                                    nums[k].setEnabled(true);
                                }
                                editor.remove("SOS");
                            }
                            editor.apply();
                        }
                    }
                }
            });
        }
    }

    private void load() {
        for (int i = 0; i < 5; i++)
            nums[i].setText(numData.getString(key[i], ""));
        if (!sosNum.equals("")) {
            for (int i = 0; i < 5; i++) {
                nums[i].setEnabled(false);
                if (nums[i].getText().equals(sosNum)) {
                    nums[i].setEnabled(true);
                }
            }
        }
    }
}
