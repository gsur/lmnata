package realrhinoceros.lmnata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import realrhinoceros.lmnata.mediators.Product;

public class Products extends DataBaseMiddleware{
    protected final String table = DataBaseHelper.T_PRODUCTS;

    public Products(Context context) {
        super(context);
    }

    private ArrayList<Product> mainQuery(String query, String[] queryParams){
        Cursor cursor = db.rawQuery(query, queryParams);

        return setProductList(cursor);
    }

    private ContentValues getCV(Product product) {
        ContentValues productCV = new ContentValues();

        productCV.put(DataBaseHelper.T_PRODUCTS_C_NAME, product.name);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_IMAGE, product.image);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_ARTICLE, product.article);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_BARCODE, product.barecode);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_DESCRIPTION, product.description);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID, product.department_id);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_CATEGORY_ID, product.category_id);
        productCV.put(DataBaseHelper.T_PRODUCTS_C_BRAND_ID, product.brand_id);
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
        product.category_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_CATEGORY_ID));
        product.brand_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BRAND_ID));
        product.approved = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_APPROVED));

        return product;
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
            product.category_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_CATEGORY_ID));
            product.brand_id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_BRAND_ID));
            product.approved = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.T_PRODUCTS_C_APPROVED));

            products.add(product);
        }

        return products;
    }

    public long insertProduct(Product product) {
        return db.insert(table, null, getCV(product));
    }

    public int updateProduct(Product product) {
        return db.update(table, getCV(product), "_id=?", new String[]{String.valueOf(product.id)});
    }

    public int deleteProduct(int id) {
        return db.delete(table, "_id=?", new String[]{Integer.toString(id)});
    }

    public int deleteProductsByBrand(int brand_id) {
        return db.delete(table, "brand_id=?", new String[]{Integer.toString(brand_id)});
    }

    public int deleteProductsByCategory(int category_id) {
        return db.delete(table, "category_id=?", new String[]{Integer.toString(category_id)});
    }

    public Product getProduct(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE _id=?", new String[]{Integer.toString(id)});

        return setProduct(cursor);
    }

    public ArrayList<Product> getAllProducts() {
        String query = "SELECT * FROM " + table;

        return mainQuery(query, null);
    }

    public ArrayList<Product> getProducts(int category_id) {
        String query = "SELECT * FROM " + table + " WHERE " + DataBaseHelper.T_PRODUCTS_C_CATEGORY_ID + "=?";
        String[] params = new String[] {Integer.toString(category_id)};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromName(String name) {
        String query = "SELECT * FROM " + table + " WHERE " + DataBaseHelper.T_PRODUCTS_C_NAME + "=?";
        String[] params = new String[] {name};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromDepartment(int department_id) {
        String query = "SELECT * FROM " + table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=?";
        String[] params = new String[] {Integer.toString(department_id)};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromCategory(int department_id, String category_id) {
        String query = "SELECT * FROM " + table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? AND " +
                DataBaseHelper.T_PRODUCTS_C_CATEGORY_ID + "=?";
        String[] params = new String[] {Integer.toString(department_id), category_id};

        return mainQuery(query, params);
    }

    public ArrayList<Product> getProductsFromBrand(int department_id, String brand_id) {
        String query = "SELECT * FROM " + table + " WHERE " +
                DataBaseHelper.T_PRODUCTS_C_DEPARTMENT_ID + "=? AND " +
                DataBaseHelper.T_PRODUCTS_C_BRAND_ID + "=?";
        String[] params = new String[] {Integer.toString(department_id), brand_id};

        return mainQuery(query, params);
    }
}
