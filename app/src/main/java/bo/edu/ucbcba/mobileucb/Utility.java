package bo.edu.ucbcba.mobileucb;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;

import static bo.edu.ucbcba.mobileucb.data.ResultContract.ResultEntry;

public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static String getJsonStringFromNetwork(String tipoNotificacion, String pUrl) {
        Log.d(LOG_TAG, "Starting network connection");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            final String FIXTURE_BASE_URL = pUrl;
            //"http://www.ceabolivia.org/mobile2/generarJSON.php";

            Uri builtUri = Uri.parse(FIXTURE_BASE_URL).buildUpon().build();
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null)
                return "";
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0)
                return "";

            return buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    public static String[] parseFixtureJson(String fixtureJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(fixtureJson);
        ArrayList<String> result = new ArrayList<>();

        final String LIST = "fixtures";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String RESULT = "result";
        final String HOME_SCORE = "goalsHomeTeam";
        final String AWAY_SCORE = "goalsAwayTeam";

        JSONArray fixturesArray = jsonObject.getJSONArray(LIST);

        for (int i = 0; i < fixturesArray.length(); i++) {
            String homeTeam;
            String awayTeam;
            int homeScore;
            int awayScore;
            JSONObject matchObject = fixturesArray.getJSONObject(i);
            JSONObject resultObject = matchObject.getJSONObject(RESULT);

            homeTeam = matchObject.getString(HOME_TEAM);
            awayTeam = matchObject.getString(AWAY_TEAM);
            homeScore = resultObject.getInt(HOME_SCORE);
            awayScore = resultObject.getInt(AWAY_SCORE);

            String resultString = new Formatter().format("%s: %d - %s: %d", homeTeam, homeScore, awayTeam, awayScore).toString();
            result.add(resultString);
        }
        return result.toArray(new String[result.size()]);
    }

    public static String[] parseJSONPersona(String PersonaJson) throws JSONException{
        JSONObject jsonObject = new JSONObject(PersonaJson);
        ArrayList<String> result = new ArrayList<>();

        JSONArray personasArray = jsonObject.getJSONArray("personas");

        for (int i = 0; i < personasArray.length(); i++) {
            String nombre;
            JSONObject matchObject = personasArray.getJSONObject(i);
            nombre = matchObject.getString("nombre");
            result.add(nombre);
        }
        return result.toArray(new String[result.size()]);
    }

    public static ArrayList<cls_notificaciones> parseJsonNotificacion(String NotificacionJson) {
        ArrayList<cls_notificaciones> items = new ArrayList<cls_notificaciones>();

        items.add(new cls_notificaciones(1, 1, "Patatas", "Tuberculo", "drawable/next"));
        //items.add(new cls_notificaciones(2, "Naranja", "Fruta", "drawable/naranjas"));
        //items.add(new cls_notificaciones(3, "Lechuga", "Verdura", "drawable/lechuga"));

        return items;
    }

    public static void parseJsonNotif(String fixtureJson, int teamID, Context context) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(fixtureJson);
        ArrayList<ContentValues> notif = new ArrayList<>();

        final String RESULT = "resultados";
        final String Tipo = "Tipo";
        final String Aviso = "Aviso";
        final String Detalle = "Detalle";

        JSONArray fixturesArray = jsonObject.getJSONArray(RESULT);



        for (int i = 0; i < fixturesArray.length(); i++) {
            String _Tipo;
            String _Aviso;
            String _Detalle;

            JSONObject matchObject = fixturesArray.getJSONObject(i);
            //  JSONObject resultObject = matchObject.getJSONObject(Estado);

            _Tipo = matchObject.getString(Tipo);
            _Aviso = matchObject.getString(Aviso);
            _Detalle =  matchObject.getString(Detalle);

            ContentValues content = new ContentValues();

            content.put(ResultEntry.COLUMN_DESC_NOTIF, _Tipo);
            content.put(ResultEntry.COLUMN_AVISO, _Aviso);
            content.put(ResultEntry.COLUMN_DETALLE, _Detalle);

            notif.add(content);
        }

        int inserted = 0;

        if (notif.size() > 0) {
            ContentValues[] valuesArray = new ContentValues[notif.size()];

            notif.toArray(valuesArray);
            inserted = context.getContentResolver().bulkInsert(ResultEntry.CONTENT_URI_DESC_NOTIF, valuesArray);
        }



        Log.d(LOG_TAG, inserted + " Filas insertadas");

    }
    public static int getDefaultNotif(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return Integer.parseInt(prefs.getString(context.getString(R.string.eval_continua_key), context.getString(R.string.pref_barcelona_key)));
    }

}
