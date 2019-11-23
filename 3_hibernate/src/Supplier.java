import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int SupplierID;

    private String CompanyName;
    private String Street;
    private String City;

    @OneToMany
    private Set<Product> products = new HashSet<>();

    public Supplier() {
    }

    public Supplier(String companyName, String street, String city) {
        CompanyName = companyName;
        Street = street;
        City = city;
    }

    public void addProduct(Product newProduct){
        products.add(newProduct);
    }

    public void addProductAndInform(Product newProduct){
        products.add(newProduct);
        newProduct.setSupplier(this);
    }
}
