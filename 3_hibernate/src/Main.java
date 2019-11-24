import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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
        Main app = new Main();

//        app.HibernateVersion();
        app.JPAversion();
    }

    public void HibernateVersion(){
        final Session session = getSession();
        try {
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

    public void JPAversion(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("derby");
        EntityManager em = emf.createEntityManager();
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        Product product1 = new Product("koszulka", 5);
        Product product2 = new Product("spodnie", 2);
//        Product product3 = new Product("kurtka", 3);

        Supplier supplier = new Supplier("SuppCompany", "MainStreet", "SomeCity","33-333","123456789");
        Customer customer = new Customer("CustCompany","OtherStreet","SecondCity","44-444",0.1);

        em.persist(supplier);
        em.persist(customer);

        List<Company> allCompanies = em.createQuery("from Company").getResultList();
        for(Company company: allCompanies){
            company.getCompanyType();
        }
//        Address address = new Address("MainStreet", "SomeCity");
//        Supplier supplier = new Supplier("SuppCompany", address);

//        Supplier supplier = new Supplier("SuppCompany", "MainStreet", "SomeCity","33-333");
//        supplier.addProductAndInform(product1);
//        supplier.addProductAndInform(product2);
//
//        em.persist(product1);
//        em.persist(product2);
//        em.persist(supplier);

//        Invoice invoice1 = new Invoice(456);
//        Invoice invoice2 = new Invoice(321);
//
//        invoice1.addProductAndInform(product1);
//        invoice1.addProductAndInform(product2);
//
//        product3.addInvoiceAndInform(invoice2);
//
//        em.persist(invoice1);
//        em.persist(product3);

        //        Category category1 = new Category("FirstCategory");
//        Category category2 = new Category("SecondCategory");
//        em.persist(product1);
//        em.persist(product2);
//        em.persist(product3);
//        em.persist(category1);
//        em.persist(category2);
//
//        category1.addProductAndInform(product1);
//        category1.addProductAndInform(product2);
//
//        category2.addProductAndInform(product3);
//
//        System.out.println("Products in category1:");
//        category1.getProducts().forEach(System.out::println);
//        System.out.println("\nCategory for product3: " + product3.getCategory());
        etx.commit();
        em.close();
    }
}