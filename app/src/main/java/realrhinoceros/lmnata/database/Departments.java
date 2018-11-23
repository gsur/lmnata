package realrhinoceros.lmnata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import realrhinoceros.lmnata.mediators.Department;

public class Departments extends DataBaseMiddleware {

    protected final String table = DataBaseHelper.T_DEPARTMENTS;

    public Departments(Context context) {
        super(context);
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

    public Department getDepartment(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE _id=?", new String[]{Integer.toString(id)});

        return setDepartment(cursor);
    }

    public ArrayList<Department> getDepartments() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
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

    /*public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT " + DataBaseHelper.T_DEPARTMENTS_C_NAME + " FROM " + table, null);

        while(cursor.moveToNext()) {
            names.add(cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_DEPARTMENTS_C_NAME)));
        }

        return names;
    }

    public long insertDepartment(Department department) {
        return db.insert(table, null, getCV(department));
    }

    public int updateDepartment(Department department) {
        return db.update(table, getCV(department), "_id=?", new String[]{String.valueOf(department.id)});
    }

    public int deleteDepartment(int id) {
        return db.delete(table, "_id=?", new String[]{Integer.toString(id)});
    }*/
}
