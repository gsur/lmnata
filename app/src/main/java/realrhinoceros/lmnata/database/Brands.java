package realrhinoceros.lmnata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import realrhinoceros.lmnata.mediators.Brand;

public class Brands extends DataBaseMiddleware {
    protected final String table = DataBaseHelper.T_BRANDS;

    public Brands(Context context) {
        super(context);
    }

    private ContentValues getCV(Brand brand) {
        ContentValues brandCV = new ContentValues();

        brandCV.put(DataBaseHelper.T_BRANDS_C_DEPARTMENT_ID, brand.department_id);
        brandCV.put(DataBaseHelper.T_BRANDS_C_NAME, brand.name);

        return brandCV;
    }

    private Brand setBrand(Cursor cursor) {
        Brand brand = new Brand();

        cursor.moveToFirst();
        brand.id = cursor.getInt(cursor.getColumnIndex("_id"));
        brand.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_BRANDS_C_DEPARTMENT_ID));
        brand.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_BRANDS_C_NAME));

        return brand;
    }

    public long insertBrand(Brand brand) {
        return db.insert(table, null, getCV(brand));
    }

    public int updateBrand(Brand brand) {
        return db.update(table, getCV(brand), "_id=?", new String[]{String.valueOf(brand.id)});
    }

    public int deleteBrand(int id) {
        return db.delete(table, "_id=?", new String[]{Integer.toString(id)});
    }

    public Brand getBrand(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE _id=?", new String[]{Integer.toString(id)});

        return setBrand(cursor);
    }

    public ArrayList<Brand> getBrands(int department_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + DataBaseHelper.T_BRANDS_C_DEPARTMENT_ID + "=?",
                                            new String[]{Integer.toString(department_id)});
        ArrayList<Brand> brands = new ArrayList<Brand>();

        while(cursor.moveToNext()) {
            Brand brand = new Brand();

            brand.id = cursor.getInt(cursor.getColumnIndex("_id"));
            brand.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_BRANDS_C_DEPARTMENT_ID));
            brand.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_BRANDS_C_NAME));

            brands.add(brand);
        }

        return brands;
    }

    public ArrayList<Brand> getAllBrands() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
        ArrayList<Brand> brands = new ArrayList<Brand>();

        while(cursor.moveToNext()) {
            Brand brand = new Brand();

            brand.id = cursor.getInt(cursor.getColumnIndex("_id"));
            brand.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_BRANDS_C_DEPARTMENT_ID));
            brand.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_BRANDS_C_NAME));

            brands.add(brand);
        }

        return brands;
    }
}
