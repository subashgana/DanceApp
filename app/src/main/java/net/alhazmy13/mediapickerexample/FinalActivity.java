package net.alhazmy13.mediapickerexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        TextView tv = (TextView)findViewById(R.id.textView2);
        Intent intent = getIntent();
        tv.setText(intent.getStringExtra("message"));


             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                     FinalActivity.this);

                // set title
                alertDialogBuilder.setTitle("Result");

                // set dialog message
                alertDialogBuilder
                        .setMessage(intent.getStringExtra("message"))
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                               dialog.dismiss();
                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

    }

}
