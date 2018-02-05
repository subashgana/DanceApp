package net.alhazmy13.mediapickerexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MediaController.MediaPlayerControl {
    private Fragment videoFragment;
    private Fragment imageFragment;
    private TextureView videoView;
    private TextView path;
    MediaController myMediaController;
    MediaMetadataRetriever mediaMetadataRetriever;
    private static final String TAG = "MainActivity";
    private List<String> mPath;
    private boolean mReturningWithResult = false;
    LinearLayout video_layout, linear2;
    ProgressDialog progressDialog;
    ImageButton guest,next;

    Bitmap finalbitmap;

    ImageView iv;
    private MediaPlayer mMediaPlayer;
    //private TextureView mPreview;
    Surface surface;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        videoView.setSurfaceTextureListener(this);

        video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("ProgressDialog"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (editText.getText().toString().length() > 0) {
                                        linear2.setVisibility(View.GONE);
                                        iv.setVisibility(View.VISIBLE);
                                        //btn_capture.setVisibility(View.VISIBLE);
                                        finalbitmap = getBitmap();
                                        iv.setImageBitmap(finalbitmap);


                                        storeImage(finalbitmap, System.currentTimeMillis() + ".png", editText.getText().toString());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please give a folder name", Toast.LENGTH_LONG).show();

                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  iv.setImageBitmap(getBitmap());

             nextMethod();

            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  iv.setImageBitmap(getBitmap());
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("ProgressDialog"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (editText.getText().toString().length() > 0) {

                                        Bitmap bitmap = finalbitmap;
                                        bitmap = getResizedBitmap(bitmap, 500);
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        byte[] bytes = stream.toByteArray();
                                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                        intent.putExtra("BMP", bytes);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please give a folder name", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
            }
        });


        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/YourAlbum/" + "Test";
                File dir = new File(file_path);
                if (!dir.exists())
                    dir.mkdirs();//create a file to write bitmap data
                startActivity(new Intent(getApplicationContext(), ThirdActivity.class));

            }
        });

      /*  btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  iv.setImageBitmap(getBitmap());
                Bitmap bitmap = finalbitmap;
                bitmap = getResizedBitmap(bitmap,500);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("BMP", bytes);
                startActivity(intent);
            }
        });*/
    }

    private void nextMethod() {


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (editText.getText().toString().length() > 0) {

                                Bitmap bitmap = finalbitmap;
                                bitmap = getResizedBitmap(bitmap, 500);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                intent.putExtra("BMP", bytes);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please give a folder name", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    private boolean storeImage(Bitmap imageData, String filename, String string) {
        //get path to external storage (SD card)
       /* String iconsStoragePath = Environment.getExternalStorageDirectory() + "/bottlefolder/";
        File sdIconStorageDir = new File(iconsStoragePath+"/sdcard/saved_images/"+string);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();*/
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/YourAlbum/" + string;
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();//create a file to write bitmap data

        File f = new File(dir, filename);
        try {
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageData.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void init() {
        // Find our View instances
        video_layout = (LinearLayout) findViewById(R.id.video_layout);
        //btn_capture = (Button) findViewById(R.id.capture);
        iv = (ImageView) findViewById(R.id.image_view);
        videoView = (TextureView) findViewById(R.id.iv_video);
        //linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);
        editText = (EditText) findViewById(R.id.editText);
        MediaController mediaController = new MediaController(this);
        guest = (ImageButton)findViewById(R.id.guest);
        next = (ImageButton)findViewById(R.id.next);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaController.setAnchorView(videoView);
      /*  videoView.setMediaController(mediaController);
        videoView.start();*/
        path = (TextView) findViewById(R.id.tv_path);
        //pickVideo();
    }


    public Bitmap getBitmap()
    {
        return videoView.getBitmap();
    }

    private void pickVideo() {
        new VideoPicker.Builder(this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mReturningWithResult = true;
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = (List<String>) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);
            Log.d(TAG, "onActivityResult: ");
            //linear1.setVisibility(View.GONE);
            //videoView.setVisibility(View.VISIBLE);
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(this, Uri.parse(mPath.get(0)));
                mMediaPlayer.setSurface(surface);
                mMediaPlayer.setLooping(true);

                // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
                // creating MediaPlayer
                mMediaPlayer.prepareAsync();
                // Play video when the media source is ready for playback.
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });

            } catch (IllegalArgumentException e) {
                Log.d(TAG, e.getMessage());
            } catch (SecurityException e) {
                Log.d(TAG, e.getMessage());
            } catch (IllegalStateException e) {
                ;
                Log.d(TAG, e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
            //loadVideo();
        }
    }


    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        pickVideo();

    }

    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mReturningWithResult) {
            // Commit your transactions here.
        }
        // Reset the boolean flag back to false for next time.
        mReturningWithResult = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        if (mMediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}