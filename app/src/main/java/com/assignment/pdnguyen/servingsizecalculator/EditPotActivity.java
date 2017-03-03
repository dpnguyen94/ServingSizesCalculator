package com.assignment.pdnguyen.servingsizecalculator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pot);

        setUpInitialValue();
    }

    private void setUpInitialValue() {
        Intent intent = getIntent();
        EditText currentpotname = (EditText) findViewById(R.id.edttxt_currentpotname);
        currentpotname.setText(intent.getStringExtra("pot_name"));
        EditText currentpotweight = (EditText) findViewById(R.id.edttxt_currentpotweight);
        currentpotweight.setText("" + intent.getIntExtra("pot_weight", 0));
        int pot_position = intent.getIntExtra("pot_position", -1);

        setUpSaveButton(pot_position);
        setUpDeleteButton(pot_position);
    }

    private void setUpSaveButton(final int position) {
        Button btn = (Button) findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textPotName = (EditText) findViewById(R.id.edttxt_currentpotname);
                EditText textPotWeight = (EditText) findViewById(R.id.edttxt_currentpotweight);

                String pot_name = textPotName.getText().toString();
                String pot_weight = textPotWeight.getText().toString();

                boolean all_valid = true;

                if (!isValidPotName(pot_name)) {
                    textPotName.setError("Pot name must have at least one character");
                    all_valid = false;
                }

                if (!isValidPotWeight(pot_weight)) {
                    textPotWeight.setError("Pot weight must be a positive integer");
                    all_valid = false;
                }

                if (all_valid) {
                    Intent intent = new Intent();
                    intent.putExtra("pot_name", pot_name);
                    intent.putExtra("pot_weight", pot_weight);
                    intent.putExtra("pot_position", position);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            private boolean isValidPotName(String pot_name) {
                return pot_name.length() > 0;
            }

            private boolean isValidPotWeight(String pot_weight) {
                if (pot_weight.length() == 0) return false;

                int value = Integer.parseInt(pot_weight);
                return value > 0;
            }
        });
    }

    private void setUpDeleteButton(final int position) {
        Button btn = (Button) findViewById(R.id.btn_delete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pot_position", position);
                setResult(Activity.RESULT_FIRST_USER, intent);
                finish();
            }
        });
    }

    public static Pot getPotFromIntent(Intent data) {
        String pot_name = data.getStringExtra("pot_name");
        String pot_weight = data.getStringExtra("pot_weight");

        return new Pot(pot_name, Integer.parseInt(pot_weight));
    }
}
