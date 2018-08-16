package realrhinoceros.lmnata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import realrhinoceros.lmnata.mediators.Department;

public class Departments {
    protected SQLiteDatabase db;
    protected final String table = DataBaseHelper.T_DEPARTMENTS;

    public Departments(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        try {
            this.db = dataBaseHelper.getWritableDatabase();
        }
        catch (SQLiteException e){
            this.db = dataBaseHelper.getReadableDatabase();
        }
    }

    private ContentValues getCV(Department department) {
        ContentValues departmentCV = new ContentValues();

        departmentCV.put(DataBaseHelper.T_DEPARTMENTS_C_NAME, department.name);
        departmentCV.put(DataBaseHelper.T_DEPARTMENTS_C_IMAGE, department.image);

        return departmentCV;
    }

    private Department setDepartment(Cursor cursor) {
        Department department = new Department();

        cursor.moveToFirst();
        department.id = cursor.getInt(cursor.getColumnIndex("_id"));
        department.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_DEPARTMENTS_C_NAME));
        department.image = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_DEPARTMENTS_C_IMAGE));

        return department;
    }

    public long insertDepartment(Department department) {
        return this.db.insert(this.table, null, this.getCV(department));
    }

    public int updateDepartment(Department department) {
        return this.db.update(this.table, this.getCV(department), "_id=?", new String[]{String.valueOf(department.id)});
    }

    public int deleteDepartment(int id) {
        return this.db.delete(this.table, "_id=?", new String[]{Integer.toString(id)});
    }

    public Department getDepartment(int id) {
        Cursor cursor = this.db.rawQuery("SELECT * FROM " + this.table + " WHERE _id=?", new String[]{Integer.toString(id)});

        return setDepartment(cursor);
    }

    public ArrayList<Department> getDepartments() {
        Cursor cursor = this.db.rawQuery("SELECT * FROM " + this.table, null);
        ArrayList<Department> departments = new ArrayList<Department>();

        while(cursor.moveToNext()) {
            Department department = new Department();

            department.id = cursor.getInt(cursor.getColumnIndex("_id"));
            department.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_DEPARTMENTS_C_NAME));
            department.image = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_DEPARTMENTS_C_IMAGE));

            departments.add(department);
        }

        return departments;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        Cursor cursor = this.db.rawQuery("SELECT " + DataBaseHelper.T_DEPARTMENTS_C_NAME + " FROM " + this.table, null);

        while(cursor.moveToNext()) {
            names.add(cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_DEPARTMENTS_C_NAME)));
        }

        return names;
    }


    public void dbClose() {
        this.db.close();
    }
}
