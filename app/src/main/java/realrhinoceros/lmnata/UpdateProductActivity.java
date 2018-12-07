package realrhinoceros.lmnata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Brands;
import realrhinoceros.lmnata.database.Categories;
import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.helpers.UriToBitmap;
import realrhinoceros.lmnata.mediators.Brand;
import realrhinoceros.lmnata.mediators.BrandAdapter;
import realrhinoceros.lmnata.mediators.Category;
import realrhinoceros.lmnata.mediators.CategoryAdapter;
import realrhinoceros.lmnata.mediators.Department;
import realrhinoceros.lmnata.mediators.DepartmentSpinnerAdapter;
import realrhinoceros.lmnata.mediators.Product;

import static android.view.View.GONE;

public class UpdateProductActivity extends AppCompatActivity {

    EditText nameField;
    EditText articleField;
    EditText descriptionField;
    EditText barecodeField;
    /*EditText categoryField;
    EditText brandField;*/
    Spinner departmentsSpinner;
    Spinner brandsSpinner;
    Spinner categoriesSpinner;
    Button addImageBtn;
    Button confirmBtn;
    ImageView imageView;

    ImageButton addBrandBtn;
    ImageButton addCategoryBtn;

    Product product;
    ArrayList<Brand> brands;
    ArrayList<Category> categories;
    BrandAdapter spinnerBrandsData;
    CategoryAdapter spinnerCategoriesData;

    boolean onCreateDept = true;
    boolean onCreateBrand = true;
    boolean onCreateCat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameField = (EditText) findViewById(R.id.add_product_name_field);
        articleField = (EditText) findViewById(R.id.add_product_article_field);
        descriptionField = (EditText) findViewById(R.id.add_product_description_field);
        barecodeField = (EditText) findViewById(R.id.add_product_barecode_field);
        /*categoryField = (EditText) findViewById(R.id.add_product_category_field);
        brandField = (EditText) findViewById(R.id.add_product_brand_field);*/
        departmentsSpinner = (Spinner) findViewById(R.id.add_product_department_spinner);
        brandsSpinner = (Spinner) findViewById(R.id.add_product_brand_spinner);
        categoriesSpinner = (Spinner) findViewById(R.id.add_product_category_spinner);
        addImageBtn = (Button) findViewById(R.id.add_product_photo_btn);
        imageView = (ImageView) findViewById(R.id.add_product_photo_image);
        confirmBtn = (Button) findViewById(R.id.add_product_confirm_btn);

        addBrandBtn = (ImageButton) findViewById(R.id.ap_add_brand_btn);
            addBrandBtn.setVisibility(GONE);
        addCategoryBtn = (ImageButton) findViewById(R.id.ap_add_category_btn);
            addCategoryBtn.setVisibility(GONE);

        Products productsDB = new Products(this);
        product = productsDB.getProduct(getIntent().getExtras().getInt("id"));
        productsDB.close();

        nameField.setText(product.name);
        articleField.setText(product.article);
        descriptionField.setText(product.description);
        barecodeField.setText(product.barecode);

        UriToBitmap.parse(this, Uri.parse(product.image), imageView);

        departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!onCreateDept){
                    Department item = (Department) parent.getSelectedItem();
                    product.department_id = item.id;
                    product.brand_id = setBrandAdapter(product.department_id);
                    product.category_id = setCategoryAdapter(product.brand_id);
                }
                else{
                    onCreateDept = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brandsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!onCreateBrand){
                    Brand item = (Brand) parent.getSelectedItem();
                    product.brand_id = item.id;
                    product.category_id = setCategoryAdapter(product.brand_id);
                }
                else{
                    onCreateBrand = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!onCreateCat){
                    Category item = (Category) parent.getSelectedItem();
                    product.category_id = item.id;
                }
                else{
                    onCreateCat = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        confirmBtn.setText(R.string.app_apply);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.name = String.valueOf(nameField.getText());
                product.article = String.valueOf(articleField.getText());
                product.description = String.valueOf(descriptionField.getText());
                product.barecode = String.valueOf(barecodeField.getText());
                product.approved = 1;

                if(product.name.replace(" ", "").length() == 0){
                    Toast.makeText(getApplicationContext(),
                            String.format(getString(R.string.error_field_empty), getString(R.string.product_name)),
                            Toast.LENGTH_LONG).show();
                }else{
                    if(product.article.replace(" ", "").length() == 0){
                        Toast.makeText(getApplicationContext(),
                                String.format(getString(R.string.error_field_empty), getString(R.string.product_article)),
                                Toast.LENGTH_LONG).show();
                    }else{
                        if(product.barecode.length() != 12){
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.error_barcode_length),
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Products productsDB = new Products(getApplicationContext());
                            if(productsDB.updateProduct(product) == -1){
                                Toast.makeText(getApplicationContext(), R.string.error_product_insert, Toast.LENGTH_LONG).show();
                            }
                            productsDB.close();

                            finish();
                        }
                    }
                }
            }
        });

        Departments departmentsDB = new Departments(getApplicationContext());
        ArrayList<Department> departments = departmentsDB.getDepartments();
        departmentsDB.close();

        if (departments.size() != 0){
            final DepartmentSpinnerAdapter spinnerDeptData = new DepartmentSpinnerAdapter(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    departments);
            departmentsSpinner.setAdapter(spinnerDeptData);
            int index = 0;
            for(Department dept : departments){
                if (dept.id == product.department_id){
                    break;
                }

                index++;
            }
            departmentsSpinner.setSelection(index);
            Log.i("DEPT INDEX", String.valueOf(index));
            setBrandAdapter(product.department_id);
            index = 0;
            for(Brand brand : brands){
                if (brand.id == product.brand_id){
                    break;
                }

                index++;
            }
            brandsSpinner.setSelection(index);
            Log.i("BRAND INDEX", String.valueOf(index));
            setCategoryAdapter(product.brand_id);
            index = 0;
            for(Category category : categories){
                if (category.id == product.category_id){
                    break;
                }
                else{
                    index++;
                }
            }
            categoriesSpinner.setSelection(index);
            Log.i("CAT INDEX", String.valueOf(index));
        }else{
            Toast.makeText(this, R.string.error_departments_loading, Toast.LENGTH_SHORT).show();
        }
    }

    protected ArrayList<Brand> setBrands(int dept_id){
        Brands brandsDB = new Brands(getApplicationContext());
        ArrayList<Brand> brands = brandsDB.getBrands(dept_id);
        brandsDB.close();

        return brands;
    }

    protected int setBrandAdapter(int dept_id){
        brands = setBrands(dept_id);
        spinnerBrandsData = new BrandAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                brands);
        brandsSpinner.setAdapter(spinnerBrandsData);

        return brands.get(0).id;
    }

    protected ArrayList<Category> setCategories(int brand_id){
        Categories categoriesDB = new Categories(getApplicationContext());
        ArrayList<Category> categories = categoriesDB.getCategories(brand_id);
        categoriesDB.close();

        return categories;
    }

    protected int setCategoryAdapter(int brand_id){
        categories = setCategories(brand_id);
        spinnerCategoriesData = new CategoryAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                categories);
        categoriesSpinner.setAdapter(spinnerCategoriesData);

        return categories.get(0).id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    try{
                        Uri imageUri = imageReturnedIntent.getData();
                        UriToBitmap.parse(this, imageUri, imageView);
                        product.image = imageUri.toString();
                    }catch (Exception exception){
                        product.image = getString(R.string.file_not_found);
                        imageView.setContentDescription(product.image);
                    }

                }
        }
    }
}
