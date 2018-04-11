package ittepic.edu.mx.a331_webservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Spinner menu;
    FloatingActionButton exit;
    String[] items;
    private boolean isFirsTime=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu=(Spinner)findViewById(R.id.spinnerMenu);
        exit=(FloatingActionButton)findViewById(R.id.fabOff);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        items=getResources().getStringArray(R.array.menu);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.menu , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(spinner_adapter);
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirsTime){
                    isFirsTime=false;
                }else {
                    Toast.makeText(getApplicationContext(), items[position],Toast.LENGTH_SHORT).show();
                }
                switch (position){
                    case 0:
                        break;
                    case 1:
                        Intent intent=new Intent();
                        intent.setClass(view.getContext(), Consultar.class);
                        startActivity(intent);
                        break;
                    default:
                        finish();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
