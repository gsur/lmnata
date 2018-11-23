package realrhinoceros.lmnata.mediators;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BrandAdapter extends ArrayAdapter<Brand> {

    public BrandAdapter(Context context, int layoutId, ArrayList<Brand> brand) {
        super(context, layoutId, brand);
    }

}
