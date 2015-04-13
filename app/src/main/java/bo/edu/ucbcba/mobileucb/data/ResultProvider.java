package bo.edu.ucbcba.mobileucb.data;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class ResultProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = buildUriMatcher();

    static final int RESULTADOS = 100;
    static final int NOTICIAS = 101;
    static final int NOTICIAS_WITH_NOTICIAS_ID = 102;
    static final int EQUIPO = 103;
    /**
     * Construcción del UriMatcher, Este UriMatcher hara posible
     * que por cada URI que se pase, se devuelva un valor constante
     * que nos permita identificar luego en el sistema
     * */
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ResultContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ResultContract.PATH_RESULTADOS, RESULTADOS);
        matcher.addURI(authority, ResultContract.PATH_NOTICIAS, NOTICIAS);

        matcher.addURI(authority, ResultContract.PATH_NOTICIAS + "/#", NOTICIAS_WITH_NOTICIAS_ID);

        matcher.addURI(authority, ResultContract.PATH_EQUIPO, EQUIPO);
      //  matcher.addURI(authority, ResultContract.PATH_NOTICIAS + "/#/#", NOTICIAS_WITH_NOTICIAS_ID);

        return matcher;
    }

    // Metodos utilitarios
    private ResultDbHelper dbHelper;

    // result.team_id = ?
    private static final String notifSelection = ResultContract.ResultEntry.TABLE_NAME_NOTIF; // + "." + ResultContract.ResultEntry.COLUMN_TEAM_ID + "= ? ";

    // result.team_id = ? AND match_date >= ?
    /*private static final String teamWithStartDateSelection =
            ResultContract.ResultEntry.TABLE_NAME + "." + ResultContract.ResultEntry.COLUMN_TEAM_ID + "= ? AND " +
                    ResultContract.ResultEntry.TABLE_NAME + "." + ResultContract.ResultEntry.COLUMN_MATCH_DATE + " <= ? ";
    // result.team_id = ? AND match_date = ?
    */
    private static final String noticiaWithNoticiaIDSelection =
            ResultContract.ResultEntry.TABLE_NAME_NOTICIAS + "." + ResultContract.ResultEntry.COLUMN_NOTICIA_ID + "= ? " ;

    private Cursor getResultByTeam(Uri uri, String []projection, String sortOrder) {
        int teamSetting = ResultContract.ResultEntry.getTeamFromUri(uri);
        long startDate = ResultContract.ResultEntry.getStartDateFromUri(uri);

        String [] selectionArgs;
        String selection;

       // if (startDate == 0) {
            selection = teamSelection;
            selectionArgs = new String [] {Integer.toString(teamSetting)};
        /*} else {
            selection = teamWithStartDateSelection;
            selectionArgs = new String [] {Integer.toString(teamSetting), Long.toString(startDate)};
        }*/

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

    }

    private Cursor getNoticiasByTeam(Uri uri, String []projection, String sortOrder) {
        int teamSetting = ResultContract.ResultEntry.getTeamFromUri(uri);
        long startDate = ResultContract.ResultEntry.getStartDateFromUri(uri);

        String [] selectionArgs;
        String selection;

        // if (startDate == 0) {
        selection = noticiaSelection;
        selectionArgs = new String [] {Integer.toString(teamSetting)};
        /*} else {
            selection = teamWithStartDateSelection;
            selectionArgs = new String [] {Integer.toString(teamSetting), Long.toString(startDate)};
        }*/

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(ResultContract.ResultEntry.TABLE_NAME_NOTICIAS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

    }

    private Cursor getNoticiasByNoticiaID(Uri uri, String []projection, String sortOrder) {
        int noticiaSet = ResultContract.ResultEntry.getNoticiaIDFromUri(uri);


        String [] selectionArgs;
        String selection;

        selection = noticiaWithNoticiaIDSelection;
        selectionArgs = new String [] {Integer.toString(noticiaSet)};

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(ResultContract.ResultEntry.TABLE_NAME_NOTICIAS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

    }


//    private void normalizeDate(ContentValues values) {
//        // normalize the date value
//        if (values.containsKey(ResultContract.ResultEntry.FECHA)) {
//            long dateValue = values.getAsLong(ResultContract.ResultEntry.FECHA);
//            values.put(ResultContract.ResultEntry.COLUMN_FECHA, ResultContract.normalizeDate(dateValue));
//        }
//    }

    /**
     *  Metodos a sobreescribir!!
     * */
    @Override
    public boolean onCreate() {
        dbHelper = new ResultDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case RESULTADOS:
                return ResultContract.ResultEntry.CONTENT_RESULTADOS;
            case NOTICIAS:
                return ResultContract.ResultEntry.CONTENT_NOTICIAS;
            case NOTICIAS_WITH_NOTICIAS_ID:
                return ResultContract.ResultEntry.CONTENT_ITEM_NOTICIAS;
            case EQUIPO:
                return ResultContract.ResultEntry.CONTENT_EQUIPO;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        Log.d("El match es :", uri.toString());
        switch (uriMatcher.match(uri)) {
            case RESULTADOS:
                retCursor = dbHelper.getReadableDatabase().query(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case NOTICIAS:
                retCursor = dbHelper.getReadableDatabase().query(ResultContract.ResultEntry.TABLE_NAME_NOTICIAS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
           case NOTICIAS_WITH_NOTICIAS_ID:
               retCursor = getNoticiasByNoticiaID(uri, projection, sortOrder);
               break;
            case EQUIPO:
                retCursor = dbHelper.getReadableDatabase().query(ResultContract.ResultEntry.TABLE_NAME_EQUIPO,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        if (match == RESULTADOS) {
            normalizeDate(values);
            long id = db.insert(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS, null, values);

            if (id > 0)
                returnUri = ResultContract.ResultEntry.buildResultUri(id);
            else
                throw new SQLException("Failed to insert row into " + uri);
        } else {

            if (match == NOTICIAS) {
                normalizeDate(values);
                long id = db.insert(ResultContract.ResultEntry.TABLE_NAME_NOTICIAS, null, values);

                if (id > 0)
                    returnUri = ResultContract.ResultEntry.buildNoticiasUri(id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
            } else {

                if (match == EQUIPO) {
                    normalizeDate(values);
                    long id = db.insert(ResultContract.ResultEntry.TABLE_NAME_EQUIPO, null, values);

                    if (id > 0)
                        returnUri = ResultContract.ResultEntry.buildEquipoUri(id);
                    else
                        throw new SQLException("Failed to insert row into " + uri);

                }
                else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int deleted;

        if (selection == null)
            selection = "1";

        if (match == RESULTADOS) {
            deleted = db.delete(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS, selection, selectionArgs);
        } else {
            if (match == NOTICIAS) {
                deleted = db.delete(ResultContract.ResultEntry.TABLE_NAME_NOTICIAS, selection, selectionArgs);
            } else {
                if (match == EQUIPO) {
                    deleted = db.delete(ResultContract.ResultEntry.TABLE_NAME_EQUIPO, selection, selectionArgs);
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }

            }

        }

        if (deleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int updated;

        if (selection == null)
            selection = "1";

        if (match == RESULTADOS) {
            updated = db.update(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS, values, selection, selectionArgs);
        } else {
            if (match == NOTICIAS) {
                updated = db.update(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS, values, selection, selectionArgs);
            } else {
                if (match == EQUIPO) {
                    updated = db.update(ResultContract.ResultEntry.TABLE_NAME_EQUIPO, values, selection, selectionArgs);
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }

        }

        if (updated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return updated;
    }

    // Optional Methods!

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        if (match == RESULTADOS) {
            db.beginTransaction();
            int count = 0;

            try {
                for (ContentValues value : values) {
                    normalizeDate(value);
                    long id = db.insert(ResultContract.ResultEntry.TABLE_NAME_RESULTADOS, null, value);

                    if (id != -1)
                        ++count;
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        } else {
            if (match == NOTICIAS) {
                db.beginTransaction();
                int count = 0;

                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long id = db.insert(ResultContract.ResultEntry.TABLE_NAME_NOTICIAS, null, value);

                        if (id != -1)
                            ++count;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return count;
            } else {
                if (match == EQUIPO) {
                    db.beginTransaction();
                    int count = 0;

                    try {
                        for (ContentValues value : values) {
                            normalizeDate(value);
                            long id = db.insert(ResultContract.ResultEntry.TABLE_NAME_EQUIPO, null, value);

                            if (id != -1)
                                ++count;
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);

                    return count;
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }

        }
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
