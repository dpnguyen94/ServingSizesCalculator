package com.assignment.pdnguyen.servingsizecalculator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1234;

    private PotCollection pots = new PotCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpAddPotActivity();
        setUpCalculateServingActivity();
    }

    private void setUpAddPotActivity() {
        Button btn = (Button) findViewById(R.id.btn_addpot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPotActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void setUpCalculateServingActivity() {
        ListView list = (ListView) findViewById(R.id.lstvw_potlist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CalculateServingActivity.class);

                Pot pot = pots.getPot(position);
                intent.putExtra("pot_name", pot.getName());
                intent.putExtra("pot_weight", pot.getWeightInG());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Pot pot = AddPotActivity.getPotFromIntent(data);
                    pots.addPot(pot);
                    populateListView();
                }
        }
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.items,pots.getPotDescriptions());
        ListView list = (ListView) findViewById(R.id.lstvw_potlist);
        list.setAdapter(adapter);
    }
}
