import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int InvoiceID;

    private int InvoiceNumber;
    private int Quantity;

    @ManyToMany
    private Set<Product> includedProducts = new HashSet<>();

    public Invoice() {
    }

    public Invoice(int invoiceNumber) {
        InvoiceNumber = invoiceNumber;
        Quantity = 0;
    }

    public void addProduct(Product product){
        if (!includedProducts.contains(product)){
            this.includedProducts.add(product);
            Quantity++;
        }
    }
    public void addProductAndInform(Product product){
        addProduct(product);
        product.addInvoice(this);
    }

    public Set<Product> getIncludedProducts() {
        return includedProducts;
    }
}
