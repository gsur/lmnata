package realrhinoceros.lmnata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

import realrhinoceros.lmnata.custom_views.DepartmentView;
import realrhinoceros.lmnata.database.Departments;
import realrhinoceros.lmnata.mediators.Department;
/*outdated class*/
public class DepartmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        Departments db = new Departments(getApplicationContext());
        ArrayList<Department> departments = db.getDepartments();
        db.close();

        LinearLayout departmentsContainer = (LinearLayout) findViewById(R.id.departments_list);

        for (Department dept : departments){
            DepartmentView departmentView = new DepartmentView(getApplicationContext());
            departmentView.setIcon(dept.image);
            departmentView.setName(dept.name);
            departmentsContainer.addView(departmentView);
        }


    }
}
