package realrhinoceros.lmnata;

public class SearchActivity extends ProductsListActivity {

    protected boolean searchFieldEmpty(){
        if(!searchField.getText().toString().isEmpty()){
            return false;
        }

        noProducts();
        return true;
    }
}
