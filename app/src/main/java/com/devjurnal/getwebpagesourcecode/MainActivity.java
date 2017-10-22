package com.devjurnal.getwebpagesourcecode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ConnectionTask CT;
    ConnectionCheck Check;
    Spinner spURL;
    static  TextView tvOutput;
    EditText edtURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOutput = (TextView) findViewById(R.id.tvOutput);
        edtURL = (EditText) findViewById(R.id.edtURL);
        spURL = (Spinner) findViewById(R.id.spURL);
        Check = new ConnectionCheck(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_url, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spURL.setAdapter(adapter);
    }

    public void getCode(View v) {
        String URL = spURL.getSelectedItem().toString()+edtURL.getText().toString().trim();
        boolean valid = Patterns.WEB_URL.matcher(URL).matches();


        if (Check.isConnectingToNetwork())
        {
            if (URL.isEmpty()){
                Toast.makeText(this, "Please Fill the Form Correctly", Toast.LENGTH_SHORT).show();
            }
            else if(valid){
                CT = new ConnectionTask(this);
                CT.execute(URL);
            } else
            {
                tvOutput.setText("URL NOT Valid");
            }

        } else
        {
            Toast.makeText(this, "Check Your Network Connection", Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Network Connection, Buka Setting dan Aktifkan Jaringan?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener()
                    {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                            @SuppressWarnings("unused") final int id){
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })

                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener()
                    {
                        public void onClick (final DialogInterface dialog , @SuppressWarnings("unused") final int id){
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }


    }

}
