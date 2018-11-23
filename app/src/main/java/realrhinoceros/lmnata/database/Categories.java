package realrhinoceros.lmnata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import realrhinoceros.lmnata.mediators.Category;

public class Categories extends DataBaseMiddleware {
    protected final String table = DataBaseHelper.T_CATEGORIES;
    
    public Categories(Context context) {
        super(context);
    }

    private ContentValues getCV(Category category) {
        ContentValues categoryCV = new ContentValues();

        categoryCV.put(DataBaseHelper.T_CATEGORIES_C_DEPARTMENT_ID, category.department_id);
        categoryCV.put(DataBaseHelper.T_CATEGORIES_C_BRAND_ID, category.brand_id);
        categoryCV.put(DataBaseHelper.T_CATEGORIES_C_NAME, category.name);

        return categoryCV;
    }

    private Category setCategory(Cursor cursor) {
        Category category = new Category();

        cursor.moveToFirst();
        category.id = cursor.getInt(cursor.getColumnIndex("_id"));
        category.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_DEPARTMENT_ID));
        category.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_BRAND_ID));
        category.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_NAME));

        return category;
    }

    public long insertCategory(Category category) {
        return db.insert(table, null, getCV(category));
    }

    public int updateCategory(Category category) {
        return db.update(table, getCV(category), "_id=?", new String[]{String.valueOf(category.id)});
    }

    public int deleteCategory(int id) {
        return db.delete(table, "_id=?", new String[]{Integer.toString(id)});
    }

    public Category getCategory(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE _id=?", new String[]{Integer.toString(id)});

        return setCategory(cursor);
    }

    public ArrayList<Category> getCategories(int brand_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + DataBaseHelper.T_CATEGORIES_C_BRAND_ID + "=?",
                                    new String[]{Integer.toString(brand_id)});

        ArrayList<Category> categories = new ArrayList<Category>();

        while(cursor.moveToNext()) {
            Category category = new Category();

            category.id = cursor.getInt(cursor.getColumnIndex("_id"));
            category.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_DEPARTMENT_ID));
            category.brand_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_BRAND_ID));
            category.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_NAME));

            categories.add(category);
        }

        return categories;
    }

    public ArrayList<Category> getAllCategories() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
        ArrayList<Category> categories = new ArrayList<Category>();

        while(cursor.moveToNext()) {
            Category category = new Category();

            category.id = cursor.getInt(cursor.getColumnIndex("_id"));
            category.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_DEPARTMENT_ID));
            category.brand_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_BRAND_ID));
            category.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_CATEGORIES_C_NAME));

            categories.add(category);
        }

        return categories;
    }
}
