package realrhinoceros.lmnata.mediators;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {

        public CategoryAdapter(Context context, int layoutId, ArrayList<Category> category) {
            super(context, layoutId, category);
        }

}
