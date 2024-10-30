package collection.link;

public class MyLinkedListV2Main {

    public static void main(String[] args) {
        MyLinkedListV2 list = new MyLinkedListV2();
        //마지막에 추가 //O(n)
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(list);

        //첫 번째 항목에 추가, 삭제
        System.out.println("첫 번째 항목에 추가");
        list.add(0,"d"); //O(1)
        System.out.println(list);

        System.out.println("첫 번째 항목 삭제");
        list.remove(0); //remove First O(1)
        System.out.println(list);

        //중간에 항목에 추가, 삭제
        System.out.println("중간 항목에 추가");
        list.add(1,"e"); //O(1)
        System.out.println(list);

        System.out.println("중간 항목 삭제");
        list.remove(1);//remove Last O(n)
        System.out.println(list);
    }
}