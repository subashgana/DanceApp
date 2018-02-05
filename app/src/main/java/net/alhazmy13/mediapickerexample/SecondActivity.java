package net.alhazmy13.mediapickerexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Util.ConvolutionMatrix;
import Util.Util;

public class SecondActivity extends Activity {

    //Button btnLoadImage;
    TextView textSource;
    ImageView imageResult;
    SeekBar hueBar, satBar, valBar;
    TextView hueText, satText, valText;
    Button btnResetHSV, buttoncartoon, next,btncompare;

    final int RQS_IMAGE1 = 1;

    Uri source;
    //Bitmap bitmapMaster;
    Canvas canvasMaster;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BMP");
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        // btnLoadImage = (Button) findViewById(R.id.loadimage);
        textSource = (TextView) findViewById(R.id.sourceuri);
        imageResult = (ImageView) findViewById(R.id.result);
        buttoncartoon = (Button) findViewById(R.id.buttoncartoon);
        next = (Button) findViewById(R.id.gallery);
        btncompare = (Button)findViewById(R.id.compare);



   /*     btnLoadImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE1);
            }
        });*/

        hueText = (TextView) findViewById(R.id.texthue);
        satText = (TextView) findViewById(R.id.textsat);
        valText = (TextView) findViewById(R.id.textval);
        hueBar = (SeekBar) findViewById(R.id.huebar);
        satBar = (SeekBar) findViewById(R.id.satbar);
        valBar = (SeekBar) findViewById(R.id.valbar);
        hueBar.setOnSeekBarChangeListener(seekBarChangeListener);
        satBar.setOnSeekBarChangeListener(seekBarChangeListener);
        valBar.setOnSeekBarChangeListener(seekBarChangeListener);
        btnResetHSV = (Button) findViewById(R.id.resethsv);

        imageResult.setImageBitmap(bitmap);
        buttoncartoon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        bitmap = engrave(bitmap);
                        imageResult.setImageBitmap(bitmap);

                    }
                });
            }
        });

        btnResetHSV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // reset SeekBars
                hueBar.setProgress(256);
                satBar.setProgress(256);
                valBar.setProgress(256);

                loadBitmapHSV();
            }
        });

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ThirdActivity.class));

            }
        });

        btncompare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompareActivity.class));

            }
        });


        imageResult.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(SecondActivity.this)) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File f = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "temporary_file.jpg");
                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // share.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAILID_TO});
                    share.putExtra(Intent.EXTRA_SUBJECT, "Diabetes level");
                    share.putExtra(Intent.EXTRA_TEXT, "Image is attached");
                    // share.setData(Uri.parse(Constants.EMAILID_FROM));
                    share.setType("message/rfc822");
                    share.putExtra(Intent.EXTRA_STREAM,
                            Uri.parse("file:///sdcard/temporary_file.jpg"));
                    startActivity(Intent.createChooser(share, "Share Image"));

                } else {
                    Toast.makeText(SecondActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (resultCode == RESULT_OK) {
             switch (requestCode) {
                 case RQS_IMAGE1:
                     source = data.getData();

                     try {
                         bitmapMaster = BitmapFactory
                                 .decodeStream(getContentResolver().openInputStream(
                                         source));

                         // reset SeekBars
                         hueBar.setProgress(256);
                         satBar.setProgress(256);
                         valBar.setProgress(256);

                         loadBitmapHSV();

                     } catch (FileNotFoundException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }

                     break;
             }
         }
     }
 */
    OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            loadBitmapHSV();
        }

    };


    public static Bitmap engrave(Bitmap src) {
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(5);
        convMatrix.setAll(0);
        convMatrix.Matrix[0][0] = -2;
        convMatrix.Matrix[1][1] = 2;
        convMatrix.Factor = 1;
        convMatrix.Offset = 95;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

    private void loadBitmapHSV() {
        if (bitmap != null) {
            runOnUiThread(new Runnable() {
                public void run() {

                    int progressHue = hueBar.getProgress() - 256;
                    int progressSat = satBar.getProgress() - 256;
                    int progressVal = valBar.getProgress() - 256;

   /*
    * Hue (0 .. 360) Saturation (0...1) Value (0...1)
    */

                    float hue = (float) progressHue * 360 / 256;
                    float sat = (float) progressSat / 256;
                    float val = (float) progressVal / 256;

                    hueText.setText("Hue: " + String.valueOf(hue));
                    satText.setText("Saturation: " + String.valueOf(sat));
                    valText.setText("Value: " + String.valueOf(val));

                    Intent intent = getIntent();
                    byte[] bytes = intent.getByteArrayExtra("BMP");
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    imageResult.setImageBitmap(updateHSV(bitmap, hue, sat, val));

                }
            });
        }
    }

    private Bitmap updateHSV(Bitmap src, float settingHue, float settingSat,
                             float settingVal) {

        int w = src.getWidth();
        int h = src.getHeight();
        int[] mapSrcColor = new int[w * h];
        int[] mapDestColor = new int[w * h];

        float[] pixelHSV = new float[3];

        src.getPixels(mapSrcColor, 0, w, 0, 0, w, h);

        int index = 0;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {

                // Convert from Color to HSV
                Color.colorToHSV(mapSrcColor[index], pixelHSV);

                // Adjust HSV
                pixelHSV[0] = pixelHSV[0] + settingHue;
                if (pixelHSV[0] < 0.0f) {
                    pixelHSV[0] = 0.0f;
                } else if (pixelHSV[0] > 360.0f) {
                    pixelHSV[0] = 360.0f;
                }

                pixelHSV[1] = pixelHSV[1] + settingSat;
                if (pixelHSV[1] < 0.0f) {
                    pixelHSV[1] = 0.0f;
                } else if (pixelHSV[1] > 1.0f) {
                    pixelHSV[1] = 1.0f;
                }

                pixelHSV[2] = pixelHSV[2] + settingVal;
                if (pixelHSV[2] < 0.0f) {
                    pixelHSV[2] = 0.0f;
                } else if (pixelHSV[2] > 1.0f) {
                    pixelHSV[2] = 1.0f;
                }

                // Convert back from HSV to Color
                mapDestColor[index] = Color.HSVToColor(pixelHSV);

                index++;
            }
        }

        return Bitmap.createBitmap(mapDestColor, w, h, Bitmap.Config.ARGB_8888);

    }

}
