package realrhinoceros.lmnata.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DataBaseMiddleware {
    protected SQLiteDatabase db;

    DataBaseMiddleware(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        try {
            this.db = dataBaseHelper.getWritableDatabase();
        }
        catch (SQLiteException e){
            this.db = dataBaseHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }
}
