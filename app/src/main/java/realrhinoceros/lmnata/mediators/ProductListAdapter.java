package realrhinoceros.lmnata.mediators;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import realrhinoceros.lmnata.R;
import realrhinoceros.lmnata.database.Products;
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
        TextView textViewDescr = (TextView) view.findViewById(R.id.product_description);
        //ImageButton btnViewDelete = (ImageButton) view.findViewById(R.id.product_delete_btn);

        final Product product = this.products.get(position);

        Uri imageUri = Uri.parse(product.image);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), imageUri);
            Bitmap finalBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
            //bitmap.setHeight(130);
            //bitmap.setWidth(130);
            imageView.setImageBitmap(finalBmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textViewName.setText(product.name);
        textViewArticle.setText(product.article);
        textViewDescr.setText(product.description);

        /*btnViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(R.string.app_delete_msg);
                dialog.setPositiveButton(R.string.app_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Products db = new Products(context);
                        if (db.deleteProduct(product.id) != 0){
                            Toast.makeText(context, R.string.delete_success_msg, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, R.string.delete_failed_msg, Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                        remove(product);
                        notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });*/

        return view;
    }
}
