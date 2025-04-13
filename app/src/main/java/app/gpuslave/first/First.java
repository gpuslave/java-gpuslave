package app.gpuslave.first;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Класс {@code First} - это основной класс приложения, демонстрирующий работу
 * с классами из всех лабораторных работ:
 * <ul>
 * <li>Первая лабораторная: использование класса {@link Container} для хранения
 * элементов</li>
 * <li>Вторая лабораторная: использование класса {@link ExpressionEvaluator} для
 * вычисления математических выражений</li>
 * <li>Третья лабораторная: использование класса {@link Compare} для сравнения
 * производительности коллекций</li>
 * </ul>
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
   * Последовательно демонстрирует работу всех трех лабораторных работ:
   * <ul>
   * <li>Первая лабораторная: Класс {@link Container} для хранения элементов</li>
   * <li>Вторая лабораторная: Класс {@link ExpressionEvaluator} для вычисления
   * математических выражений</li>
   * <li>Третья лабораторная: Класс {@link Compare} для сравнения
   * производительности коллекций</li>
   * </ul>
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

    System.out.println(eval.evaluate("(1+a+C) + (DOUBLE)"));
    System.out.println(eval.evaluate("1+(2)*(300)"));

    System.out.println("----------THIRD--LAB------------");
    Compare cmpr = new Compare();
    System.out.println("Сравнение производительности ArrayList и LinkedList");
    System.out.println("Количество операций для каждого метода: " + Compare.ITERATIONS);
    System.out.println("Размер коллекций: " + Compare.LIST_SIZE);
    System.out.println("\n");

    ArrayList<Integer> arrayList = new ArrayList<>();
    LinkedList<Integer> linkedList = new LinkedList<>();

    for (int i = 0; i < Compare.LIST_SIZE; i++) {
      arrayList.add(i);
      linkedList.add(i);
    }

    cmpr.runTests(arrayList, linkedList);
  }
}