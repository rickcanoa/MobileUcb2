package bo.edu.ucbcba.mobileucb;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by Richard on 01/4/15.
 */
public class NotifService extends IntentService {
    public static final String TEAM_QUERY_EXTRA = "tqe";
    private static final String LOG_TAG = NotifService.class.getSimpleName();

    public NotifService() {
        super("mobileucb");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String team = intent.getStringExtra(TEAM_QUERY_EXTRA);
        String json = Utility.getJsonStringFromNetwork(team, "90");

        Log.v(LOG_TAG, "Servicio iniciado");

        try {
            Utility.parseJsonNotif(json, Integer.parseInt(team), this);
        } catch (JSONException | ParseException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }


        Log.v(LOG_TAG, "Service finalizado");
    }
}
