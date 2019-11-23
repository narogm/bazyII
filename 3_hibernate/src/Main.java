import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
//            System.out.println("Podaj nazwe produktu oraz jego ilosc");
//            Scanner inputScanner = new Scanner(System.in);
//            String prodName = inputScanner.nextLine();
//            int prodUnits = Integer.parseInt(inputScanner.nextLine());

            Product product1 = new Product("koszulka", 5);
            Product product2 = new Product("spodnie", 2);
            Product product3 = new Product("kurtka", 3);

            Transaction tx = session.beginTransaction();
            session.save(product1);
            session.save(product2);
            session.save(product3);

            Invoice invoice1 = new Invoice(456);
            Invoice invoice2 = new Invoice(321);

            session.save(invoice1);
            session.save(invoice2);

            invoice1.addProductAndInform(product1);
            invoice1.addProductAndInform(product2);
            product3.addInvoiceAndInform(invoice1);

            System.out.println("Products in invoice1:");
            invoice1.getIncludedProducts().forEach(System.out::println);

            product2.addInvoiceAndInform(invoice2);

            System.out.println("\nProduct2's invoices:");
            product2.getInvoices().forEach(System.out::println);

//            Category category1 = new Category("FirstCategory");
//            Category category2 = new Category("SecondCategory");
//
//            session.save(category1);
//            session.save(category2);
//
//            category1.addProductAndInform(product1);
//            category1.addProductAndInform(product2);
//
//            category2.addProductAndInform(product3);
//
//            System.out.println("Products in category1:");
//            category1.getProducts().forEach(System.out::println);
//
//            System.out.println("\nCategory for product3: " + product3.getCategory());

//            Supplier supplier = new Supplier("SuppCompany", "MainStreet", "SomeCity");
//            session.save(supplier);
//            supplier.addProductAndInform(product1);
//            supplier.addProductAndInform(product2);
//
//            Supplier supplier2 = new Supplier("SecondCompany", "OtherStreet", "SomeCity");
//            session.save(supplier2);
//            supplier2.addProductAndInform(product3);

            tx.commit();
        } finally {
            session.close();
        }
    }
}