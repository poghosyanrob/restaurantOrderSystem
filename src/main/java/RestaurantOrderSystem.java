import util.Category;
import util.Commands;
import util.Status;
import model.Customer;
import model.Dish;
import model.Order;
import model.OrderItem;
import service.CustomerService;
import service.DishService;
import service.OrderItemService;
import service.OrderService;

import java.util.List;
import java.util.Scanner;

public class RestaurantOrderSystem implements Commands {

    private static Scanner scanner = new Scanner(System.in);
    private static CustomerService customerService = new CustomerService();
    private static DishService dishService = new DishService();
    private static OrderItemService orderItemService = new OrderItemService();
    private static OrderService orderService = new OrderService();

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printCommands();
            String command = scanner.nextLine();
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case ADD_DISHES:
                    addDishes();
                    break;
                case DELETE_DISHES:
                    printDishes();
                    deleteDish();
                    break;
                case UPDATE_DISHES:
                    printDishes();
                    dishUpdate();
                    break;
                case ADD_CUSTOMER:
                    addCustomer();
                    break;
                case SHOW_CUSTOMER:
                    printCustomers();
                    break;
                case ADD_ORDER:
                    printCustomers();
                    addOrder();
                    break;
                case SHOW_ALL_ORDERS:
                    printOrders();
                    break;
                case SHOW_ORDERS_BY_CUSTOMER:
                    printCustomers();
                    printOrdersByCustomer();
                    break;
                case PRINT_ORDER_ITEMS:
                    printOrders();
                    printOrderItems();
                    break;
                case UPDATE_ORDER_STATUS:
                    printOrders();
                    statusUpdate();
                    break;
                case SHOW_MENU:
                    printCategory();
                    printMenu();
                    break;
                default:
                    System.out.println("Wrong command! Try again");

            }
        }

    }

    private static void printOrdersByCustomer() {
        System.out.println("Please input customer id which you would like to see");
        int customerId = Integer.parseInt(scanner.nextLine());
        Customer getCustomer = customerService.getCustomerById(customerId);
        if (getCustomer != null) {
            List<Order> customerOrders = orderService.getCustomerOrders(customerId);
            for (Order customerOrder : customerOrders) {
                System.out.println(customerOrder);
            }
        } else {
            System.out.println("Wrong customer id");
        }
    }

    private static void printMenu() {
        System.out.println("Please input category which you would like ");
        Category category = Category.valueOf(scanner.nextLine().toUpperCase());
        List<Dish> dishByCategory = dishService.getDishByCategory(category);
        for (Dish dish : dishByCategory) {
            System.out.println(dish);
        }
    }

    private static void printOrderItems() {
        System.out.println("Please input order id which you  would like to see orders in detail");
        int orderIdForShowOrderItems = Integer.parseInt(scanner.nextLine());
        List<OrderItem> orderItem = orderItemService.getOrderItem(orderIdForShowOrderItems);
        for (OrderItem item : orderItem) {
            System.out.println(item);
        }
    }

    private static void statusUpdate() {
        System.out.println("Please input order id which you would like to UPDATE ORDER STATUS ");
        int orderIdForStatus = Integer.parseInt(scanner.nextLine());
        Order orderForUpdateStatus = orderService.getOrderById(orderIdForStatus);
        if (orderForUpdateStatus != null) {
            printStatus();
            String status = scanner.nextLine().toUpperCase();
            Status updateStatus = Status.valueOf(status);
            Status previousStatus = orderForUpdateStatus.getStatus();
            if (updateStatus.ordinal() > previousStatus.ordinal()) {
                orderForUpdateStatus.setStatus(updateStatus);
                orderService.changeOrderStatus(orderForUpdateStatus);
                System.out.println("Order status updated successfully!");
            } else {
                System.out.println("You can't update status!");
            }
        } else {
            System.out.println("Wrong order id");
        }

    }

    private static void printStatus() {
        Status[] values = Status.values();
        System.out.print("Status : ");
        for (Status value : values) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static void printOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        System.out.println("Orders : ");
        for (Order order : allOrders) {
            System.out.println(order);
        }
    }

    private static void addOrder() {
        System.out.println("Please input customer id");
        int customerId = Integer.parseInt(scanner.nextLine());
        Customer customerById = customerService.getCustomerById(customerId);
        if (customerById != null) {
            Order order = new Order(customerById);
            orderService.createOrder(order);
            printDishes();
            System.out.println("Please input dish id");
            int dishId = Integer.parseInt(scanner.nextLine());
            Dish dish = dishService.getDishById(dishId);
            if (dish != null) {
                System.out.println("Please input order quantity");
                int quantity = Integer.parseInt(scanner.nextLine());
                OrderItem orderItem = new OrderItem(order, dish, quantity);
                orderItemService.createOrderItem(orderItem);
                order.setTotalPrice(orderItem.getPrice() * orderItem.getQuantity());
                orderService.updateOrder(order);
                System.out.println("Order added successfully!");
            } else {
                System.out.println("Wrong dish id");
            }
        } else {
            System.out.println("Wrong customer id");
        }
    }

    private static void printCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        System.out.println("Customers : ");
        for (Customer allCustomer : allCustomers) {
            System.out.println(allCustomer);
        }
    }

    private static void addCustomer() {
        System.out.println("Please input customer's name, phone, email");
        String customerStr = scanner.nextLine();
        String[] customerArr = customerStr.split(",");
        Customer customer = new Customer();
        customer.setName(customerArr[0]);
        customer.setPhone((customerArr[1]));
        customer.setEmail(customerArr[2]);
        customerService.addCustomer(customer);
        System.out.println("Customer added successfully!");
    }

    private static void dishUpdate() {
        System.out.println("Please input dishes name, category, price which you would like to update");
        dishService.changeDish(getDish());
        System.out.println("Dish updated successfully!");
    }

    private static void deleteDish() {
        System.out.println("Please input dishes' id that you would like to delete");
        int id = Integer.parseInt(scanner.nextLine());
        Dish dishById = dishService.getDishById(id);
        if (dishById != null) {
            dishService.deleteDish(id);
            System.out.println("Dish deleted successfully!");
        } else {
            System.out.println("Wrong dish's id");
        }

    }

    private static void printDishes() {
        System.out.println("Dishes: ");
        List<Dish> allDishes = dishService.getAllDishes();
        for (Dish allDish : allDishes) {
            System.out.println(allDish);
        }
    }

    private static void addDishes() {
        printCategory();
        System.out.println("Please input name, category, price");
        dishService.addDish(getDish());
        System.out.println("Dish added successfully!");

    }

    private static Dish getDish() {
        String dishStr = scanner.nextLine();
        String[] dishArr = dishStr.split(",");
        Dish dish = new Dish();
        dish.setName(dishArr[0]);
        dish.setCategory(Category.valueOf(dishArr[1].toUpperCase()));
        dish.setPrice(Double.parseDouble(dishArr[2]));
        return dish;
    }

    private static void printCategory() {
        Category[] categories = Category.values();
        System.out.print("Category : ");
        for (Category category : categories) {
            System.out.print(category + " ");
        }
        System.out.println();
    }
}
