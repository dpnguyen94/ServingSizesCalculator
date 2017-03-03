package com.assignment.pdnguyen.servingsizecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.assignment.pdnguyen.servingsizecalculator.R.string.pot_weight;

public class CalculateServingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_serving);

        setUpInitialValue();
        setUpBackButton();
    }


    private void setUpInitialValue() {
        Intent intent = getIntent();

        String pot_name = intent.getStringExtra("pot_name");
        TextView pot = (TextView) findViewById(R.id.txtvw_pot);
        pot.setText(pot_name);

        final int pot_weight = intent.getIntExtra("pot_weight", 0);
        TextView weightempty = (TextView) findViewById(R.id.txtvw_weightempty);
        weightempty.setText("" + pot_weight);

        setUpEditText(pot_weight);
    }


    private void setUpEditText(final int pot_weight) {
        final EditText weightwithfood = (EditText) findViewById(R.id.edttxt_weightwithfood);
        final EditText numberserving = (EditText) findViewById(R.id.edttxt_numberserving);

        final TextView weightoffood = (TextView) findViewById(R.id.txtvw_weightoffood);
        final TextView servingweight = (TextView) findViewById(R.id.txtvw_servingweight);

        weightwithfood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int value = Math.max(getInt(weightwithfood) - pot_weight, 0);
                int number = getInt(numberserving);

                updateText(weightoffood, value);

                if (number == 0) updateText(servingweight, 0);
                else updateText(servingweight, value / number);
            }
        });

        numberserving.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int value = Math.max(getInt(weightwithfood) - pot_weight, 0);
                int number = getInt(numberserving);

                if (number == 0) updateText(servingweight, 0);
                else updateText(servingweight, value / number);
            }
        });
    }

    private int getInt(EditText editText) {
        String string = editText.getText().toString();
        if (string.length() == 0) return 0;

        return Integer.parseInt(string);
    }

    private void updateText(TextView textView, int val) {
        textView.setText("" + val);
    }


    private void setUpBackButton() {
        Button btn = (Button) findViewById(R.id.btn_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
