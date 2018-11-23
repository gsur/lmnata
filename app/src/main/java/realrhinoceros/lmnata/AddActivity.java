package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button addProductBtn = (Button) findViewById(R.id.addProductBtn);
        Button addBrandBtn = (Button) findViewById(R.id.addBrandBtn);
        Button addCategoryBtn = (Button) findViewById(R.id.addCategoryBtn);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        addBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, AddBrandActivity.class);
                startActivity(intent);
            }
        });

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
