package realrhinoceros.lmnata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.mediators.Department;
import realrhinoceros.lmnata.mediators.DepartmentListAdapter;

public class DepartmentsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments_list);

        Departments db = new Departments(getApplicationContext());
        ArrayList<Department> departments = db.getDepartments();
        db.dbClose();

        if (departments.size() != 0) {
            ListView listView = (ListView) findViewById(R.id.departments_list);

            final DepartmentListAdapter listData = new DepartmentListAdapter(this,
                    R.layout.view_department,
                    departments);
            listView.setAdapter(listData);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Department item = (Department) parent.getItemAtPosition(position);
                    Intent intent = new Intent(DepartmentsListActivity.this, ProductsListActivity.class);
                    intent.putExtra("id", item.id);
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(this, R.string.error_departments_loading, Toast.LENGTH_SHORT).show();
        }
    }
}
