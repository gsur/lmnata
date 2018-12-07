package realrhinoceros.lmnata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.custom_views.HeadPanel;
import realrhinoceros.lmnata.database.Brands;
import realrhinoceros.lmnata.database.Categories;
import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.mediators.Brand;
import realrhinoceros.lmnata.mediators.Category;
import realrhinoceros.lmnata.mediators.CategoryAdapter;

public class CategoriesListActivity extends AppCompatActivity {

    protected int brand_id;
    ArrayList<Category> categories;
    Context cont;
    HeadPanel headPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);
        cont = (Context) this;

        brand_id = getIntent().getExtras().getInt("id");

        setCategories();

        Brands brands = new Brands(this);
        Brand brandName = brands.getBrand(brand_id);
        brands.close();
        String description = getString(R.string.brand);
        headPanel = new HeadPanel(this, brandName.name, description, this);

        PopupMenu.OnMenuItemClickListener popupListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update_item:
                            AlertDialog.Builder updateBuilder = new AlertDialog.Builder(cont);
                            updateBuilder.setTitle(R.string.rename_brand);

                            final EditText renameBrandNameText = new EditText(cont);
                            updateBuilder.setView(renameBrandNameText);
                            renameBrandNameText.setText(headPanel.getNameSectorText());

                            updateBuilder.setPositiveButton(R.string.app_apply, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!String.valueOf(renameBrandNameText.getText()).isEmpty()){
                                        Brands brandsDB = new Brands(cont);
                                        Brand brand = brandsDB.getBrand(brand_id);
                                            brand.name = String.valueOf(renameBrandNameText.getText());
                                        brandsDB.updateBrand(brand);
                                        brandsDB.close();

                                        headPanel.setNameSectorText(brand.name);
                                    }
                                    else{
                                        dialog.cancel();
                                    }
                                }
                            });

                            updateBuilder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            updateBuilder.show();
                        return true;

                    case R.id.delete_item:
                            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(cont);
                            deleteBuilder.setMessage(R.string.brand_delete_msg);
                            deleteBuilder.setPositiveButton(R.string.app_delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Brands brand_db = new Brands(cont);
                                    if (brand_db.deleteBrand(brand_id) != 0){
                                        Toast.makeText(cont, R.string.brand_delete_success_msg, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(cont, R.string.brand_delete_failed_msg, Toast.LENGTH_SHORT).show();
                                    }
                                    brand_db.close();

                                    Categories category_db = new Categories(cont);
                                    category_db.deleteCategoriesByBrand(brand_id);
                                    category_db.close();

                                    Products product_db = new Products(cont);
                                    product_db.deleteProductsByBrand(brand_id);
                                    product_db.close();

                                    finish();
                                }
                            });
                            deleteBuilder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            deleteBuilder.show();
                        return true;

                    default:

                        return false;
                }
            }
        };
        headPanel.setOptionsListener(popupListener);

        LinearLayout linear = (LinearLayout) findViewById(R.id.categoriesLinear);
        linear.addView(headPanel, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCategories();
    }

    protected void setCategories(){
        Categories db = new Categories(getApplicationContext());
        categories = db.getCategories(brand_id);
        db.close();

        if (categories.size() != 0) {
            ListView listView = (ListView) findViewById(R.id.categories_list);

            final CategoryAdapter listData = new CategoryAdapter(this,
                    android.R.layout.simple_list_item_1,
                    categories);
            listView.setAdapter(listData);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Category item = (Category) parent.getItemAtPosition(position);
                    Intent intent = new Intent(CategoriesListActivity.this, ProductsListActivity.class);
                    intent.putExtra("id", item.id);
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(this, R.string.error_categories_loading, Toast.LENGTH_SHORT).show();
        }
    }
}
