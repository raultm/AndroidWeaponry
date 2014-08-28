package mobi.kinetica.foodmeter.core.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqlHelper {

    public static int insertFromRawResource(Context context, SQLiteOpenHelper sqliteHelper, int resourceId) throws IOException {
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        return SqlHelper.insertFromInputStream(sqliteHelper, insertsStream);
    }

    public static int insertFromAssetsPath(Context context, String databaseName, String assetsPath) throws IOException {
        return SqlHelper.insertFromAssetsPath(context, new SqlHelper().new DefaultDatabaseHelper(context, databaseName), assetsPath);
    }

    public static int insertFromAssetsPath(Context context, SQLiteOpenHelper sqliteHelper, String assetsPath) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream insertsStream = assetManager.open(assetsPath);
        return SqlHelper.insertFromInputStream(sqliteHelper, insertsStream);
    }

    public static int insertFromFilePath(Context context, SQLiteOpenHelper sqliteHelper, String filePath) throws IOException {
        File dbFile = new File(filePath);
        final int readLimit = 16 * 1024;
        InputStream insertsStream = new BufferedInputStream(new FileInputStream(dbFile), readLimit);
        return SqlHelper.insertFromInputStream(sqliteHelper, insertsStream);
    }



    public static int insertFromInputStream(SQLiteOpenHelper sqliteHelper, InputStream insertsStream) throws IOException {
        int numOfLinesExecuted = 0;
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
        while (insertReader.ready()) {
            String sqlStatement = insertReader.readLine();
            try {
                db.execSQL(sqlStatement);
                numOfLinesExecuted++;
            }catch (Exception e){
                Log.i("SQLHelper", "Error on line " + sqlStatement);
            }
        }
        insertReader.close();
        db.close();
        return numOfLinesExecuted;
    }

    public class DefaultDatabaseHelper extends SQLiteOpenHelper {

        public DefaultDatabaseHelper(Context context, String databaseName) {
            super(context, databaseName, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}