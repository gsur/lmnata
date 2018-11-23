package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Brands;
import realrhinoceros.lmnata.mediators.Brand;
import realrhinoceros.lmnata.mediators.BrandAdapter;

public class BrandsListActivity extends AppCompatActivity {

    protected int department_id;
    ArrayList<Brand> brands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands_list);

        department_id = getIntent().getExtras().getInt("id");

        setBrands();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBrands();
    }

    protected void setBrands(){
        Brands db = new Brands(getApplicationContext());
        brands = db.getBrands(department_id);
        db.close();

        if (brands.size() != 0) {
            ListView listView = (ListView) findViewById(R.id.brands_list);

            final BrandAdapter listData = new BrandAdapter(this,
                    android.R.layout.simple_list_item_1,
                    brands);
            listView.setAdapter(listData);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Brand item = (Brand) parent.getItemAtPosition(position);
                    Intent intent = new Intent(BrandsListActivity.this, CategoriesListActivity.class);
                    intent.putExtra("id", item.id);
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(this, R.string.error_brands_loading, Toast.LENGTH_SHORT).show();
        }
    }
}
