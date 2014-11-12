package com.pathofthefood.flyingburger.Address;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.pathofthefood.flyingburger.R;

import java.util.ArrayList;


public class AddressBook extends Activity {

    private ListView AddressList;
    private ArrayList<Address> addressess;
    private AddressAdapter adapter;
    Button address_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressbook);

        AddressList = (ListView) findViewById(R.id.ListViewAddress);
        address_add = (Button) findViewById(R.id.butonAddDirections);

        address_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewAddress.class));
            }
        });
        Button fab = (Button) findViewById(R.id.fabbutton);




            addressess = (ArrayList<Address>) getIntent().getSerializableExtra("addressbook");

            if (addressess != null) {
                adapter = new AddressAdapter(getApplicationContext(), addressess);
                Log.e("ARRAYLIST", String.valueOf(addressess.get(0)));
                AddressList.setAdapter(adapter);
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressBook.this);
            alertDialog.setTitle("Alertas");
            alertDialog.setMessage("No Direcciones registradas");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
}