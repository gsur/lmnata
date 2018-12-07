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
import realrhinoceros.lmnata.mediators.CategoryAdapter;
import realrhinoceros.lmnata.mediators.Department;
import realrhinoceros.lmnata.mediators.DepartmentSpinnerAdapter;

public class AddCategoryActivity extends AppCompatActivity {
    Spinner departmentsSpinner;
    Spinner brandSpinner;
    EditText categoryNameEditText;
    Button categoryConfirmBtn;
    ListView categoryListView;

    ArrayList<Brand> brands;
    BrandAdapter brandAdapter;
    ArrayList<Category> categories;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        departmentsSpinner = (Spinner) findViewById(R.id.add_category_dept_spinner);
        brandSpinner = (Spinner) findViewById(R.id.add_category_brand_spinner);
        categoryNameEditText = (EditText) findViewById(R.id.add_category_name);
        categoryConfirmBtn = (Button) findViewById(R.id.add_category_btn);
        categoryListView = (ListView) findViewById(R.id.add_category_list);

        Departments departmentsDB = new Departments(getApplicationContext());
        ArrayList<Department> departments = departmentsDB.getDepartments();
        departmentsDB.close();

        if (departments.size() != 0){
            final DepartmentSpinnerAdapter spinnerDeptData = new DepartmentSpinnerAdapter(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    departments);
            departmentsSpinner.setAdapter(spinnerDeptData);
            int brand_id = setBrandAdapter(departments.get(0).id);
            setCategoryAdapter(brand_id);

            departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Department item = (Department) parent.getSelectedItem();
                    setCategoryAdapter(setBrandAdapter(item.id));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Brand item = (Brand) parent.getSelectedItem();
                    setCategoryAdapter(item.id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            Toast.makeText(this, R.string.error_departments_loading, Toast.LENGTH_SHORT).show();
        }

        categoryConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(categoryNameEditText.getText()).isEmpty()){
                    Category category = new Category();
                    Department dept = (Department) departmentsSpinner.getSelectedItem();
                    Brand brand = (Brand) brandSpinner.getSelectedItem();
                    category.department_id = dept.id;
                    category.brand_id = brand.id;
                    category.name = String.valueOf(categoryNameEditText.getText());
                    Categories categoriesDB = new Categories(getApplicationContext());
                    category.id = (int) categoriesDB.insertCategory(category);
                    categoriesDB.close();

                    categoryAdapter.add(category);
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category item = (Category) parent.getItemAtPosition(position);
                Intent intent = new Intent(AddCategoryActivity.this, ProductsListActivity.class);
                intent.putExtra("id", item.id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Brand item = (Brand) brandSpinner.getSelectedItem();
        setCategoryAdapter(item.id);
    }

    protected int setCategoryAdapter(int brand_id){
        categories = setCategories(brand_id);
        categoryAdapter = new CategoryAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                categories);
        categoryListView.setAdapter(categoryAdapter);

        return categories.get(0).id;
    }

    protected ArrayList<Category> setCategories(int brand_id){
        Categories categoriesDB = new Categories(getApplicationContext());
        ArrayList<Category> categories = categoriesDB.getCategories(brand_id);
        categoriesDB.close();

        return categories;
    }

    protected int setBrandAdapter(int dept_id){
        brands = setBrands(dept_id);
        brandAdapter = new BrandAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                brands);
        brandSpinner.setAdapter(brandAdapter);

        return brands.get(0).id;
    }

    protected ArrayList<Brand> setBrands(int dept_id){
        Brands brandsDB = new Brands(getApplicationContext());
        ArrayList<Brand> brands = brandsDB.getBrands(dept_id);
        brandsDB.close();

        return brands;
    }
}
