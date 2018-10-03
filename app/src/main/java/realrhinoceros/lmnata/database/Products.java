package realrhinoceros.lmnata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import realrhinoceros.lmnata.mediators.Product;

public class Products {
    protected SQLiteDatabase db;
    protected final String table = DataBaseHelper.T_PRODUCTS;

    public Products(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        try {
            this.db = dataBaseHelper.getWritableDatabase();
        }
        catch (SQLiteException e){
            this.db = dataBaseHelper.getReadableDatabase();
        }
    }

    private ContentValues getCV(Product product) {
        ContentValues productCV = new ContentValues();

        productCV.put(DataBaseHelper.T_PRODUCTS_C_NAME, product.name);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_IMAGE, product.image);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_ARTICLE, product.article);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_BARCODE, product.barecode);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_DESCRIPTION, product.description);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID, product.department_id);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_CATEGORY, product.category);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_BRAND, product.brand);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_APPROVED, product.approved);

        return productCV;
    }

    private Product setProduct(Cursor cursor) {
        Product product = new Product();

        cursor.moveToFirst();
        product.id = cursor.getInt(cursor.getColumnIndex("_id"));
        product.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_NAME));
        product.image = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_IMAGE));
        product.article = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_ARTICLE));
        product.barecode = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BARCODE));
        product.description = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_DESCRIPTION));
        product.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID));
        product.category = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_CATEGORY));
        product.brand = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BRAND));
        product.approved = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_APPROVED));

        return product;
    }

    public long insertProduct(Product product) {
        return this.db.insert(this.table, null, this.getCV(product));
    }

    public int updateProduct(Product product) {
        return this.db.update(this.table, this.getCV(product), "_id=?", new String[]{String.valueOf(product.id)});
    }

    public int deleteProduct(int id) {
        return this.db.delete(this.table, "_id=?", new String[]{Integer.toString(id)});
    }

    public Product getProduct(int id) {
        Cursor cursor = this.db.rawQuery("SELECT * FROM " + this.table + " WHERE _id=?", new String[]{Integer.toString(id)});

        return setProduct(cursor);
    }

    private ArrayList<Product> setProductList(Cursor cursor){
        ArrayList<Product> products = new ArrayList<Product>();

        while(cursor.moveToNext()) {
            Product product = new Product();

            product.id = cursor.getInt(cursor.getColumnIndex("_id"));
            product.name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_NAME));
            product.image = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_IMAGE));
            product.article = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_ARTICLE));
            product.barecode = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BARCODE));
            product.description = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_DESCRIPTION));
            product.department_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID));
            product.category = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_CATEGORY));
            product.brand = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BRAND));
            product.approved = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_APPROVED));

            products.add(product);
        }

        return products;
    }

    private ArrayList<Product> mainQuery(String query, String[] queryParams){
        Cursor cursor = db.rawQuery(query, queryParams);

        return setProductList(cursor);
    }

    public ArrayList<Product> getProducts() {
        String query = "SELECT * FROM " + this.table;
        return mainQuery(query, null);
    }

    public ArrayList<Product> getProductsFromName(String name) {
        String query = "SELECT * FROM " + this.table + " WHERE " + DataBaseHelper.T_PRODUCTS_C_NAME + "=?";
        String[] params = new String[] {name};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromDepartment(int department_id) {
        String query = "SELECT * FROM " + this.table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=?";
        String[] params = new String[] {Integer.toString(department_id)};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromCategory(int department_id, String category) {
        String query = "SELECT * FROM " + this.table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? AND " +
                DataBaseHelper.T_PRODUCTS_C_CATEGORY + "=?";
        String[] params = new String[] {Integer.toString(department_id), category};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromBrand(int department_id, String brand) {
        String query = "SELECT * FROM " + this.table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? AND " +
                DataBaseHelper.T_PRODUCTS_C_BRAND + "=?";
        String[] params = new String[] {Integer.toString(department_id), brand};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromUncategorized(int department_id) {
        String query = "SELECT * FROM " + this.table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? AND " +
                DataBaseHelper.T_PRODUCTS_C_CATEGORY + "=? AND " +
                DataBaseHelper.T_PRODUCTS_C_BRAND + "=?";
        String[] params = new String[] {Integer.toString(department_id), "", ""};

        return mainQuery(query, params);
    }

    public ArrayList<String> getCategories(int department_id) {
        Cursor cursor = this.db.rawQuery("SELECT " + DataBaseHelper.T_PRODUCTS_C_CATEGORY +
                                            " FROM " + this.table + " WHERE " +
                                            DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? " +
                                            "GROUP BY " + DataBaseHelper.T_PRODUCTS_C_CATEGORY + " ORDER BY " +
                                            DataBaseHelper.T_PRODUCTS_C_CATEGORY + " ASC",
                new String[] {Integer.toString(department_id)});
        ArrayList<String> categories = new ArrayList<String>();

        while (cursor.moveToNext()) {
            categories.add(cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_CATEGORY)));
        }

        return categories;
    }

    public ArrayList<String> getBrands(int department_id) {
        Cursor cursor = this.db.rawQuery("SELECT " + DataBaseHelper.T_PRODUCTS_C_BRAND +
                        " FROM " + this.table + " WHERE " +
                        DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? " +
                        "GROUP BY " + DataBaseHelper.T_PRODUCTS_C_BRAND + " ORDER BY " +
                        DataBaseHelper.T_PRODUCTS_C_BRAND + " ASC",
                new String[] {Integer.toString(department_id)});
        ArrayList<String> brands = new ArrayList<String>();

        while (cursor.moveToNext()) {
            brands.add(cursor.getString(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BRAND)));
        }

        return brands;
    }

    public void dbClose() {
        this.db.close();
    }
}
