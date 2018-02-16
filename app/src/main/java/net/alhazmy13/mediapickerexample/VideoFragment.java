package net.alhazmy13.mediapickerexample;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import net.alhazmy13.mediapicker.Video.VideoPicker;
import net.alhazmy13.mediapicker.rxjava.video.VideoPickerHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import rx.Subscriber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by alhazmy13 on 3/13/17.
 */

public class VideoFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private TextureView videoView;
    private TextView path;
    MediaController myMediaController;
    MediaMetadataRetriever mediaMetadataRetriever;
    private static final String TAG = "MainActivity";
    private List<String> mPath;

    LinearLayout video_layout;

    Button btn_capture;

    ImageView iv;
    private MediaPlayer mMediaPlayer;
    //private TextureView mPreview;
    Surface surface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_layout, container, false);

        init(view);
        videoView.setSurfaceTextureListener(this);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             /*   Bitmap bitmap = Bitmap.createBitmap(videoView.getWidth(), videoView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                video_layout.draw(canvas);
                bitmap = getResizedBitmap(bitmap, 500);
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
                //share.setData(Uri.parse(Constants.EMAILID_FROM));
                share.setType("message/rfc822");
                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));*/

                iv.setImageBitmap(getBitmap());
            }
        });

        return view;
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

    private void init(View view) {
        // Find our View instances
        video_layout = (LinearLayout) view.findViewById(R.id.video_layout);
        btn_capture = (Button) view.findViewById(R.id.capture);
        iv = (ImageView) view.findViewById(R.id.image_view);
        videoView = (TextureView) view.findViewById(R.id.iv_video);
        MediaController mediaController = new MediaController(getActivity());

        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaController.setAnchorView(videoView);
      /*  videoView.setMediaController(mediaController);
        videoView.start();*/
        path = (TextView) view.findViewById(R.id.tv_path);
        view.findViewById(R.id.bt_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideo();
            }
        });
    }


    public Bitmap getBitmap(){
        return  videoView.getBitmap();
    }

    private void pickVideo() {
        new VideoPicker.Builder(getActivity())
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
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = (List<String>) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);
            Log.d(TAG, "onActivityResult: ");

            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(getActivity(), Uri.parse(mPath.get(0)));
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
                Log.d(TAG, e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
            //loadVideo();
        }
    }

  /*  private void loadVideo() {
        Log.d(TAG, "loadVideo: " + (mPath == null));
        if (mPath != null && mPath.size() > 0) {
            Log.d(TAG, "loadVideo: ");
            //Log.d(TAG, "loadImage: " + mPath.size());
            //path.setText(mPath.get(0));
            videoView.setVideoURI(Uri.parse(mPath.get(0)));
            videoView.start();
        }
    }*/

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
    public void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
