package app.gpuslave.first;

/**
 * Класс {@code First} - это основной класс приложения, демонстрирующий работу с
 * классом {@link Container}.
 */
public class First {

  /**
   * Возвращает строку "Hello, world!".
   *
   * @return Строка "Hello, world!".
   */
  public String Hello() {
    String str = "Hello, world!";
    return str;
  }

  /**
   * Основной метод приложения.
   * Выводит в консоль строку "Hello, world!" и демонстрирует использование класса
   * {@link Container}.
   *
   * @param args Аргументы командной строки (не используются).
   */
  public static void main(String[] args) {
    System.out.println("----------FIRST--LAB------------");
    First firstInstance = new First();
    System.out.println(firstInstance.Hello());

    Container<Integer> arr = new Container<>();
    arr.add(1);
    arr.print();

    System.out.println("----------SECOND-LAB------------");
    ExpressionEvaluator eval = new ExpressionEvaluator();
    eval.evaluate("1+a+C + DOUBLE");
  }
}