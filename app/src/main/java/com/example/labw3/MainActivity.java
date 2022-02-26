package com.example.labw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labw3.provider.PC;
import com.example.labw3.provider.PCViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity{
    private String cpuInput;
    private String motherBoardInput;
    private String gCardInput;
    private String ramInput;
    private String ssdInput;
    private String priceInput;
    private String prevInputs; // Stores the previous inputs for all the fields separated by "|"
    private PCViewModel myPCViewModel;
    private Integer numOfPCs;
    private FirebaseDatabase db;
    private DatabaseReference mRef;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    EditText cpuText, ramText, ssdText, mbText, gcText, priceText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView welcomeUserTv = findViewById(R.id.welcomeUserTitle);
        welcomeUserTv.setText("Welcome " + getIntent().getStringExtra(getString(R.string.LOGGED_USERNAME_TAG)) + "!");

        cpuText = findViewById(R.id.cpuText);
        ramText = findViewById(R.id.ramText);
        ssdText = findViewById(R.id.ssdText);
        mbText = findViewById(R.id.mbText);
        gcText = findViewById(R.id.gcText);
        priceText = findViewById(R.id.priceText);

        /*Link the drawerLayout with the action bar, including having the hamburger icon*/
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new navigationViewListener());

        myPCViewModel = new ViewModelProvider(this).get(PCViewModel.class);
        myPCViewModel.getNumOfPCs().observe(this, newData -> {
            this.numOfPCs = newData;
        });

        /*Activate the "add" FAB with the corresponding functionalities*/
        FloatingActionButton addFab = findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPCToBill();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        registerReceiver(new MyBroadcastReceiver(), new IntentFilter(SMSReceiver.PC_PARTS_SMS_ACTION));

        /*Firebase stuff*/
        this.db = FirebaseDatabase.getInstance();
        this.mRef = this.db.getReference("FIT2081/PCList");

        this.gestureDetector = new GestureDetector(this, new PCGestureDetector());
        this.scaleGestureDetector = new ScaleGestureDetector(this, new PCScaleGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @SuppressLint("SetTextI18n")
    private void setDefaultValuesToFields()
    {
        this.cpuText.setText("Intel i7");
        this.ramText.setText("32");
        this.ssdText.setText("500");
        this.mbText.setText("MPG X570");
        this.gcText.setText("MSI RADEON 5700XT");
        this.priceText.setText("3200");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.clear_fields)
        {
            this.clearFields();
            return true;
        }
        else if(id == R.id.logout)
        {
            Intent logoutIntent = new Intent(this, LoginActivity.class);

            // The current task will be cleared and this activity will be the root of the task.
            // FLAG_ACTIVITY_CLEAR_TASK: This can only be used in conjunction with FlAG_ACTIVITY_NEW_TASK.
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean addPCToBill() {
        this.cpuInput = cpuText.getText().toString();
        this.ramInput = ramText.getText().toString();
        this.ssdInput = ssdText.getText().toString();
        this.motherBoardInput = mbText.getText().toString();
        this.gCardInput = gcText.getText().toString();
        this.priceInput = priceText.getText().toString();

        if(this.cpuInput.isEmpty())
        {
            Toast.makeText(this, "Please specify your CPU", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(this.ramInput.isEmpty())
        {
            Toast.makeText(this, "Please specify your RAM", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(this.ssdInput.isEmpty())
        {
            Toast.makeText(this, "Please specify your SSD", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(this.motherBoardInput.isEmpty())
        {
            Toast.makeText(this, "Please specify your Motherboard", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(this.gCardInput.isEmpty())
        {
            Toast.makeText(this, "Please specify your Graphics Card", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(this.priceInput.isEmpty())
        {
            Toast.makeText(this, "Please specify your Price", Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            PC newPC = new PC(this.cpuInput, Integer.parseInt(this.ramInput),
                    Integer.parseInt(this.ssdInput), this.motherBoardInput, this.gCardInput,
                    Float.parseFloat(this.priceInput));

            this.myPCViewModel.insert(newPC);

            this.mRef.push().setValue(newPC);

            // clearFields is also in charge of storing the inputs into "prevInputs"
            this.clearFields();

            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "New PC Added!", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myPCViewModel.deleteLastPC();
                    parseUserInputStrSetToFields(prevInputs);
                }
            }).show();

            return true;
        }
    };

    public void clearFields() {
        this.prevInputs =
                  cpuText.getText().toString() + "|"
                + ramText.getText().toString() + "|"
                + ssdText.getText().toString() + "|"
                + mbText.getText().toString() + "|"
                + gcText.getText().toString() + "|"
                + priceText.getText().toString();

        cpuText.getText().clear();
        ramText.getText().clear();
        ssdText.getText().clear();
        mbText.getText().clear();
        gcText.getText().clear();
        priceText.getText().clear();
    }

    protected void onStart() {
        super.onStart();

        SharedPreferences fields_sp = getSharedPreferences(getString(R.string.FIELDS_SP_TAG), 0);

        //Get all the stored relevant persistent data
        cpuInput = fields_sp.getString(getString(R.string.CPU_TAG), "");
        ramInput = fields_sp.getString(getString(R.string.RAM_TAG), "");
        ssdInput = fields_sp.getString(getString(R.string.SSD_TAG), "");
        motherBoardInput = fields_sp.getString(getString(R.string.mBoard_TAG), "");
        gCardInput = fields_sp.getString(getString(R.string.gCard_TAG), "");
        priceInput = fields_sp.getString(getString(R.string.PRICE_TAG), "");

        //Put them back to the fields
        cpuText.setText(cpuInput);
        ramText.setText(ramInput);
        ssdText.setText(ssdInput);
        mbText.setText(motherBoardInput);
        gcText.setText(gCardInput);
        priceText.setText(priceInput);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void onPause() {
        super.onPause();

        /*Save the text fields data*/
        SharedPreferences fields_sp = getSharedPreferences(getString(R.string.FIELDS_SP_TAG), 0);
        SharedPreferences.Editor fields_editor = fields_sp.edit();

        cpuInput = cpuText.getText().toString();
        ramInput = ramText.getText().toString();
        ssdInput = ssdText.getText().toString();
        motherBoardInput = mbText.getText().toString();
        gCardInput = gcText.getText().toString();
        priceInput = priceText.getText().toString();

        //Store them in the sp
        fields_editor.putString(getString(R.string.mBoard_TAG), motherBoardInput);
        fields_editor.putString(getString(R.string.RAM_TAG), ramInput);
        fields_editor.putString(getString(R.string.gCard_TAG), gCardInput);
        fields_editor.putString(getString(R.string.CPU_TAG), cpuInput);
        fields_editor.putString(getString(R.string.SSD_TAG), ssdInput);
        fields_editor.putString(getString(R.string.PRICE_TAG), priceInput);

        //Asynchronously commit the data
        fields_editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        // Android does save the view data automatically via the parent class
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        // Android does restore the view data automatically via the parent class
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void parseUserInputStr(String msg) {
        StringTokenizer sT = new StringTokenizer(msg, "|");
        this.cpuInput = sT.nextToken();
        this.ramInput = sT.nextToken();
        this.ssdInput = sT.nextToken();
        this.motherBoardInput =  sT.nextToken();
        this.gCardInput = sT.nextToken();
        this.priceInput = sT.nextToken();
    }

    public void parseUserInputStrSetToFields(String msg) {
        this.parseUserInputStr(msg);

        cpuText.setText(cpuInput);
        ramText.setText(ramInput);
        ssdText.setText(ssdInput);
        mbText.setText(motherBoardInput);
        gcText.setText(gCardInput);
        priceText.setText(priceInput);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.PC_PARTS_SMS_KEY);

            parseUserInputStrSetToFields(msg);
        }
    }

    private class navigationViewListener implements NavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();

            if(id == R.id.add_pc)
            {
                addPCToBill();
            }
            else if(id == R.id.remove_last_pc)
            {
                if(numOfPCs != 0)
                {
                    myPCViewModel.deleteLastPC();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(id == R.id.remove_all_pcs)
            {
                myPCViewModel.deleteAll();
                mRef.removeValue();
            }
            else if(id == R.id.list_all_pcs)
            {
                Intent intent = new Intent(MainActivity.this, PCListActivity.class);

                startActivity(intent);
            }

            // Close the drawer after any item click
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    private class PCGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (ramText.getText().toString().isEmpty())
                Toast.makeText(MainActivity.this, "Initial RAM size is not specified", Toast.LENGTH_SHORT).show();
            else
            {
                ramText.setText("" + (Integer.parseInt(ramText.getText().toString()) + 8));
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY == 0)
            {
                if (ssdText.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Initial SSD size is not specified", Toast.LENGTH_SHORT).show();
                else
                {
                    int newSsd = Integer.parseInt(ssdText.getText().toString()) + (int)distanceX;
                    newSsd = Math.max(newSsd, 0);
                    ssdText.setText("" + newSsd);
                }
            }
            else if (distanceX == 0)
            {
                if (ramText.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Initial RAM size is not specified", Toast.LENGTH_SHORT).show();
                else
                {
                    int newRam = Integer.parseInt(ramText.getText().toString()) - (int)distanceY;
                    newRam = Math.max(newRam, 0);
                    ramText.setText("" + newRam);
                }
            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            setDefaultValuesToFields();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > 1000 || Math.abs(velocityY) > 1000)
                moveTaskToBack(true);
            else if (Math.abs(velocityX) > 300 || Math.abs(velocityY) > 300)
            {
                if(numOfPCs != 0)
                {
                    myPCViewModel.deleteLastPC();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            clearFields();
        }
    }

    private class PCScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            if (priceText.getText().toString().isEmpty())
            {
                Toast.makeText(MainActivity.this, "Initial Price is not specified", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                return true;
            }
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            String price = priceText.getText().toString();

            if (detector.getScaleFactor() < 1)
                priceText.setText("" + (Integer.parseInt(price) + 1));
            else
                priceText.setText("" + (Integer.parseInt(price) - 1));

            return true;
        }
    }
}