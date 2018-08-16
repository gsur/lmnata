package realrhinoceros.lmnata.custom_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import realrhinoceros.lmnata.R;

/*outdated class*/
public class DepartmentView extends LinearLayout {
    private ImageView departmentIcon;
    private TextView departmentName;
    private Context context;

    public DepartmentView(Context context) {
        super(context);
        this.context = context;
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_department, this);
        }
        departmentIcon = (ImageView) findViewById(R.id.department_icon);
        departmentName = (TextView) findViewById(R.id.department_name);
    }

    public void setIcon(String image) {

        departmentIcon.setImageResource(context.getResources().getIdentifier(image, "drawable", context.getPackageName()));
    }

    public void setName(String name) {
        departmentName.setText(name);
    }
}
