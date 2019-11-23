import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int CategoryID;

    private String Name;

    @OneToMany
    private List<Product> Products = new ArrayList<>();

    public Category() {
    }

    public Category(String name) {
        Name = name;
    }

    public void addProduct(Product newProduct){
        Products.add(newProduct);
    }

    public void addProductAndInform(Product newProduct){
        Products.add(newProduct);
        newProduct.setCategory(this);
    }

    public List<Product> getProducts() {
        return Products;
    }

    @Override
    public String toString(){
        return "Category " + Name + " with ID " + CategoryID;
    }
}
