package collection.deque.test.stack;

public class BrowserHistoryTest {

    public static void main(String[] args) {
        BrowserHistory browser = new BrowserHistory();
        
        // 사용자가 웹페이지를 방문하는 시나리오
        browser.visitPage("youtube.com");
        browser.visitPage("google.com");
        browser.visitPage("facebook.com");

        // 뒤로 가기 기능을 사용하는 시나리오
        String currentPage1 = browser.goBack();
        System.out.println("currentPage1 = " + currentPage1);

        String currentPage2 = browser.goBack();
        System.out.println("currentPage2 = " + currentPage2);
    }
}