package realrhinoceros.lmnata.mediators;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DepartmentSpinnerAdapter extends ArrayAdapter<Department> {
    /*private ArrayList<Department> departments;
    private Context context;*/

    public DepartmentSpinnerAdapter(Context context, int layoutId, ArrayList<Department> departments) {
        super(context, layoutId, departments);
        /*this.context = context;
        this.departments = departments;*/
    }

    /*public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }*/


}
