package carnival.bubble.shooter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.facebook.internal.AttributionIdentifiers;


public class CarnivalActivity extends ActionBarActivity {
    private static String CARNIVAL_APP_ID = "451095005036628";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this, CARNIVAL_APP_ID);
        Log.i("WOOHOO", "onCreate: " + this.getPackageName().toString());
        Intent intent = getIntent();
        if (null == intent) {
            Log.i("WOOHOO", "failed intent");
            return;
        }
        Uri intentData = intent.getData();
        String action = "";
        if (null != intent.getAction()) {
            action += "--" + intent.getAction();
        }
        if (null != intent.getScheme()) {
            action += "--" + intent.getScheme();
        }
        if (null != intent.getExtras()) {
            action += "--" + intent.getExtras().toString();
        }
        if (null == intentData) {
            Log.i("WOOHOO", "failed data: " + action);
            Log.i("WOOHOO", "Extras:");
            Bundle bundle = intent.getExtras();
            if (null == bundle) {
                return;
            }
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                String valueStr = "NULL";
                String valueClass = "NULL";
                if (null != value) {
                    valueStr = value.toString();
                    valueClass = value.getClass().getName();
                }
                Log.i("WOOHOO", String.format("%s %s (%s)", key,
                        valueStr, valueClass));
            }
            return;
        }
        Log.i("WOOHOO", "DEEP LINK!!!: " + intentData.toString());
    }

    protected void onResume() {
        super.onResume();
        final CarnivalActivity appCtx = this;
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        Uri intentData = intent.getData();
        if (null == intentData) {
            return;
        }
        Log.i("WOOHOO", "DEEP LINK!!!: " + intentData.toString());

        Log.i("WOOHOO", "Running!!!!");
        Log.i("WOOHOO", "anon_id: " + AppEventsLogger.getAnonymousAppDeviceGUID(this.getApplicationContext()));
        Log.i("WOOHOO", "onResume: " + this.getPackageName().toString());
        AppLinkData.fetchDeferredAppLinkData(this.getApplicationContext(), CARNIVAL_APP_ID,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        if ((null != appLinkData) && (null != appLinkData.getTargetUri())) {
                            Log.i("WOOHOO", "Got Deep Link!!!: " + appLinkData.getTargetUri().toString());
                            Log.i("WOOHOO", "Other stuff: " + appLinkData.getArgumentBundle().toString());
                        } else {
                            Log.i("WOOHOO", "Got nothing :(");
                            AttributionIdentifiers ids = AttributionIdentifiers.getAttributionIdentifiers(appCtx.getApplicationContext());
                            if (null != ids.getAndroidInstallerPackage()) {
                                Log.i("WOOHOO", ids.getAndroidInstallerPackage());
                            }
                            if (null != ids.getAndroidAdvertiserId()) {
                                Log.i("WOOHOO", ids.getAndroidAdvertiserId());
                            }
                            if (null != ids.getAttributionId()) {
                                Log.i("WOOHOO", ids.getAttributionId());
                            }
                        }
                    }
                });
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
