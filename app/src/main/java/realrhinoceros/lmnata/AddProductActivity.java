package realrhinoceros.lmnata;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
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

public class AddProductActivity extends AppCompatActivity {
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
        addCategoryBtn = (ImageButton) findViewById(R.id.ap_add_category_btn);

        product = new Product();

        Departments departmentsDB = new Departments(getApplicationContext());
        ArrayList<Department> departments = departmentsDB.getDepartments();
        departmentsDB.close();

        if (departments.size() != 0){
            final DepartmentSpinnerAdapter spinnerDeptData = new DepartmentSpinnerAdapter(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    departments);
            departmentsSpinner.setAdapter(spinnerDeptData);
            product.department_id = departments.get(0).id;
            product.brand_id = setBrandAdapter(product.department_id);
            product.category_id = setCategoryAdapter(product.brand_id);

            departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Department item = (Department) parent.getSelectedItem();
                    product.department_id = item.id;
                    product.brand_id = setBrandAdapter(product.department_id);
                    product.category_id = setCategoryAdapter(product.brand_id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            brandsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Brand item = (Brand) parent.getSelectedItem();
                    product.brand_id = item.id;
                    product.category_id = setCategoryAdapter(product.brand_id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Category item = (Category) parent.getSelectedItem();
                    product.category_id = item.id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            Toast.makeText(this, R.string.error_departments_loading, Toast.LENGTH_SHORT).show();
        }

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        addBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qwe", "qwe");
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setTitle(R.string.add_new_brand);

                final EditText addBrandNameText = new EditText(AddProductActivity.this);
                builder.setView(addBrandNameText);

                builder.setPositiveButton(R.string.app_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!String.valueOf(addBrandNameText.getText()).isEmpty()){
                            Brand brand = new Brand();
                            brand.department_id = product.department_id;
                            brand.name = String.valueOf(addBrandNameText.getText());
                            Brands brandsDB = new Brands(getApplicationContext());
                            brand.id = (int) brandsDB.insertBrand(brand);
                            brandsDB.close();
                            product.brand_id = brand.id;

                            Category category = new Category();
                            category.department_id = product.department_id;
                            category.brand_id = product.brand_id;
                            category.name = getResources().getString(R.string.no_category);
                            Categories categoriesDB = new Categories(getApplicationContext());
                            categoriesDB.insertCategory(category);
                            categoriesDB.close();

                            spinnerBrandsData.add(brand);
                            spinnerBrandsData.notifyDataSetChanged();
                            brandsSpinner.setSelection(spinnerBrandsData.getPosition(brand));

                            product.category_id = setCategoryAdapter(brand.id);
                        }
                        else{
                            dialog.cancel();
                        }
                    }
                });

                builder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qwe", "qwe");
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setTitle(R.string.add_new_category);

                final EditText addCategoryNameText = new EditText(AddProductActivity.this);
                builder.setView(addCategoryNameText);

                builder.setPositiveButton(R.string.app_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!String.valueOf(addCategoryNameText.getText()).isEmpty()){
                            Category category = new Category();
                            category.department_id = product.department_id;
                            category.brand_id = product.brand_id;
                            category.name = String.valueOf(addCategoryNameText.getText());
                            Categories categoriesDB = new Categories(getApplicationContext());
                            category.id = (int) categoriesDB.insertCategory(category);
                            categoriesDB.close();
                            product.category_id = category.id;

                            spinnerCategoriesData.add(category);
                            spinnerCategoriesData.notifyDataSetChanged();
                            categoriesSpinner.setSelection(spinnerCategoriesData.getPosition(category));
                        }
                        else{
                            dialog.cancel();
                        }
                    }
                });

                builder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

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
                            if(productsDB.insertProduct(product) == -1){
                                Toast.makeText(getApplicationContext(), R.string.error_product_insert, Toast.LENGTH_LONG).show();
                            }
                            productsDB.close();

                            finish();
                        }
                    }
                }
            }
        });
    }

    protected ArrayList<Brand> setBrands(int dept_id){
        Brands brandsDB = new Brands(getApplicationContext());
        ArrayList<Brand> brands = brandsDB.getBrands(dept_id);
        brandsDB.close();

        return brands;
    }

    protected ArrayList<Category> setCategories(int brand_id){
        Categories categoriesDB = new Categories(getApplicationContext());
        ArrayList<Category> categories = categoriesDB.getCategories(brand_id);
        categoriesDB.close();

        return categories;
    }

    protected int setBrandAdapter(int dept_id){
        brands = setBrands(dept_id);
        spinnerBrandsData = new BrandAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                brands);
        brandsSpinner.setAdapter(spinnerBrandsData);

        return brands.get(0).id;
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
