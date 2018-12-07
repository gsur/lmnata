package realrhinoceros.lmnata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.custom_views.HeadPanel;
import realrhinoceros.lmnata.database.Categories;
import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.helpers.ProductSearch;
import realrhinoceros.lmnata.mediators.Category;
import realrhinoceros.lmnata.mediators.Product;
import realrhinoceros.lmnata.mediators.ProductListAdapter;

public class ProductsListActivity extends AppCompatActivity {

    protected ListView listView;

    protected EditText searchField;
    protected ImageButton searchBtn;

    protected TextView noProdFound;

    Bundle extras;
    ArrayList<Product> products;
    int category_id;
    Context cont;
    HeadPanel headPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        listView = (ListView) findViewById(R.id.products_list);

        searchField = (EditText) findViewById(R.id.search_field);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);

        noProdFound = (TextView) findViewById(R.id.no_products_field);

        cont = (Context) this;

        setCategoryId();
        setDBProducts();
        setProducts(products);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(ProductsListActivity.this, searchField);
                searchField.clearFocus();
                setProducts(products);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product item = (Product) parent.getItemAtPosition(position);
                Intent intent = new Intent(ProductsListActivity.this, ProductActivity.class);
                intent.putExtra("id", item.id);
                startActivity(intent);
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setProducts(products);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    searchBtn.callOnClick();
                }

                return false;
            }
        });


        PopupMenu.OnMenuItemClickListener popupListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update_item:
                            AlertDialog.Builder updateBuilder = new AlertDialog.Builder(cont);
                            updateBuilder.setTitle(R.string.rename_category);

                            final EditText renameCategoryNameText = new EditText(cont);
                            updateBuilder.setView(renameCategoryNameText);
                            renameCategoryNameText.setText(headPanel.getNameSectorText());

                            updateBuilder.setPositiveButton(R.string.app_apply, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!String.valueOf(renameCategoryNameText.getText()).isEmpty()){
                                        Categories categoriesDB = new Categories(cont);
                                        Category category = categoriesDB.getCategory(category_id);
                                            category.name = String.valueOf(renameCategoryNameText.getText());
                                        categoriesDB.updateCategory(category);

                                        categoriesDB.close();

                                        headPanel.setNameSectorText(category.name);
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
                            deleteBuilder.setMessage(R.string.category_delete_msg);
                            deleteBuilder.setPositiveButton(R.string.app_delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Categories category_db = new Categories(cont);
                                    if (category_db.deleteCategory(category_id) != 0){
                                        Toast.makeText(cont, R.string.category_delete_success_msg, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(cont, R.string.category_delete_failed_msg, Toast.LENGTH_SHORT).show();
                                    }
                                    category_db.close();

                                    Products product_db = new Products(cont);
                                    product_db.deleteProductsByCategory(category_id);
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

        Categories categories = new Categories(this);
        Category categoryName = categories.getCategory(category_id);
        categories.close();
        String description = getString(R.string.category_1);
        headPanel = new HeadPanel(this, categoryName.name, description, this);
        headPanel.setOptionsListener(popupListener);

        LinearLayout linear = (LinearLayout) findViewById(R.id.productLinear);
        linear.addView(headPanel, 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        setDBProducts();
        setProducts(products);
    }

    protected void setCategoryId(){
        category_id = getIntent().getExtras().getInt("id");
    }

    protected void setDBProducts(){
        Products db = new Products(getApplicationContext());
        products = db.getProducts(category_id);
        db.close();
    }

    protected void setProducts(ArrayList<Product> products){
        if(!searchFieldEmpty()){
            products = ProductSearch.filter(products, searchField.getText().toString());

            if (products.size() > 0) {
                if(noProdFound.getVisibility() == View.VISIBLE){
                    noProdFound.setVisibility(View.GONE);
                }
                final ProductListAdapter listData = new ProductListAdapter(this,
                        R.layout.view_product,
                        products);
                listView.setAdapter(listData);
            }else{
                noProducts();
            }
        }
    }

    protected void noProducts(){
        noProdFound.setVisibility(View.VISIBLE);
        listView.setAdapter(null);
    }

    protected boolean searchFieldEmpty(){
        return false;
    }

    protected void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
