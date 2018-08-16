package realrhinoceros.lmnata.mediators;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import realrhinoceros.lmnata.R;
import realrhinoceros.lmnata.helpers.UriToBitmap;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;
    private LayoutInflater inflater;
    private int layout;
    private Context context;

    public ProductListAdapter(Context context, int layoutId, ArrayList<Product> departments) {
    super(context, layoutId, departments);
        this.context = context;
        this.products = departments;
        this.inflater = LayoutInflater.from(context);;
        this.layout = layoutId;
}

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.product_icon);
        TextView textViewName = (TextView) view.findViewById(R.id.product_name);
        TextView textViewArticle = (TextView) view.findViewById(R.id.product_article);


        Product product = this.products.get(position);

        Uri imageUri = Uri.parse(product.image);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), imageUri);
            Bitmap finalBmp = Bitmap.createScaledBitmap(bitmap, 250, 250, false);
            //bitmap.setHeight(130);
            //bitmap.setWidth(130);
            imageView.setImageBitmap(finalBmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageView.setImageURI(imageUri);
        textViewName.setText(product.name);
        textViewArticle.setText(product.article);

        return view;
    }
}
