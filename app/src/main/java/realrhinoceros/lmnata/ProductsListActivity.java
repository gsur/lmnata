package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import realrhinoceros.lmnata.mediators.Product;
import realrhinoceros.lmnata.mediators.ProductListAdapter;

public class ProductsListActivity extends AppCompatActivity {
    TextView noProdFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        final ListView listView = (ListView) findViewById(R.id.products_list);
        final EditText searchField = (EditText) findViewById(R.id.search_field);
        final ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);
        noProdFound = (TextView) findViewById(R.id.no_products_field);

        this.setProductsList(setProductsFromExtra(getIntent().getExtras(), searchField.getText().toString()), listView);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProductsList(setProductsFromExtra(getIntent().getExtras(), searchField.getText().toString()), listView);
            }
        });
    }

    private ArrayList<Product> setProductsFromExtra(Bundle extras, String searchedName){
        ArrayList<Product> products;
        Products db = new Products(getApplicationContext());
        try{
            String selector = extras.getString("selector");
            int department_id = extras.getInt("id");
            switch (selector){
                case "category":
                    products = db.getProductsFromCategory(department_id, extras.getString("item"), searchedName);
                    break;
                case "brand":
                    products = db.getProductsFromBrand(department_id, extras.getString("item"), searchedName);
                    break;
                case "none":
                    products = db.getProductsFromUncategorized(department_id, searchedName);
                    break;
                case "search":
                    products = db.getProductsFromName(searchedName);
                    break;

                default:
                    products = db.getProductsFromDepartment(department_id, searchedName);
                    break;
            }
        }catch (NullPointerException e){
            products = new ArrayList<Product>();
        }finally {
            db.dbClose();
        }

        return products;
    }

    private void setProductsList(ArrayList<Product> products, ListView listView){
        if (products.size() != 0) {
            if(noProdFound.getVisibility() == View.VISIBLE){
                noProdFound.setVisibility(View.GONE);
            }
            final ProductListAdapter listData = new ProductListAdapter(this,
                    R.layout.view_product,
                    products);
            listView.setAdapter(listData);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Product item = (Product) parent.getItemAtPosition(position);
                    Intent intent = new Intent(ProductsListActivity.this, ProductActivity.class);
                    intent.putExtra("id", item.id);
                    startActivity(intent);
                }
            });
        }else{
            noProdFound.setVisibility(View.VISIBLE);
            listView.setAdapter(null);
        }
    }
}
