package app.gpuslave.first;

/**
 * Пример использования аннотации {@link AutoInjectable} и класса {@link Injector}
 * для внедрения зависимостей. Демонстрирует принцип работы внедрения зависимостей
 * с использованием рефлексии и конфигурационного файла.
 */
public class ReflectionExample {

  /**
   * Интерфейс для демонстрации внедрения зависимостей.
   * Предоставляет метод doSomething(), который будет реализован различными классами.
   */
  public interface SomeInterface {
    void doSomething();
  }

  /**
   * Другой интерфейс для демонстрации внедрения множественных зависимостей.
   * Предоставляет метод doSomeOther(), который будет реализован классом SODoer.
   */
  public interface SomeOtherInterface {
    void doSomeOther();
  }

  /**
   * Первая реализация интерфейса {@link SomeInterface}.
   * При вызове метода doSomething() выводит символ "A".
   */
  public static class SomeImpl implements SomeInterface {
    @Override
    public void doSomething() {
      System.out.println("A");
    }
  }

  /**
   * Вторая реализация интерфейса {@link SomeInterface}.
   * При вызове метода doSomething() выводит символ "B".
   */
  public static class OtherImpl implements SomeInterface {
    @Override
    public void doSomething() {
      System.out.println("B");
    }
  }

  /**
   * Реализация интерфейса {@link SomeOtherInterface}.
   * При вызове метода doSomeOther() выводит символ "C".
   */
  public static class SODoer implements SomeOtherInterface {
    @Override
    public void doSomeOther() {
      System.out.println("C");
    }
  }

  /**
   * Класс-бин с полями, помеченными аннотацией {@link AutoInjectable}.
   * Демонстрирует механизм автоматического внедрения зависимостей без
   * необходимости их явной инициализации в коде.
   */
  public static class SomeBean {
    /**
     * Поле, реализация которого будет внедрена автоматически.
     */
    @AutoInjectable
    private SomeInterface field1;

    /**
     * Поле, реализация которого будет внедрена автоматически.
     */
    @AutoInjectable
    private SomeOtherInterface field2;

    /**
     * Метод, использующий внедренные зависимости.
     * Вызывает соответствующие методы обоих зависимых интерфейсов.
     */
    public void foo() {
      field1.doSomething();
      field2.doSomeOther();
    }
  }

  /**
   * Демонстрация работы внедрения зависимостей с использованием класса {@link Injector}.
   * Создает экземпляр класса SomeBean, внедряет в него зависимости на основе файла
   * конфигурации и вызывает метод, использующий эти зависимости.
   * 
   * @param args аргументы командной строки (не используются)
   * @throws Exception при ошибке внедрения зависимостей
   */
  public static void main(String[] args) throws Exception {
    SomeBean sb = new Injector("injection.properties").inject(new SomeBean());
    sb.foo();
  }
}