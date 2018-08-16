package realrhinoceros.lmnata.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import realrhinoceros.lmnata.R;

public class UriToBitmap {

    public static void parse(Context context, Uri uri, ImageView imageView) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            /*int scale = imageView.getWidth();
            selectedImage.setWidth(scale);
            selectedImage.setHeight(scale);*/
            imageView.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            imageView.setContentDescription(context.getString(R.string.file_not_found));
        }

    }
}
