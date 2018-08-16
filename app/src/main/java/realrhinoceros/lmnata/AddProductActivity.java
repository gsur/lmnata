package realrhinoceros.lmnata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.database.Products;
import realrhinoceros.lmnata.helpers.UriToBitmap;
import realrhinoceros.lmnata.mediators.Department;
import realrhinoceros.lmnata.mediators.DepartmentSpinnerAdapter;
import realrhinoceros.lmnata.mediators.Product;

public class AddProductActivity extends AppCompatActivity {
    EditText nameField;
    EditText articleField;
    EditText descriptionField;
    EditText barecodeField;
    EditText categoryField;
    EditText brandField;
    Spinner departmentsSpinner;
    Button addImageBtn;
    Button confirmBtn;
    ImageView imageView;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameField = (EditText) findViewById(R.id.add_product_name_field);
        articleField = (EditText) findViewById(R.id.add_product_article_field);
        descriptionField = (EditText) findViewById(R.id.add_product_description_field);
        barecodeField = (EditText) findViewById(R.id.add_product_barecode_field);
        categoryField = (EditText) findViewById(R.id.add_product_category_field);
        brandField = (EditText) findViewById(R.id.add_product_brand_field);
        departmentsSpinner = (Spinner) findViewById(R.id.add_product_department_spinner);
        addImageBtn = (Button) findViewById(R.id.add_product_photo_btn);
        imageView = (ImageView) findViewById(R.id.add_product_photo_image);
        confirmBtn = (Button) findViewById(R.id.add_product_confirm_btn);

        product = new Product();

        Departments departmentsDB = new Departments(getApplicationContext());
        ArrayList<Department> departments = departmentsDB.getDepartments();
        departmentsDB.dbClose();

        if (departments.size() != 0){
            final DepartmentSpinnerAdapter spinnerData = new DepartmentSpinnerAdapter(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    departments);
            departmentsSpinner.setAdapter(spinnerData);

            product.department_id = departments.get(0).id;

            departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Department item = (Department) parent.getSelectedItem();
                    product.department_id = item.id;
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

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.name = String.valueOf(nameField.getText());
                product.article = String.valueOf(articleField.getText());
                product.description = String.valueOf(descriptionField.getText());
                product.barecode = String.valueOf(barecodeField.getText());
                product.category = String.valueOf(categoryField.getText());
                product.brand = String.valueOf(brandField.getText());
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
                            productsDB.dbClose();

                            finish();
                        }
                    }
                }
            }
        });
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
