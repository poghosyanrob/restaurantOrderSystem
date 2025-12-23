package util;

public interface Commands {
    String EXIT = "0";
    String ADD_DISHES = "1";
    String DELETE_DISHES = "2";
    String UPDATE_DISHES = "3";
    String ADD_CUSTOMER = "4";
    String SHOW_CUSTOMER = "5";
    String ADD_ORDER = "6";
    String SHOW_ALL_ORDERS = "7";
    String SHOW_ORDERS_BY_CUSTOMER = "8";
    String PRINT_ORDER_ITEMS = "9";
    String UPDATE_ORDER_STATUS = "10";
    String SHOW_MENU = "11";


    static void printCommands() {
        System.out.println("Please input " + EXIT + " for EXIT");
        System.out.println("Please input " + ADD_DISHES + " for ADD_DISHES");
        System.out.println("Please input " + DELETE_DISHES + " for DELETE_DISHES");
        System.out.println("Please input " + UPDATE_DISHES + " for UPDATE_DISHES");
        System.out.println("Please input " + ADD_CUSTOMER + " for ADD_CUSTOMER");
        System.out.println("Please input " + SHOW_CUSTOMER + " for SHOW_CUSTOMER");
        System.out.println("Please input " + ADD_ORDER + " for ADD_ORDER");
        System.out.println("Please input " + SHOW_ALL_ORDERS + " for SHOW_ALL_ORDERS");
        System.out.println("Please input " + SHOW_ORDERS_BY_CUSTOMER + " for SHOW_ORDERS_BY_CUSTOMER");
        System.out.println("Please input " + PRINT_ORDER_ITEMS + " for SHOW_ORDER_DETAIL");
        System.out.println("Please input " + UPDATE_ORDER_STATUS + " for UPDATE_ORDER_STATUS");
        System.out.println("Please input " + SHOW_MENU + " for SHOW_MENU");
    }
}
