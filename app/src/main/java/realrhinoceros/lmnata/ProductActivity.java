package realrhinoceros.lmnata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import realrhinoceros.lmnata.custom_views.HeadPanel;
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
    HeadPanel headPanel;
    Context cont;
    int product_id;

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

        cont = (Context) this;

        LinearLayout linear = (LinearLayout) findViewById(R.id.productLinear);
        headPanel = new HeadPanel(this, "", getString(R.string.product), this);

        setProduct();

        PopupMenu.OnMenuItemClickListener popupListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update_item:
                            Intent intent = new Intent(ProductActivity.this, UpdateProductActivity.class);
                            intent.putExtra("id", product_id);
                            startActivity(intent);
                        return true;

                    case R.id.delete_item:
                            AlertDialog.Builder dialog = new AlertDialog.Builder(cont);
                            dialog.setMessage(R.string.product_delete_msg);
                            dialog.setPositiveButton(R.string.app_delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Products product_db = new Products(cont);
                                    if (product_db.deleteProduct(product.id) != 0){
                                        Toast.makeText(cont, R.string.product_delete_success_msg, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(cont, R.string.product_delete_failed_msg, Toast.LENGTH_SHORT).show();
                                    }
                                    product_db.close();

                                    finish();
                                }
                            });
                            dialog.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                        return true;

                    default:

                        return false;
                }
            }
        };
        headPanel.setOptionsListener(popupListener);

        linear.addView(headPanel, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProduct();
    }

    protected void setProduct(){
        product_id = getIntent().getExtras().getInt("id");

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

            headPanel.setNameSectorText(product.name);
        }else{
            Toast.makeText(getApplicationContext(), R.string.error_products_loading, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
