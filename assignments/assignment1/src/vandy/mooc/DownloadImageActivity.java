package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;

import java.lang.ref.WeakReference;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
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

        final Handler downloadHandler = new DownloadHandler(this);

/*
                new Handler () {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                 runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultIntent = new Intent();
                        Uri returnUrl = Uri.parse(msg.getData().getString("result"));
                        resultIntent.setData(returnUrl);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
            }
        };
        */


        downloadHandler.post(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Uri returnUri = DownloadUtils.downloadImage(getApplicationContext(), url);
                        threadMsg(returnUri.toString());
                    }

                    private void threadMsg(String url) {
                        Message msg = downloadHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("result", url);
                        msg.setData(bundle);
                        downloadHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }


    private static class DownloadHandler extends Handler {

        final private WeakReference<DownloadImageActivity> mOuterClass;

        public DownloadHandler(DownloadImageActivity outerActivity) {
            mOuterClass = new WeakReference<DownloadImageActivity>(outerActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final Message theMsg = msg;
            mOuterClass.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent resultIntent = new Intent();
                    Uri returnUrl = Uri.parse(theMsg.getData().getString("result"));
                    if (returnUrl != null) {
                        resultIntent.setData(returnUrl);
                        mOuterClass.get().setResult(RESULT_OK, resultIntent);
                    }
                    else {
                        mOuterClass.get().setResult(RESULT_CANCELED, resultIntent);
                    }
                    mOuterClass.get().finish();
                }
            });
        }
    }
}
