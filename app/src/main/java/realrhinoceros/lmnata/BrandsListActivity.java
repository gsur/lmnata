package realrhinoceros.lmnata;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.custom_views.HeadPanel;
import realrhinoceros.lmnata.database.Brands;
import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.mediators.Brand;
import realrhinoceros.lmnata.mediators.BrandAdapter;
import realrhinoceros.lmnata.mediators.Department;

public class BrandsListActivity extends AppCompatActivity {

    protected int department_id;
    ArrayList<Brand> brands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands_list);

        department_id = getIntent().getExtras().getInt("id");

        setBrands();

        Departments departments = new Departments(this);
        Department deptName = departments.getDepartment(department_id);
        departments.close();
        String description = getString(R.string.department);
        HeadPanel headPanel = new HeadPanel(this, deptName.name, description, this);
        headPanel.setOptionsBtnVisibility(View.GONE);

        LinearLayout linear = (LinearLayout) findViewById(R.id.brandsLinear);
        linear.addView(headPanel, 0);
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
