package realrhinoceros.lmnata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.helpers.ProductSearch;
import realrhinoceros.lmnata.mediators.Product;
import realrhinoceros.lmnata.mediators.ProductListAdapter;

public class ProductsListActivity extends AppCompatActivity {

    protected ListView listView;

    protected EditText searchField;
    protected ImageButton searchBtn;

    protected TextView noProdFound;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        listView = (ListView) findViewById(R.id.products_list);

        searchField = (EditText) findViewById(R.id.search_field);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);

        noProdFound = (TextView) findViewById(R.id.no_products_field);

        extras = getIntent().getExtras();

        setProducts(extras);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(ProductsListActivity.this, searchField);
                searchField.clearFocus();
                setProducts(extras);
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
                setProducts(extras);
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
    }

    protected void setProducts(Bundle extras){
        if(!searchFieldEmpty()){
            ArrayList<Product> products;
            Products db = new Products(getApplicationContext());

            try {
                String selector = extras.getString("selector");
                int department_id = extras.getInt("id");
                switch (selector) {
                    case "category":
                        products = db.getProductsFromCategory(department_id, extras.getString("item"));
                        break;
                    case "brand":
                        products = db.getProductsFromBrand(department_id, extras.getString("item"));
                        break;
                    case "none":
                        products = db.getProductsFromUncategorized(department_id);
                        break;

                    default:
                        products = db.getProducts();
                        break;
                }
            }catch (NullPointerException e){
                products = db.getProducts();
            }finally {
                db.dbClose();
            }

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
