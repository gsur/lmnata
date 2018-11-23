package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.mediators.Product;
import realrhinoceros.lmnata.mediators.ProductListAdapter;

public class BrandCategoryListActivity extends AppCompatActivity {

    protected int department_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_category_list);

        department_id = getIntent().getExtras().getInt("id");

        Products db = new Products(getApplicationContext());
        ArrayList<String> categories = new ArrayList();// = db.getCategories(department_id);
        ArrayList<String> brands = new ArrayList();// = db.getBrands(department_id);
        db.close();

        ListView catListView = (ListView) findViewById(R.id.category_list);

        if (categories.size() != 0){
            final ArrayAdapter<String> catListData = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    categories);
            catListView.setAdapter(catListData);

            catListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent(BrandCategoryListActivity.this, ProductsListActivity.class);
                    intent.putExtra("id", department_id);
                    intent.putExtra("selector", "category");
                    intent.putExtra("item", item);
                    startActivity(intent);
                }
            });
        }

        if (brands.size() != 0) {
            ListView brandListView = (ListView) findViewById(R.id.brand_list);

            final ArrayAdapter<String> brandListData = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    brands);
            brandListView.setAdapter(brandListData);

            brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent(BrandCategoryListActivity.this, ProductsListActivity.class);
                    intent.putExtra("id", department_id);
                    intent.putExtra("selector", "brand");
                    intent.putExtra("item", item);
                    startActivity(intent);
                }
            });
        }

        Button uncatBtn = (Button) findViewById(R.id.uncat_btn);

        uncatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrandCategoryListActivity.this, ProductsListActivity.class);
                intent.putExtra("id", department_id);
                intent.putExtra("selector", "none");
                startActivity(intent);
            }
        });
    }
}
