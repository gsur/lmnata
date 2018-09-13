package realrhinoceros.lmnata;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import realrhinoceros.lmnata.helpers.LMNataPermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!new LMNataPermissions().checkPermissionREAD_EXTERNAL_STORAGE(this)){
            LinearLayout mainLayout =(LinearLayout) findViewById(R.id.mainActivityLayout);
            TextView textView = new TextView(this);
            textView.setTextColor(getApplicationContext().getResources().getColor(R.color.error));
            textView.setTextSize(13);
            textView.setText(R.string.error_permissions);
            mainLayout.addView(textView, 0);
        }

        Button selectDepartmentBtn = (Button) findViewById(R.id.selectDepartmentBtn);
        Button addProductBtn = (Button) findViewById(R.id.addProductBtn);
        Button searchBtn = (Button) findViewById(R.id.searchProductBtn);

        selectDepartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DepartmentsListActivity.class);
                startActivity(intent);
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsListActivity.class);
                intent.putExtra("selector", "search");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LMNataPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


}
