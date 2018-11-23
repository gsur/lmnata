package realrhinoceros.lmnata;

import realrhinoceros.lmnata.database.Products;

public class SearchActivity extends ProductsListActivity {


    protected boolean searchFieldEmpty(){
        if(!searchField.getText().toString().isEmpty()){
            return false;
        }

        noProducts();
        return true;
    }

    protected void setDBProducts(){
        Products db = new Products(getApplicationContext());
        products = db.getAllProducts();
        db.close();
    }

    protected void setCategoryId(){}
}
