package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {

    CheckBox enableBT, visibleBT;
    ImageView search_bt;
    TextView nameBT;
    ListView listView;


    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        enableBT = findViewById(R.id.enableBT);
        visibleBT = findViewById(R.id.visibleBT);
        search_bt = findViewById(R.id.search_bt);
        nameBT = findViewById(R.id.nameBT);
        listView = findViewById(R.id.listView);

        nameBT.setText(getLocalBluetoothName());

        BA = BluetoothAdapter.getDefaultAdapter();

        if(BA == null)
        {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(BA.isEnabled())
        {
            enableBT.setChecked(true);
        }

        enableBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    BA.disable();
                    Toast.makeText(Bluetooth.this, "Turned off", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intentOn, 0);
                    Toast.makeText(Bluetooth.this, "Turned on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visibleBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                   Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                   startActivityForResult(getVisible,0);
                    Toast.makeText(Bluetooth.this, "Visible for 2min", Toast.LENGTH_SHORT).show();
                }
            }
        });

        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list();
            }
        });

    }

    public void list(){
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices){
            list.add(bt.getName());
        }

        Toast.makeText(this, "Showing Devices", Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
    }

    public String getLocalBluetoothName(){

        if(BA == null){
            BA = BluetoothAdapter.getDefaultAdapter();
        }
        String name = BA.getName();

        if(name == null){
            name = BA.getAddress();
        }

        return name;
    }
}