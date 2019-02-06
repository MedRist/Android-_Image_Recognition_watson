package com.example.medrist.boudouarmohamed;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class FaceActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btnChoose, btnUpload;
    private TextView textView;
    String filePath;
    private File file;
    private TableLayout mTableLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnChoose = (Button) findViewById(R.id.button_choose);
        btnUpload = (Button) findViewById(R.id.button_upload);
        textView  = (TextView)findViewById(R.id.text_v);
        setSupportActionBar(toolbar);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });
        mTableLayout = (TableLayout) findViewById(R.id.table_layout);
        mTableLayout.setStretchAllColumns(true);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    imageUpload(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch(id) {
            case R.id.face:
                intent = new Intent(this,FaceActivity.class);
                this.startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == 1){
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);
                file = new File(filePath);
                imageView.setImageURI(picUri);
            }
        }
    }

    private void imageUpload(final String imagePath) {
        // Start the AsynckTask
        new FaceReco(mTableLayout,this).execute(file);
        Toast.makeText(getApplicationContext(), "image="+imagePath, Toast.LENGTH_LONG).show();
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 1);
    }

    // return the path of the selected image
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

}

