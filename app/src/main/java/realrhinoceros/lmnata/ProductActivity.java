package realrhinoceros.lmnata;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import realrhinoceros.lmnata.database.Brands;
import realrhinoceros.lmnata.database.Categories;
import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.helpers.EAN13CodeBuilder;
import realrhinoceros.lmnata.helpers.UriToBitmap;
import realrhinoceros.lmnata.mediators.Brand;
import realrhinoceros.lmnata.mediators.Category;
import realrhinoceros.lmnata.mediators.Department;
import realrhinoceros.lmnata.mediators.Product;

public class ProductActivity extends AppCompatActivity {
    TextView nameField;
    TextView articleField;
    TextView descriptionField;
    TextView barecodeField;
    TextView categoryField;
    TextView brandField;
    TextView departmentName;
    ImageView imageView;

    Product product;
    Department department;
    Brand brand;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        nameField = (TextView) findViewById(R.id.product_name_field);
        articleField = (TextView) findViewById(R.id.product_article_field);
        descriptionField = (TextView) findViewById(R.id.product_description_field);
        departmentName = (TextView) findViewById(R.id.department_field);
        imageView = (ImageView) findViewById(R.id.product_photo_image);
        barecodeField = (TextView) findViewById(R.id.product_barecode_field);
            barecodeField.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/ea13.ttf"));
        categoryField = (TextView) findViewById(R.id.product_category_field);
        brandField = (TextView) findViewById(R.id.product_brand_field);

        setProduct();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProduct();
    }

    protected void setProduct(){
        int product_id = getIntent().getExtras().getInt("id");

        Products dbProd = new Products(getApplicationContext());
        product = dbProd.getProduct(product_id);
        dbProd.close();

        if(product.id != -1) {
            Departments dbDept = new Departments(getApplicationContext());
            department = dbDept.getDepartment(product.department_id);
            dbDept.close();

            Brands dbBrand = new Brands(getApplicationContext());
            brand = dbBrand.getBrand(product.brand_id);
            dbBrand.close();

            Categories dbCat = new Categories(getApplicationContext());
            category = dbCat.getCategory(product.category_id);
            dbCat.close();

            nameField.setText(product.name);
            articleField.setText(product.article);
            descriptionField.setText(product.description);
            if (Integer.valueOf(department.id) != -1) {
                departmentName.setText(department.name);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_departments_loading, Toast.LENGTH_LONG).show();
            }
            if (Integer.valueOf(brand.id) != -1) {
                brandField.setText(brand.name);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_brands_loading, Toast.LENGTH_LONG).show();
            }
            if (Integer.valueOf(category.id) != -1) {
                categoryField.setText(category.name);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_categories_loading, Toast.LENGTH_LONG).show();
            }
            UriToBitmap.parse(this, Uri.parse(product.image), imageView);
            if (product.barecode.length() == 12) {
                barecodeField.setText(new EAN13CodeBuilder(product.barecode).getCode());
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_barcode_parce, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.error_products_loading, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
