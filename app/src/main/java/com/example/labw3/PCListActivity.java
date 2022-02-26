package com.example.labw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Type;

import com.example.labw3.provider.PC;
import com.example.labw3.provider.PCViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.List;

public class PCListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PCListRecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton addPcFab;
    private PCViewModel myPCViewModel;
    private Spinner pcFilterDropDownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pclist);

        Toolbar toolbar = findViewById(R.id.pc_list_toolbar);
        toolbar.setTitle("Your PC List");
        setSupportActionBar(toolbar);

        this.pcFilterDropDownMenu = findViewById(R.id.pcFilterSpinner);
        String[] items = new String[]{"All PCs"};
        ArrayAdapter<String> pcFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        this.pcFilterDropDownMenu.setAdapter(pcFilterAdapter);
        this.pcFilterDropDownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        this.recyclerView = findViewById(R.id.pc_list_recyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.recyclerViewAdapter = new PCListRecyclerViewAdapter();
        this.recyclerView.setAdapter(recyclerViewAdapter);

        myPCViewModel = new ViewModelProvider(this).get(PCViewModel.class);
        myPCViewModel.getAllPCs().observe(this, newData -> {
            this.setAdapterDataByFilter(newData);
        });

        this.addPcFab = findViewById(R.id.pc_list_fab);
        this.addPcFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });
    }

    private void setAdapterDataByFilter(List<PC> newData)
    {
        this.recyclerViewAdapter.setPCs(newData);
        this.recyclerViewAdapter.notifyDataSetChanged();
    }

}