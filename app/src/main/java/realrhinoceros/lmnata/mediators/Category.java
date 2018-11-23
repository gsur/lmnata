package realrhinoceros.lmnata.mediators;

public class Category {

    public int id = -1;
    public int department_id = -1;
    public int brand_id = -1;
    public String name = "";

    public Category() {

    }

    public String toString(){
        return this.name;
    }
}
