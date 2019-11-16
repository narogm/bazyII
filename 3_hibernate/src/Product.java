import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    private String ProductName;
    private int UnitsOnStock;

    @ManyToOne
    @JoinColumn(name = )
    private Supplier supplier;

    public Product (){}

    public Product(String productName, int unitsOnStock) {
        ProductName = productName;
        UnitsOnStock = unitsOnStock;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
    }
}
