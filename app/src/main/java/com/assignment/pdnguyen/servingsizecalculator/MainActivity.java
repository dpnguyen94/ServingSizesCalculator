package com.assignment.pdnguyen.servingsizecalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final int REQUEST_CODE_ADD_POT = 1234;
    private static final int REQUEST_CODE_EDIT_POT = 4567;

    private PotCollection pots = new PotCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPotListfromSharedPrefs();
        setUpAddPotActivity();
        setUpCalculateServingActivity();
    }

    private void getPotListfromSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences("Pot List", MODE_PRIVATE);
        int count = prefs.getInt("PotListCount", 0);
        for (int i = 0; i < count; i ++) {
            String pot_name = prefs.getString("PotName" + i,"no string found");
            int pot_weight = prefs.getInt("PotWeight" + i, 0);
            pots.addPot(new Pot(pot_name, pot_weight));
        }

        //Initiate ArrayAdapter -> ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.items,pots.getPotDescriptions());
        ListView list = (ListView) findViewById(R.id.lstvw_potlist);
        list.setAdapter(adapter);
    }

    private void setUpAddPotActivity() {
        Button btn = (Button) findViewById(R.id.btn_addpot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPotActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_POT);
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

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditPotActivity.class);

                Pot pot = pots.getPot(position);
                intent.putExtra("pot_name", pot.getName());
                intent.putExtra("pot_weight", pot.getWeightInG());
                intent.putExtra("pot_position", position);

                startActivityForResult(intent, REQUEST_CODE_EDIT_POT);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_ADD_POT:
                if (resultCode == Activity.RESULT_OK) {
                    Pot pot = AddPotActivity.getPotFromIntent(data);
                    pots.addPot(pot);
                    populateListView();
                }
                break;

            case REQUEST_CODE_EDIT_POT:
                if (resultCode == Activity.RESULT_OK) {
                    int position = data.getIntExtra("pot_position", -1);
                    Pot pot = EditPotActivity.getPotFromIntent(data);
                    pots.changePot(pot, position);
                    populateListView();
                }
                if (resultCode == Activity.RESULT_FIRST_USER) {
                    int position = data.getIntExtra("pot_position", -1);
                    pots.removePot(position);
                    populateListView();
                }
                break;
        }
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.items,pots.getPotDescriptions());
        ListView list = (ListView) findViewById(R.id.lstvw_potlist);
        list.setAdapter(adapter);

        storePotListtoSharedPrefs(pots.getPotName(), pots.getPotWeight());
    }

    private void storePotListtoSharedPrefs(String[] potname, int[] potweight) {
        SharedPreferences prefs = getSharedPreferences("Pot List", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("PotListCount", potname.length);
        for (int i = 0; i < potname.length; i ++) {
            editor.putString("PotName" + i, potname[i]);
            editor.putInt("PotWeight" + i, potweight[i]);
        }

        editor.commit();
    }
}
