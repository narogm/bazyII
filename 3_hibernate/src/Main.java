import com.sun.java.swing.plaf.windows.WindowsPopupMenuSeparatorUI;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("derby");
    private static EntityManager em = emf.createEntityManager();
    private final static Scanner input = new Scanner(System.in);

    public static void main(final String[] args) {

        boolean end = false;
        showHelp();

        while(!end){
            System.out.println("\nSelect option:");
            String selectedCase;
            do {
                selectedCase = input.nextLine();
            } while (selectedCase == null || selectedCase.chars().allMatch(Character::isWhitespace));

            switch (selectedCase){
                case "help":
                    showHelp();
                    break;
                case "add product":
                    addProduct();
                    break;
                case "list products":
                    listProducts(false);
                    break;
                case "add customer":
                    addCustomer();
                    break;
                case "list customers":
                    listCustomers(false);
                    break;
                case "add supplier":
                    addSupplier();
                    break;
                case "list suppliers":
                    listSuppliers(false);
                    break;
                case "add order":
                    addOrder();
                    break;
                case "list orders":
                    listOrders(false);
                    break;
                case "add product to order":
                    addProductToOrder();
                    break;
                case "end":
                    end = true;
                    break;
                default:
                    System.out.println("Unrecognized option");
                    showHelp();
                    break;
            }
        }
        em.close();
    }

    private static void addProduct(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        System.out.println("INSERT NAME:");
        String name = input.nextLine();
        System.out.println("INSERT UNITS ON STOCK:");
        int units = input.nextInt();

        Supplier selectedSupplier = listSuppliers(true);

        Product newProduct = new Product(name, units);
        em.persist(newProduct);
        selectedSupplier.addProductAndInform(newProduct);
        transaction.commit();
    }

    private static Product listProducts(boolean returnProduct){
        int index[] = {1};
        List<Product> products = em.createQuery("from Product").getResultList();

        products.forEach((product) -> {
            System.out.println(index[0] + " --> " + product);
            index[0]++;
        });

        if(returnProduct){
            System.out.println("\nSelect product:");
            return products.get(input.nextInt()-1);
        }
        return null;
    }

    private static void addCustomer(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        System.out.println("INSERT COMPANY NAME:");
        String name = input.nextLine();
        System.out.println("INSERT STREET:");
        String street = input.nextLine();
        System.out.println("INSERT CITY");
        String city = input.nextLine();
        System.out.println("INSERT ZIP CODE:");
        String zipCode = input.nextLine();
        System.out.println("INSERT DISCOUNT:");
        double discount = input.nextDouble();

        Customer newCustomer = new Customer(name, street, city, zipCode, discount);

        em.persist(newCustomer);
        transaction.commit();
    }

    private static Customer listCustomers(boolean returnCustomer){
        int index[] = {1};
        List<Customer> customers = em.createQuery("from Customer").getResultList();

        customers.forEach((customer) -> {
            System.out.println(index[0] + " --> " + customer);
            index[0]++;
        });

        if(returnCustomer){
            System.out.println("\nSelect customer:");
            return customers.get(input.nextInt()-1);
        }
        return null;
    }

    private static void addSupplier(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        System.out.println("INSERT COMPANY NAME:");
        String name = input.nextLine();
        System.out.println("INSERT STREET:");
        String street = input.nextLine();
        System.out.println("INSERT CITY");
        String city = input.nextLine();
        System.out.println("INSERT ZIP CODE:");
        String zipCode = input.nextLine();
        System.out.println("INSERT BANK ACCOUNT NUMBER:");
        String bankAccountNumber = input.nextLine();

        Supplier newSupplier = new Supplier(name, street, city, zipCode, bankAccountNumber);

        em.persist(newSupplier);
        transaction.commit();
    }

    private static Supplier listSuppliers(boolean returnSupplier){
        int index[] = {1};
        List<Supplier> suppliers = em.createQuery("from Supplier").getResultList();

        suppliers.forEach((supplier) -> {
            System.out.println(index[0] + " --> " + supplier);
            index[0]++;
        });

        if(returnSupplier){
            System.out.println("\nSelect supplier:");
            return suppliers.get(input.nextInt()-1);
        }
        return null;
    }

    private static void addOrder(){
        System.out.println("Creating new order...");
        System.out.println("Available customers:");
        Customer selectedCustomer = listCustomers(true);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Order newOrder = new Order(selectedCustomer);
        em.persist(newOrder);
        transaction.commit();
    }

    private static Order listOrders(boolean returnOrder){
        int index[] = {1};
        List<Order> orders = em.createQuery("from Order").getResultList();

        orders.forEach((order) -> {
            System.out.println(index[0] + " --> " + order);
            index[0]++;
        });

        if(returnOrder){
            System.out.println("\nSelect order:");
            return orders.get(input.nextInt()-1);
        }
        return null;
    }

    private static void addProductToOrder(){
        System.out.println("Select product you want to add");
        Product selectedProduct = listProducts(true);
        System.out.println("\n to which order you want to add selected product");
        Order selectedOrder = listOrders(true);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        selectedOrder.addProduct(selectedProduct);
        transaction.commit();
    }

    private static void showHelp(){
        System.out.println("Available options:\n" +
                "help\n" +
                "add product\n" +
                "list products\n" +
                "add customer\n" +
                "list customers\n" +
                "add supplier\n" +
                "list suppliers\n" +
                "add order\n" +
                "list orders\n" +
                "add product to order\n" +
                "end");
    }
}