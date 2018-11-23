package realrhinoceros.lmnata.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import realrhinoceros.lmnata.R;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "NataDB";

    public static final String T_DEPARTMENTS = "departments";
    //public static final String T_DEPARTMENTS_C_ID = "p_id";
    public static final String T_DEPARTMENTS_C_NAME = "name";
    public static final String T_DEPARTMENTS_C_IMAGE = "image";

    public static final String T_BRANDS = "brands";
    //public static final String T_BRAND_C_ID = "p_id";
    public static final String T_BRANDS_C_DEPARTMENT_ID = "department_id";
    public static final String T_BRANDS_C_NAME = "name";

    public static final String T_CATEGORIES = "categories";
    //public static final String T_CATEGORY_C_ID = "p_id";
    public static final String T_CATEGORIES_C_DEPARTMENT_ID = "department_id";
    public static final String T_CATEGORIES_C_BRAND_ID = "brand_id";
    public static final String T_CATEGORIES_C_NAME = "name";

    public static final String T_PRODUCTS = "products";
    //public static final String T_PRODUCTS_C_ID = "p_id";
    public static final String T_PRODUCTS_C_NAME = "name";
    public static final String T_PRODUCTS_C_IMAGE = "image";
    public static final String T_PRODUCTS_C_ARTICLE = "article";
    public static final String T_PRODUCTS_C_BARCODE = "barecode";
    public static final String T_PRODUCTS_C_DESCRIPTION = "description";
    public static final String T_PRODUCTS_C_DEPARTMENT_ID = "department_id";
    //public static final String T_PRODUCTS_C_BRAND = "brand";
    public static final String T_PRODUCTS_C_BRAND_ID = "brand_id";
    //public static final String T_PRODUCTS_C_CATEGORY = "category";
    public static final String T_PRODUCTS_C_CATEGORY_ID = "category_id";
    public static final String T_PRODUCTS_C_APPROVED = "approved";

    private Context context;

    public DataBaseHelper(Context context){
        super(context, DB_NAME,null,12);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + T_DEPARTMENTS +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    T_DEPARTMENTS_C_NAME + " TEXT, " +
                    T_DEPARTMENTS_C_IMAGE + " TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + T_BRANDS +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                T_BRANDS_C_DEPARTMENT_ID + " INTEGER, " +
                T_BRANDS_C_NAME + " TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + T_CATEGORIES +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                T_CATEGORIES_C_DEPARTMENT_ID + " INTEGER, " +
                T_CATEGORIES_C_BRAND_ID + " INTEGER, " +
                T_CATEGORIES_C_NAME + " TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                T_PRODUCTS +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                T_PRODUCTS_C_NAME + " TEXT, " +
                T_PRODUCTS_C_IMAGE + " TEXT, " +
                T_PRODUCTS_C_ARTICLE + " TEXT, " +
                T_PRODUCTS_C_BARCODE + " TEXT, " +
                T_PRODUCTS_C_DESCRIPTION + " TEXT, " +
                T_PRODUCTS_C_DEPARTMENT_ID + " INTEGER, " +
                T_PRODUCTS_C_BRAND_ID + " INTEGER, " +
                T_PRODUCTS_C_CATEGORY_ID + " INTEGER, " +
                T_PRODUCTS_C_APPROVED + " INTEGER)"
        );

        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                                    T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.building) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_building_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.joiner) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_joiner_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.electrician) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_electrician_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.instrument) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_instrument_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.floor) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_floor_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.tile) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_tile_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.sanitary) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_sanitary_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.watersupp) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_watersupp_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.garden) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_garden_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.hardware) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_hardware_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.paint) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_paint_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                                    T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.design) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_design_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.light) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_light_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.storage) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_storage_dept) + "')");
        db.execSQL("INSERT INTO " + T_DEPARTMENTS + " (" + T_DEPARTMENTS_C_NAME + ", " +
                                    T_DEPARTMENTS_C_IMAGE + ") VALUES ('" +
                this.context.getResources().getString(R.string.kitchen) + "', '" +
                this.context.getResources().getResourceEntryName(R.drawable.ic_kitchen_dept) + "')");

        for (int i = 0; i < 15; i++){
            db.execSQL("INSERT INTO " + T_BRANDS + " (" + T_BRANDS_C_DEPARTMENT_ID + ", " +
                    T_BRANDS_C_NAME + ") VALUES ('" + i + "', '" +
                    this.context.getResources().getString(R.string.no_brand) + "')");
            db.execSQL("INSERT INTO " + T_CATEGORIES + " (" + T_CATEGORIES_C_DEPARTMENT_ID + ", " +
                    T_CATEGORIES_C_BRAND_ID + ", " + T_CATEGORIES_C_NAME + ") " +
                    "VALUES ('" + i + "', '" + i + "', '" +
                    this.context.getResources().getString(R.string.no_category) + "')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_DEPARTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + T_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + T_BRANDS);
        db.execSQL("DROP TABLE IF EXISTS " + T_CATEGORIES);

        onCreate(db);
    }
}
