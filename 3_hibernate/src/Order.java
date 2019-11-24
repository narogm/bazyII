import javax.naming.ldap.HasControls;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int OrderID;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    private Set<Product> products = new HashSet<>();

    public Order() {
    }

    public Order(Customer customer) {
        this.customer = customer;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    @Override
    public String toString(){
        return "Order " + OrderID + " made by " + customer;
    }
}
