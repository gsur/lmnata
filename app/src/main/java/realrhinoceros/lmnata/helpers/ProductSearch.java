package realrhinoceros.lmnata.helpers;

import java.util.ArrayList;

import realrhinoceros.lmnata.mediators.Product;

public class ProductSearch {

    public ProductSearch(){}

    public static ArrayList<Product> filter(ArrayList<Product> base, String name){
        if (name.length() <= 0){
            return base;
        }

        ArrayList<Product> sample = new ArrayList<Product>();
        String required = name.toLowerCase();
        for(Product product : base){
            if(product.name.toLowerCase().contains(required.toLowerCase())){
                sample.add(product);
            }
        }

        return sample;
    }
}
