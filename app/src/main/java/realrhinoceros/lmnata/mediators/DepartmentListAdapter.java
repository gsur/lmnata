package realrhinoceros.lmnata.mediators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import realrhinoceros.lmnata.R;

public class DepartmentListAdapter extends ArrayAdapter<Department> {
    private ArrayList<Department> departments;
    private LayoutInflater inflater;
    private int layout;
    private Context context;

    public DepartmentListAdapter(Context context, int layoutId, ArrayList<Department> departments) {
        super(context, layoutId, departments);
        this.context = context;
        this.departments = departments;
        this.inflater = LayoutInflater.from(context);;
        this.layout = layoutId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.department_icon);
        TextView textView = (TextView) view.findViewById(R.id.department_name);


        Department department = this.departments.get(position);

        imageView.setImageResource(context.getResources().getIdentifier(department.image,
                                                                "drawable",
                                                                context.getPackageName()));
        textView.setText(department.name);

        return view;
    }
}
