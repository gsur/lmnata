package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Categories;
import realrhinoceros.lmnata.mediators.Category;
import realrhinoceros.lmnata.mediators.CategoryAdapter;

public class CategoriesListActivity extends AppCompatActivity {

    protected int brand_id;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        brand_id = getIntent().getExtras().getInt("id");

        setCategories();
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
