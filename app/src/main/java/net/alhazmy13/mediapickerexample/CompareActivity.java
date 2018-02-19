package net.alhazmy13.mediapickerexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by subash.b on 03-Jan-18.
 */

public class CompareActivity extends Activity {

    Button compareagain;

    ImageView iv1, iv2;

    String Message = "";
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparelayout);


        compareagain = (Button) findViewById(R.id.compareagain);
        compareagain.setVisibility(View.GONE);
        iv1 = (ImageView) findViewById(R.id.testimage1);
        iv2 = (ImageView) findViewById(R.id.testimage2);


        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/YourAlbum/" + "Test";
                File dir = new File(file_path);
                if (!dir.exists())
                    dir.mkdirs();//create a file to write bitmap data
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                intent.putExtra("classname", "compare1");
                startActivity(intent);

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/YourAlbum/" + "Test";
                File dir = new File(file_path);
                if (!dir.exists())
                    dir.mkdirs();//create a file to write bitmap data
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                intent.putExtra("classname", "compare2");
                startActivity(intent);
            }
        });


        if (ThirdActivity.firstimage.length() > 2) {


            File imgFile = new File(ThirdActivity.firstimage);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv1.setImageBitmap(myBitmap);

            }
        }

        if (ThirdActivity.secondimage.length() > 2) {


            File imgFile = new File(ThirdActivity.secondimage);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv2.setImageBitmap(myBitmap);

            }
        }

        if (ThirdActivity.firstimage.length() > 2 && ThirdActivity.secondimage.length() > 2) {

            compareagain.setVisibility(View.VISIBLE);
        }

        compareagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("path",ThirdActivity.firstimage);

                if (ThirdActivity.firstimage.equals(ThirdActivity.secondimage)) {

                    Message = "Pictures are matched";


                }
                else if(ThirdActivity.firstimage.equals("error.jpg"))
                {
                    Message = "Pictures are not  matched";
                }
                else{

                    Message = "Pictures are not  matched";
                }

                progressDoalog = new ProgressDialog(CompareActivity.this);
                progressDoalog.setMessage(" loading....");
                progressDoalog.setTitle("Pixels are calculating");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity

                        progressDoalog.dismiss();
                        Intent i = new Intent(CompareActivity.this, FinalActivity.class);
                        i.putExtra("message",Message);
                        startActivity(i);

                        // close this activity
                    }
                }, 6000);

               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        CompareActivity.this);

                // set title
                alertDialogBuilder.setTitle("Result");

                // set dialog message
                alertDialogBuilder
                        .setMessage(Message)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                               dialog.dismiss();
                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();*/
            }
        });

    }
}
