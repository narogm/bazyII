import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ProductID;

    private String ProductName;
    private int UnitsOnStock;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private Category category;

    @ManyToMany
    private Set<Invoice> Invoices = new HashSet<>();

    public Product (){}

    public Product(String productName, int unitsOnStock) {
        this.ProductName = productName;
        this.UnitsOnStock = unitsOnStock;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setSupplierAndInform(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public void setCategoryAndInform(Category category){
        this.category = category;
        this.category.addProduct(this);
    }

    public void addInvoice(Invoice invoice){
        this.Invoices.add(invoice);
    }

    public void addInvoiceAndInform(Invoice invoice){
        this.Invoices.add(invoice);
        invoice.addProduct(this);
    }

    @Override
    public String toString(){
        return "Product " + ProductName + " with ID " + ProductID;
    }

    public String getCategory() {
        return String.valueOf(category);
    }

    public Set<Invoice> getInvoices() {
        return Invoices;
    }
}
