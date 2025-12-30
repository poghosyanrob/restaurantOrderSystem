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
import java.util.Optional;
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
                    System.err.println("Wrong command! Try again");

            }
        }

    }

    private static void printOrdersByCustomer() {
        System.out.println("Please input customer id which you would like to see");
        int customerId = Integer.parseInt(scanner.nextLine());
        Optional<Customer> getCustomer = Optional.ofNullable(customerService.getCustomerById(customerId));
        getCustomer.ifPresentOrElse(
                customer -> {
                    List<Order> customerOrders = orderService.getCustomerOrders(customerId);
                    for (Order customerOrder : customerOrders) {
                        System.out.println(customerOrder);
                    }
                },
                () -> System.err.println("Wrong customer id")
        );
    }

    private static void printMenu() {
        System.out.println("Please input category which you would like ");
        String categoryStr = scanner.nextLine().toUpperCase();
        switch (categoryStr) {
            case "APPETIZER" :
            case "MAIN":
            case "DESSERT":
            case "DRINK":
                Category category = Category.valueOf(categoryStr);
                List<Dish> dishByCategory = dishService.getDishByCategory(category);
                    for (Dish dish : dishByCategory) {
                        System.out.println(dish);
                    };
                    break;
            default: System.err.println("Wrong category");
        }
    }

    private static void printOrderItems() {
        System.out.println("Please input order id which you  would like to see orders in detail");
        int orderIdForShowOrderItems = Integer.parseInt(scanner.nextLine());
        Optional<Order> orderOptional = Optional.ofNullable(orderService.getOrderById(orderIdForShowOrderItems));
        orderOptional.ifPresentOrElse(
                order -> {
                    System.out.println(orderItemService.getOrderItem(order.getId()));
                },()-> System.err.println("Wrong order's id")
        );
    }

    private static void statusUpdate() {
        System.out.println("Please input order id which you would like to UPDATE ORDER STATUS ");
        int orderIdForStatus = Integer.parseInt(scanner.nextLine());
        Order orderForUpdateStatus = orderService.getOrderById(orderIdForStatus);
        Optional<Order> orderStatusOptional = Optional.ofNullable(orderForUpdateStatus);
        orderStatusOptional.ifPresentOrElse(
                order -> {
                    printStatus();
                    String status = scanner.nextLine().toUpperCase();
                    switch (status) {
                        case "PENDING" :
                        case "PREPARING":
                        case "READY":
                        case "DELIVERED":
                            Status updateStatus = Status.valueOf(status);
                            Status previousStatus = order.getStatus();
                            if (updateStatus.ordinal() > previousStatus.ordinal()) {
                                order.setStatus(updateStatus);
                                orderService.changeOrderStatus(order);
                                System.out.println("Order status updated successfully!");
                            } else {
                                System.err.println("You can't update status!");
                            }
                            break;
                        default: System.err.println("Wrong status");
                    }
                },
                () -> System.err.println("Wrong order's id")
        );
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
        Optional<Customer> customerByIdOptional = Optional.ofNullable(customerById);
        customerByIdOptional.ifPresentOrElse(
                customer -> {
                    Order order = new Order(customerById);
                    orderService.createOrder(order);
                    printDishes();
                    System.out.println("Please input dish id");
                    int dishId = Integer.parseInt(scanner.nextLine());
                    Dish dishById = dishService.getDishById(dishId);
                    Optional<Dish> dishByIdOptional = Optional.ofNullable(dishById);
                    dishByIdOptional.ifPresentOrElse(
                            dish -> {
                                System.out.println("Please input order quantity");
                                try {
                                    int quantity = Integer.parseInt(scanner.nextLine());
                                    dish.setPrice(quantity);
                                    OrderItem orderItem = new OrderItem(order, dish, quantity);
                                    orderItemService.createOrderItem(orderItem);
                                    order.setTotalPrice(orderItem.getPrice() * orderItem.getQuantity());
                                    orderService.updateOrder(order);
                                    System.out.println("Order added successfully!");
                                }catch ( NumberFormatException e){
                                    System.err.println(e.getMessage());
                                }
                            },
                            () -> System.err.println("Wrong dish's id")
                    );
                },
                () -> System.err.println("Wrong customer's id")
        );
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
        System.out.println("Please input dishes id which you would like to update");
        int id = Integer.parseInt(scanner.nextLine());
        Dish dishById = dishService.getDishById(id);
        Optional<Dish> dishByIdOptional = Optional.ofNullable(dishById);
        dishByIdOptional.ifPresentOrElse(
                dish -> {
                    System.out.println(dishById);
                    System.out.println("Please input dish name, category, price  for update");
                    String dishStr = scanner.nextLine();
                    String[] dishArr = dishStr.split(",");
                    dishById.setName(dishArr[0]);
                    String category = dishArr[1].toUpperCase();
                    switch (category) {
                        case "APPETIZER" :
                        case "MAIN":
                        case "DESSERT":
                        case "DRINK":
                            dishById.setCategory(Category.valueOf(category));
                            break;
                        default: System.err.println("Wrong category");
                    }
                    try {
                        dish.setPrice(Double.parseDouble(dishArr[2]));
                        dishService.changeDish(dishById);
                        System.out.println("Dish updated successfully!");
                        printDishes();
                    }catch ( NumberFormatException e){
                        System.err.println(e.getMessage());
                    }
                },
                () -> System.err.println("Wrong dish's id")
        );
    }

    private static void deleteDish() {
        System.out.println("Please input dishes' id that you would like to delete");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Dish> dishById = Optional.ofNullable(dishService.getDishById(id));
        dishById.ifPresentOrElse(
                dish -> {
                    dishService.deleteDish(id);
                    System.out.println("Dish deleted successfully!");
                },
                () -> System.err.println("Wrong dish's id")
        );
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
        String dishStr = scanner.nextLine();
        Optional<Dish> dishOptional = Optional.ofNullable(getDish(dishStr));
        dishOptional.ifPresentOrElse(
                dish -> {
                    dishService.addDish(getDish(dishStr));
                    System.out.println("Dish added successfully!");
                },()->System.err.println("Dish is invalid!")
        );
    }

    private static Dish getDish(String dishStr) {
        String[] dishArr = dishStr.split(",");
        Dish dish = new Dish();
        dish.setName(dishArr[0]);
        String category = dishArr[1].toUpperCase();
        boolean isCategory = false;
        switch (category) {
            case "APPETIZER" :
            case "MAIN":
            case "DESSERT":
            case "DRINK":
                dish.setCategory(Category.valueOf(category));
                isCategory = true;
                break;
            default: System.err.println("Wrong category");
        }
        if(isCategory){
            try {
                dish.setPrice(Double.parseDouble(dishArr[2]));
                return dish;
            }catch ( NumberFormatException e){
                System.err.println(e.getMessage());
            }
        }
        return null;
    };

    private static void printCategory() {
        Category[] categories = Category.values();
        System.out.print("Category : ");
        for (Category category : categories) {
            System.out.print(category + " ");
        }
        System.out.println();
    }
}
