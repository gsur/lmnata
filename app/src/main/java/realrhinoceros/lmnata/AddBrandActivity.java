package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Brands;
import realrhinoceros.lmnata.database.Categories;
import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.mediators.Brand;
import realrhinoceros.lmnata.mediators.BrandAdapter;
import realrhinoceros.lmnata.mediators.Category;
import realrhinoceros.lmnata.mediators.Department;
import realrhinoceros.lmnata.mediators.DepartmentSpinnerAdapter;

public class AddBrandActivity extends AppCompatActivity {

    Spinner departmentsSpinner;
    EditText brandNameEditText;
    Button brandConfirmBtn;
    ListView brandListView;

    ArrayList<Brand> brands;
    BrandAdapter brandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);

        departmentsSpinner = (Spinner) findViewById(R.id.add_brand_dept_spinner);
        brandNameEditText = (EditText) findViewById(R.id.add_brand_name);
        brandConfirmBtn = (Button) findViewById(R.id.add_brand_btn);
        brandListView = (ListView) findViewById(R.id.add_brand_list);

        Departments departmentsDB = new Departments(getApplicationContext());
        ArrayList<Department> departments = departmentsDB.getDepartments();
        departmentsDB.close();

        if (departments.size() != 0){
            final DepartmentSpinnerAdapter spinnerDeptData = new DepartmentSpinnerAdapter(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    departments);
            departmentsSpinner.setAdapter(spinnerDeptData);
            setBrandAdapter(departments.get(0).id);

            departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Department item = (Department) parent.getSelectedItem();
                    setBrandAdapter(item.id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            Toast.makeText(this, R.string.error_departments_loading, Toast.LENGTH_SHORT).show();
        }

        brandConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(brandNameEditText.getText()).isEmpty()){
                    Brand brand = new Brand();
                    Department dept = (Department) departmentsSpinner.getSelectedItem();
                    brand.department_id = dept.id;
                    brand.name = String.valueOf(brandNameEditText.getText());
                    Brands brandsDB = new Brands(getApplicationContext());
                    brand.id = (int) brandsDB.insertBrand(brand);
                    brandsDB.close();

                    Category category = new Category();
                    category.department_id = dept.id;
                    category.brand_id = brand.id;
                    category.name = getResources().getString(R.string.no_category);
                    Categories categoriesDB = new Categories(getApplicationContext());
                    categoriesDB.insertCategory(category);
                    categoriesDB.close();

                    brandAdapter.add(brand);
                    brandAdapter.notifyDataSetChanged();
                }
            }
        });

        brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand item = (Brand) parent.getItemAtPosition(position);
                Intent intent = new Intent(AddBrandActivity.this, CategoriesListActivity.class);
                intent.putExtra("id", item.id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Department item = (Department) departmentsSpinner.getSelectedItem();
        setBrandAdapter(item.id);
    }

    protected int setBrandAdapter(int dept_id){
        brands = setBrands(dept_id);
        brandAdapter = new BrandAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                brands);
        brandListView.setAdapter(brandAdapter);

        return brands.get(0).id;
    }

    protected ArrayList<Brand> setBrands(int dept_id){
        Brands brandsDB = new Brands(getApplicationContext());
        ArrayList<Brand> brands = brandsDB.getBrands(dept_id);
        brandsDB.close();

        return brands;
    }
}
