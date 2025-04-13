package app.gpuslave.first;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * <li>Четвертая лабораторная: использование класса {@link CSV} для чтения и
 * обработки
 * данных из CSV файла</li>
 * </ul>
 */
public class First {

  /**
   * Конструктор по умолчанию для класса First.
   * Создает новый экземпляр класса без инициализации полей.
   */
  public First() {
    // Пустой конструктор
  }

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
   * Последовательно демонстрирует работу всех четырех лабораторных работ:
   * <ul>
   * <li>Первая лабораторная: Класс {@link Container} для хранения элементов</li>
   * <li>Вторая лабораторная: Класс {@link ExpressionEvaluator} для вычисления
   * математических выражений</li>
   * <li>Третья лабораторная: Класс {@link Compare} для сравнения
   * производительности коллекций</li>
   * <li>Четвертая лабораторная: Класс {@link CSV} для чтения и обработки
   * данных из CSV файла</li>
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

    System.out.println("----------FOURTH-LAB------------");
    System.out.println("Чтение и обработка данных из CSV файла");

    String csvFilePath = "/home/gpuslave/pet/java-gpuslave/foreign_names.csv";
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ';');
    System.out.println("Загружено " + people.size() + " записей из CSV файла");

    System.out.println("Примеры записей:");
    for (int i = 0; i < Math.min(5, people.size()); i++) {
      CSV.Person person = people.get(i);
      System.out.println((i + 1) + ". " + person.getName() +
          " (" + person.getGender() + "), " +
          "отдел: " + person.getDivision().getTitle() +
          ", зарплата: " + person.getSalary());
    }

    System.out.println("\nПример группировки по отделам:");
    java.util.Map<String, Integer> divisionCounts = new java.util.HashMap<>();

    for (CSV.Person person : people) {
      String division = person.getDivision().getTitle();
      divisionCounts.put(division, divisionCounts.getOrDefault(division, 0) + 1);
    }

    int count = 0;
    for (java.util.Map.Entry<String, Integer> entry : divisionCounts.entrySet()) {
      System.out.println("Отдел " + entry.getKey() + ": " + entry.getValue() + " сотрудников");
      count++;
      if (count >= 5)
        break;
    }
  }
}