package app.gpuslave.first;

public class First {

  public String Hello() {
    String str = "Hello, world!";
    return str;
  }

  public static void main(String[] args) {
    First firstInstance = new First();
    System.out.println(firstInstance.Hello());

    Container<Integer> arr = new Container<>();
    arr.add(1);
    arr.print();
  }
}