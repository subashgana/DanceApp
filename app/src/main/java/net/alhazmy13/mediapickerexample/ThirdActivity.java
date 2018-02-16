package net.alhazmy13.mediapickerexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subash.b on 11/13/2017.
 */

public class ThirdActivity extends Activity implements AdapterView.OnItemClickListener {

    List<GridViewItem> gridItems;
    public static String strClassname = " ";
    public static String firstimage = " ";
    public static String secondimage = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdactivity);



        if (MainActivity.mMediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            MainActivity.mMediaPlayer.stop();
            MainActivity. mMediaPlayer.release();
            MainActivity.mMediaPlayer = null;
        }

        Intent intent = getIntent();
        strClassname = intent.getStringExtra("classname");
        Toast.makeText(getApplicationContext(), strClassname, Toast.LENGTH_SHORT).show();
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/YourAlbum/";
        setGridAdapter(file_path);
    }


    /**
     * This will create our GridViewItems and set the adapter
     *
     * @param path The directory in which to search for images
     */
    private void setGridAdapter(String path) {
        // Create a new grid adapter
        gridItems = createGridItems(path);
        MyGridAdapter adapter = new MyGridAdapter(this, gridItems);

        // Set the grid adapter
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        // Set the onClickListener
        gridView.setOnItemClickListener(this);
    }


    /**
     * Go through the specified directory, and create items to display in our
     * GridView
     */
    private List<GridViewItem> createGridItems(String directoryPath) {
        List<GridViewItem> items = new ArrayList<GridViewItem>();

        // List all the items within the folder.
        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());
        for (File file : files) {

            // Add the directories containing images or sub-directories
            if (file.isDirectory()
                    && file.listFiles(new ImageFileFilter()).length > 0) {

                items.add(new GridViewItem(file.getAbsolutePath(), true, null));
            }
            // Add the images
            else {
                Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(),
                        50,
                        50);
                items.add(new GridViewItem(file.getAbsolutePath(), false, image));
            }
        }

        return items;
    }


    /**
     * Checks the file to see if it has a compatible extension.
     */
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }


    @Override
    public void
    onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (gridItems.get(position).isDirectory()) {
            setGridAdapter(gridItems.get(position).getPath());
        } else if (strClassname.equals("mainactivity")) {
            shareImage(position);
        } else {
            if (strClassname.equals("compare1")) {
                Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
                firstimage = Uri.parse(gridItems.get(position).getPath()) + "";
                startActivity(intent);

            } else if (strClassname.equals("compare2")) {
                Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
                secondimage = Uri.parse(gridItems.get(position).getPath()) + "";
                startActivity(intent);

            } else {
                // Display the image
                shareImage(position);
            }
        }

    }

    private void shareImage(int position) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        //share.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAILID_TO});
        share.putExtra(Intent.EXTRA_SUBJECT, "Diabetes level");
        share.putExtra(Intent.EXTRA_TEXT, "Image is attached");
        //share.setData(Uri.parse(Constants.EMAILID_FROM));
        share.setType("message/rfc822");
        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse(gridItems.get(position).getPath()));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }
}
