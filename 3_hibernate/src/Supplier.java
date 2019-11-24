import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
//@SecondaryTable(name = "ADDRESS_TABLE")
public class Supplier extends Company{

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int SupplierID;

    private String bankAccountNumber;
//    private String CompanyName;

//    @Column(table = "ADDRESS_TABLE")
//    private String Street;
//    @Column(table = "ADDRESS_TABLE")
//    private String City;

//    @Embedded
//    private Address address;

    @OneToMany
    private Set<Product> products = new HashSet<>();

    public Supplier() {
        super();
    }

    public Supplier(String companyName, String street, String city, String zipCode, String bankAccountNumber) {
        super(companyName, street, city, zipCode);
        this.bankAccountNumber = bankAccountNumber;
//        CompanyName = companyName;
//        Street = street;
//        City = city;
    }

    //    public Supplier(String companyName, Address address) {
//        CompanyName = companyName;
//        this.address = address;
//    }

    public void addProduct(Product newProduct){
        products.add(newProduct);
    }

    public void addProductAndInform(Product newProduct){
        products.add(newProduct);
        newProduct.setSupplier(this);
    }

    @Override
    public void getCompanyType() {
        System.out.println("Supplier with company name: " + companyName);
    }

    @Override
    public String toString(){
        return "Supplier: " + companyName;
    }
}
