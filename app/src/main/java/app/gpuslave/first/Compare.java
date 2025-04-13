package app.gpuslave.first;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Класс {@code Compare} предназначен для сравнения производительности
 * ArrayList и LinkedList при выполнении различных операций.
 * Тестируются различные методы коллекций с замером времени их выполнения.
 */
public class Compare {
  public static final int ITERATIONS = 5000;
  public static final int LIST_SIZE = 10000;
  private static final Random random = new Random();

  /**
   * Возвращает количество итераций, используемое для тестирования
   * производительности.
   *
   * @return количество итераций для тестов
   */
  public int getIter() {
    return ITERATIONS;
  }

  /**
   * Возвращает размер коллекций, используемый для тестирования
   * производительности.
   *
   * @return размер тестируемых коллекций
   */
  public int getListSize() {
    return LIST_SIZE;
  }

  /**
   * Запуск всех тестов производительности для обеих коллекций
   * и вывод результатов.
   * 
   * @param arrayList  коллекция ArrayList для тестирования
   * @param linkedList коллекция LinkedList для тестирования
   */
  public void runTests(ArrayList<Integer> arrayList, LinkedList<Integer> linkedList) {
    System.out.println("+----------------------------+---------------+---------------+");
    System.out.println("| Операция                   | ArrayList (мс) | LinkedList (мс) |");
    System.out.println("+----------------------------+---------------+---------------+");

    long arrayListAddTime = testAddToEnd(arrayList);
    long linkedListAddTime = testAddToEnd(linkedList);
    printResult("Добавление в конец", arrayListAddTime, linkedListAddTime);

    arrayListAddTime = testAddToBeginning(arrayList);
    linkedListAddTime = testAddToBeginning(linkedList);
    printResult("Добавление в начало", arrayListAddTime, linkedListAddTime);

    arrayListAddTime = testAddToMiddle(arrayList);
    linkedListAddTime = testAddToMiddle(linkedList);
    printResult("Добавление в середину", arrayListAddTime, linkedListAddTime);

    long arrayListGetTime = testGet(arrayList);
    long linkedListGetTime = testGet(linkedList);
    printResult("Получение по индексу", arrayListGetTime, linkedListGetTime);

    long arrayListContainsTime = testContains(arrayList);
    long linkedListContainsTime = testContains(linkedList);
    printResult("Поиск элемента", arrayListContainsTime, linkedListContainsTime);

    long arrayListRemoveEndTime = testRemoveFromEnd(arrayList);
    long linkedListRemoveEndTime = testRemoveFromEnd(linkedList);
    printResult("Удаление с конца", arrayListRemoveEndTime, linkedListRemoveEndTime);

    long arrayListRemoveBeginningTime = testRemoveFromBeginning(arrayList);
    long linkedListRemoveBeginningTime = testRemoveFromBeginning(linkedList);
    printResult("Удаление с начала", arrayListRemoveBeginningTime, linkedListRemoveBeginningTime);

    long arrayListRemoveMiddleTime = testRemoveFromMiddle(arrayList);
    long linkedListRemoveMiddleTime = testRemoveFromMiddle(linkedList);
    printResult("Удаление из середины", arrayListRemoveMiddleTime, linkedListRemoveMiddleTime);

    System.out.println("+----------------------------+---------------+---------------+");
  }

  /**
   * Тестирует производительность операции добавления элемента в конец коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testAddToEnd(List<Integer> list) {
    List<Integer> testList = new ArrayList<>(list);

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      testList.add(random.nextInt(1000));
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции добавления элемента в начало коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testAddToBeginning(List<Integer> list) {
    List<Integer> testList = new ArrayList<>(list);

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      testList.add(0, random.nextInt(1000));
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции добавления элемента в середину
   * коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testAddToMiddle(List<Integer> list) {
    List<Integer> testList = new ArrayList<>(list);
    int middle = testList.size() / 2;

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      testList.add(middle, random.nextInt(1000));
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции получения элемента по индексу.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testGet(List<Integer> list) {
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      int index = random.nextInt(list.size());
      list.get(index);
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции поиска элемента в коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testContains(List<Integer> list) {
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      list.contains(random.nextInt(LIST_SIZE * 2));
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции удаления элемента с конца коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testRemoveFromEnd(List<Integer> list) {
    List<Integer> testList = new ArrayList<>(list);

    for (int i = 0; i < ITERATIONS; i++) {
      testList.add(random.nextInt(1000));
    }

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      testList.remove(testList.size() - 1);
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции удаления элемента с начала коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testRemoveFromBeginning(List<Integer> list) {
    List<Integer> testList = new ArrayList<>(list);

    for (int i = 0; i < ITERATIONS; i++) {
      testList.add(0, random.nextInt(1000));
    }

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      testList.remove(0);
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Тестирует производительность операции удаления элемента из середины
   * коллекции.
   * 
   * @param list коллекция для тестирования
   * @return время выполнения операций в миллисекундах
   */
  private static long testRemoveFromMiddle(List<Integer> list) {
    List<Integer> testList = new ArrayList<>(list);

    int middle = testList.size() / 2;
    for (int i = 0; i < ITERATIONS; i++) {
      testList.add(middle, random.nextInt(1000));
    }

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      testList.remove(middle);
    }
    return System.currentTimeMillis() - startTime;
  }

  /**
   * Выводит результаты сравнения производительности операций в формате таблицы.
   * 
   * @param operation      название операции
   * @param arrayListTime  время выполнения для ArrayList в миллисекундах
   * @param linkedListTime время выполнения для LinkedList в миллисекундах
   */
  private static void printResult(String operation, long arrayListTime, long linkedListTime) {
    System.out.printf("| %-26s | %13d | %13d |%n", operation, arrayListTime, linkedListTime);
  }
}