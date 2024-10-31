package collection.list.test.ex2_old;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductOrderTestV2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<ProductOrder> orders = new ArrayList<>();

        while (true) {
            System.out.println((orders.size() + 1) + "번째 주문 정보를 입력하세요. 완료시 'x' 입력");

            System.out.print("상품명: ");
            String productName = scanner.nextLine();
            if (productName.equals("x")) {
                break;
            }

            System.out.print("가격: ");
            int price = scanner.nextInt();

            System.out.print("수량: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼를 비우기 위한 코드

            orders.add(createOrder(productName, price, quantity));
        }

        printOrders(orders);
        int totalAmount = getTotalAmount(orders);
        System.out.println("총 결제 금액: " + totalAmount);
    }

    static ProductOrder createOrder(String productName, int price, int quantity) {
        ProductOrder order1 = new ProductOrder();
        order1.productName = productName;
        order1.price = price;
        order1.quantity = quantity;
        return order1;
    }

    static void printOrders(List<ProductOrder> orders) {
        for (ProductOrder order : orders) {
            System.out.println("상품명: " + order.productName + ", 가격: " + order.price + ", 수량: " + order.quantity);
        }
    }

    static int getTotalAmount(List<ProductOrder> orders) {
        int totalAmount = 0;
        for (ProductOrder order : orders) {
            totalAmount += order.price * order.quantity;
        }
        return totalAmount;
    }
}
