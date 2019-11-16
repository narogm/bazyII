import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;

import java.util.Map;
import java.util.Scanner;

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

            Supplier supplier = new Supplier("SuppCompany", "MainStreet", "SomeCity");
            session.save(supplier);
            supplier.addProduct(product1);
            supplier.addProduct(product2);
            supplier.addProduct(product3);

//            Product foundProduct = session.get(Product.class, 1);
//            foundProduct.setSupplier(supplier);
            tx.commit();
        } finally {
            session.close();
        }
    }
}

//            System.out.println("querying all the managed entities...");
//            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
//            for (EntityType<?> entityType : metamodel.getEntities()) {
//                final String entityName = entityType.getName();
//                final Query query = session.createQuery("from " + entityName);
//                System.out.println("executing: " + query.getQueryString());
//                for (Object o : query.list()) {
//                    System.out.println("  " + o);
//                }
//            }