import javax.persistence.Entity;

@Entity
public class Customer extends Company{

    private double discount;

    public Customer() {
        super();
    }

    public Customer(String companyName, String street, String city, String zipCode, double discount) {
        super(companyName, street, city, zipCode);
        this.discount = discount;
    }

    @Override
    public void getCompanyType() {
        System.out.println("Customer with company name: " + companyName);
    }
}
