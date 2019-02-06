package com.example.medrist.boudouarmohamed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ImageReco extends AsyncTask<File,Integer,ClassifiedImages> {
    private ProgressDialog dialog;
    private AppCompatActivity appCompatActivity;
    private TableLayout mtable;
    /** application context. */

    public ImageReco(TableLayout textView, AppCompatActivity appCompatActivity)
    {
        this.mtable =textView;
        this.appCompatActivity=appCompatActivity;
        this.dialog = new ProgressDialog(appCompatActivity);
    }
    @Override
    protected ClassifiedImages doInBackground(File... files) {
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
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(imagesStream)
                .imagesFilename(files[0].getName())
                .classifierIds(Arrays.asList("default"))
                .build();
        publishProgress(10);
        ClassifiedImages result =null;
        int flag=-1;
        while (flag<0){
            result = service.classify(classifyOptions).execute();
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
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(ClassifiedImages s) {
        //Hide the Dialog
        dialog.hide();
        addResultToTableView(s.getImages().get(0).getClassifiers().get(0).getClasses());
    }

    private void addResultToTableView(List<ClassResult> classes) {
        cleanTable();
        for (ClassResult r : classes)
        {
            TableRow tableRow = new TableRow(appCompatActivity);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView classe = new TextView(appCompatActivity);
            classe.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView score = new TextView(appCompatActivity);
            score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            classe.setText(r.getClassName());
            score.setText(r.getScore()+"");

            tableRow.addView(classe);
            tableRow.addView(score);
            mtable.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        }
    }
    private void cleanTable() {
        // recyclying the view
        int childCount = mtable.getChildCount();
        System.out.print(mtable.getChildCount()+"\tcount ");
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