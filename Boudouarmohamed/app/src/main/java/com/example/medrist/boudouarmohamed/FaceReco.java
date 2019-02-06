package com.example.medrist.boudouarmohamed;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectFacesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Face;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class FaceReco extends AsyncTask<File,Integer,DetectedFaces> {
    private ProgressDialog dialog;
    private AppCompatActivity appCompatActivity;
    private TableLayout mtable;
    /** application context. */

    public FaceReco(TableLayout textView, AppCompatActivity appCompatActivity)
    {
        this.mtable =textView;
        this.appCompatActivity=appCompatActivity;
        this.dialog = new ProgressDialog(appCompatActivity);
    }
    @Override
    protected DetectedFaces doInBackground(File... files) {
        IamOptions options = new IamOptions.Builder()
                .apiKey("len5TuXtFKngGr8bTrUjleNly03D9zJTBV_IS1Yo6aMQ")
                .build();

        VisualRecognition service = new VisualRecognition("2018-03-19", options);
        InputStream imagesStream = null;
        try {
            imagesStream = new FileInputStream(files[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DetectFacesOptions detectFacesOptions = null;
        try {
            detectFacesOptions = new DetectFacesOptions.Builder()
                    .imagesFile(files[0])
                    .build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DetectedFaces result = null;

        int flag=-1;
        while (flag<0){
           result= service.detectFaces(detectFacesOptions).execute();
            flag=1;
        }

        try {

            // Invoke a Visual Recognition method
        } catch (NotFoundException e) {

            // Handle Not Found (404) exception
        } catch (RequestTooLargeException e) {

            // Handle Request Too Large (413) exception
        } catch (ServiceResponseException e) {

            // Base class for all exceptions caused by error responses from the service
        }

        return result;
    }

    @Override
    protected void onPostExecute(DetectedFaces s) {
        //we hide the dialogue
        dialog.hide();
        //we display the results on the table
        addResultToTableView(s.getImages().get(0).getFaces());
    }

    private void addResultToTableView(List<Face> classes) {
        cleanTable();

        for (Face r : classes)
        {
            TableRow tableRow = new TableRow(appCompatActivity);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView classe = new TextView(appCompatActivity);
            classe.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView score = new TextView(appCompatActivity);
            score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            classe.setText("Max Age : "+r.getAge().getMax()+"\nMin Age : "+r.getAge().getMin());
            score.setText(( r.getGender().getGender()+"\n "));

            classe.setPadding(0,0,0,2);
            score.setPadding(0,0,0,2);
            tableRow.addView(classe);
            tableRow.addView(score);

            mtable.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));



        }
    }

    private void cleanTable() {

        int childCount = mtable.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            View v1 = mtable.getChildAt(0);
            View v2 = mtable.getChildAt(1);
            mtable.removeAllViews();
            mtable.addView(v1);
            mtable.addView(v2);
        }
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(Integer.parseInt(String.valueOf(values[0])));
    }

    @Override
    protected void onPreExecute() {
        // show the progress bar
        super.onPreExecute();
        this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


      }