package bo.edu.ucbcba.mobileucb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultsFragment extends Fragment implements NavDibujoFragment.NavigationDrawerCallbacks {

    private static final String LOG_TAG = ResultsFragment.class.getSimpleName();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<cls_notificaciones> arrayList;
    private String _resultJson = "";
    private NavDibujoFragment navDibujoFragment;

    private CharSequence mTitle;
    private final String RESULT_FRAGMENT_TAG = "RF_TAG";
    private int teamId = -1;

    public ResultsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateResults();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.result_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateResults();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateResults() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String teamId = sharedPreferences.getString(getString(R.string.pref_team_key), getString(R.string.pref_barcelona_key));
        String days = sharedPreferences.getString(getString(R.string.pref_days_key), getString(R.string.pref_days_default));

        GetNotificaciones notf = new GetNotificaciones();
        //notf.execute(teamId, days);

        //GetResultTask task = new GetResultTask();
        //task.execute(teamId, days);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Intent intent = new Intent(getActivity(), NotifService.class);

        intent.putExtra(NotifService.TEAM_QUERY_EXTRA, Integer.toString(Utility.getDefaultNotif(getActivity())));
        Log.v(LOG_TAG, "Iniciando Servicio");
        getActivity().startService(intent);

        navDibujoFragment = (NavDibujoFragment)
                getSupportFragmentManager()rootView.findFragmentById(R.id.drawer_layout);
        mTitle = getTitle();

        // Set up the drawer.
        navDibujoFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) rootView.findViewById(R.id.navigation_drawer));











        //NotificacionesAdapter adapter = new NotificacionesAdapter(getActivity(), arrayList);

//        arrayAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.result_view,
//                R.id.result_text_view,
//                new ArrayList<String>());


//        ListView lv = (ListView)rootView.findViewById(R.id.listView);
//        ArrayList<cls_notificaciones> itemsCompra = obtenerItems();
//        NotificacionesAdapter adapter = new NotificacionesAdapter(getActivity(), itemsCompra);
//        lv.setAdapter(adapter);

//        ListView listView = (ListView)rootView.findViewById(R.id.listView);
//
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//
//                intent.putExtra(Intent.EXTRA_TEXT, arrayAdapter.getItem(position));
//                startActivity(intent);
//            }
//        });

        return rootView;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, DetailsActivity.PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.eval_continua_entry);
                if (android.os.Build.VERSION.SDK_INT > 9)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                //getSupportFragmentManager().beginTransaction().replace(R.id.container,new NoticiasFragment()).commit();

                break;
            case 2:

                mTitle = getString(R.string.grupo_est_entry);

                break;
            case 3:
                mTitle = getString(R.string.avisos_ucb_entry);

                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private ArrayList<cls_notificaciones> obtenerItems() {
        ArrayList<cls_notificaciones> items = new ArrayList<cls_notificaciones>();

        items.add(new cls_notificaciones(1, 1, "Patatas", "Tuberculo", "drawable/next"));

        return items;
    }

    class GetResultTask extends AsyncTask<String, Void, String []> {

        @Override
        protected String[] doInBackground(String... params) {
            if (params.length != 2)
                return new String[] {"SIN DATOS"};

            String resultString = Utility.getJsonStringFromNetwork(params[0], "http://www.ceabolivia.org/mobile2/generarJSON.php");

            try {
                return Utility.parseJSONPersona(resultString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing" + e.getMessage(), e);
                e.printStackTrace();
                return new String[] {"SIN DATOS"};
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            arrayAdapter.clear();
            for (String result : strings) {
                arrayAdapter.add(result);
            }
        }
    }

    class GetNotificaciones extends AsyncTask<String, Void, ArrayList<cls_notificaciones>> {

        @Override
        protected ArrayList<cls_notificaciones> doInBackground(String... params) {

            if (params.length != 2)
                arrayList.add(new cls_notificaciones(0, 0, "Error", "Configure sus Notificaciones.", "drawable/next"));
            try {
                String resultString = Utility.getJsonStringFromNetwork(params[0], "http://www.ceabolivia.org/mobile2/generarJSON.php");

                return Utility.parseJsonNotificacion(resultString);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error parsing" + e.getMessage(), e);
                e.printStackTrace();
                arrayList.add(new cls_notificaciones(0, 0, "Error", "SIN DATOS", "drawable/next"));
            }
            return  null;
        }

        @Override
        protected void onPostExecute(ArrayList<cls_notificaciones> result) {
//            arrayList.clear();
//            for (cls_notificaciones result : strings) {
//                arrayList.add(result);
//            }
            Log.d("ASYNCTASK", "Tarea Ejecutada Correctamente");
            arrayList.clear();
            arrayList.add(new cls_notificaciones(1, 1, "dato1", "dato2", "drawable/next"));
            //items.add(new cls_notificaciones(2, "Naranja", "Fruta", "drawable/naranjas"));
            //items.add(new cls_notificaciones(3, "Lechuga", "Verdura", "drawable/lechuga"));
        }
    }
}
