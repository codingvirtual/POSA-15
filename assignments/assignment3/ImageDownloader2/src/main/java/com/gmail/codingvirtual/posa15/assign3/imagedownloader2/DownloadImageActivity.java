package com.gmail.codingvirtual.posa15.assign3.imagedownloader2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends LifecycleLoggingActivity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ DONE -- you fill in here.
        super.onCreate(savedInstanceState);

        // Get the URL associated with the Intent data.
        // @@ DONE -- you fill in here.
        final Uri url = getIntent().getData();

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ DONE -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.

        // create a handler
        // post a runnable to the handler that does the downloading
        // runnable posts a result to the handler
        // handler takes the result via handleMessage and creates the Intent and returns it
        new DownloadTask().execute(url);

    }


    private class DownloadTask extends AsyncTask<Uri, Void, Uri> {

        @Override
        protected Uri doInBackground(Uri... url) {
            return Utils.downloadImage(getApplicationContext(), url[0]);
        }

        protected void onPostExecute(Uri returnUrl) {
            Intent resultIntent = new Intent();
            resultIntent.setData(returnUrl);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
