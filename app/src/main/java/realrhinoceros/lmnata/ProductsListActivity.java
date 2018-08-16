package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.mediators.Product;
import realrhinoceros.lmnata.mediators.ProductListAdapter;

public class ProductsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        int department_id = getIntent().getExtras().getInt("id");

        Products db = new Products(getApplicationContext());
        ArrayList<Product> products = db.getProductsFromDepartment(department_id);
        db.dbClose();

        if (products.size() != 0) {
            ListView listView = (ListView) findViewById(R.id.products_list);

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
            Toast.makeText(this, R.string.error_products_loading, Toast.LENGTH_SHORT).show();
        }
    }
}
