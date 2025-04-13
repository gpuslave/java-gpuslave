package app.gpuslave.first;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Тестовый класс для проверки механизма внедрения зависимостей
 * с использованием аннотации {@link AutoInjectable} и класса {@link Injector}.
 * Проверяет корректность внедрения зависимостей с различными реализациями
 * и обработку ошибок при отсутствии реализаций.
 */
public class ReflectionTest {

  /**
   * Путь к тестовому файлу свойств, используемому для проверки внедрения зависимостей.
   */
  private String propertiesPath;

  /**
   * Тестовый интерфейс A для демонстрации внедрения зависимостей.
   * Имеет две реализации для проверки переключения между ними.
   */
  public interface InterfaceA {
    /**
     * Тестовый метод, возвращающий строку, идентифицирующую реализацию.
     * 
     * @return строка-идентификатор реализации
     */
    String doSomething();
  }

  /**
   * Тестовый интерфейс B для демонстрации внедрения множественных зависимостей.
   * Имеет одну реализацию для проверки корректности внедрения.
   */
  public interface InterfaceB {
    /**
     * Тестовый метод, возвращающий строку, идентифицирующую реализацию.
     * 
     * @return строка-идентификатор реализации
     */
    String doSomethingElse();
  }

  /**
   * Первая реализация интерфейса A.
   * Используется в тесте стандартного внедрения.
   */
  public static class ImplementationA1 implements InterfaceA {
    @Override
    public String doSomething() {
      return "Implementation A1";
    }
  }

  /**
   * Альтернативная реализация интерфейса A.
   * Используется в тесте альтернативного внедрения для проверки
   * возможности изменения реализаций через конфигурационный файл.
   */
  public static class ImplementationA2 implements InterfaceA {
    @Override
    public String doSomething() {
      return "Implementation A2";
    }
  }

  /**
   * Реализация интерфейса B.
   * Используется во всех тестах внедрения.
   */
  public static class ImplementationB implements InterfaceB {
    @Override
    public String doSomethingElse() {
      return "Implementation B";
    }
  }

  /**
   * Тестовый бин с зависимостями, которые будут внедрены.
   * Содержит два поля, помеченных аннотацией {@link AutoInjectable},
   * для которых будут внедрены соответствующие реализации.
   */
  public static class TestBean {
    /**
     * Поле типа InterfaceA, для которого будет внедрена реализация.
     */
    @AutoInjectable
    private InterfaceA fieldA;

    /**
     * Поле типа InterfaceB, для которого будет внедрена реализация.
     */
    @AutoInjectable
    private InterfaceB fieldB;

    /**
     * Возвращает внедренную реализацию интерфейса A.
     * 
     * @return внедренная реализация InterfaceA
     */
    public InterfaceA getFieldA() {
      return fieldA;
    }

    /**
     * Возвращает внедренную реализацию интерфейса B.
     * 
     * @return внедренная реализация InterfaceB
     */
    public InterfaceB getFieldB() {
      return fieldB;
    }
  }

  /**
   * Настройка тестового окружения перед выполнением каждого теста.
   * Создает временный файл свойств с конфигурацией внедрения зависимостей.
   * 
   * @throws IOException при ошибке создания или записи в файл свойств
   */
  @Before
  public void setUp() throws IOException {
    propertiesPath = "test-injection.properties";

    Properties props = new Properties();
    props.setProperty("app.gpuslave.first.ReflectionTest$InterfaceA",
        "app.gpuslave.first.ReflectionTest$ImplementationA1");
    props.setProperty("app.gpuslave.first.ReflectionTest$InterfaceB",
        "app.gpuslave.first.ReflectionTest$ImplementationB");

    try (FileOutputStream out = new FileOutputStream(propertiesPath)) {
      props.store(out, "Test configuration for dependency injection");
    }
  }

  /**
   * Проверяет корректность внедрения зависимостей с использованием
   * стандартной реализации (A1).
   * 
   * @throws Exception при ошибке внедрения зависимостей
   */
  @Test
  public void testStandardInjection() throws Exception {
    // Создаем инжектор и тестовый бин
    Injector injector = new Injector(propertiesPath);
    TestBean testBean = new TestBean();

    // Внедряем зависимости
    testBean = injector.inject(testBean);

    // Проверяем, что зависимости внедрены
    assertNotNull("Поле A не должно быть null после внедрения", testBean.getFieldA());
    assertNotNull("Поле B не должно быть null после внедрения", testBean.getFieldB());

    // Проверяем, что внедрены правильные реализации
    assertEquals("Implementation A1", testBean.getFieldA().doSomething());
    assertEquals("Implementation B", testBean.getFieldB().doSomethingElse());
  }

  /**
   * Проверяет корректность внедрения зависимостей с использованием
   * альтернативной реализации (A2).
   * Демонстрирует возможность изменения реализаций через файл свойств.
   * 
   * @throws Exception при ошибке внедрения зависимостей
   */
  @Test
  public void testAlternativeInjection() throws Exception {
    // Изменяем файл свойств, чтобы использовать альтернативную реализацию
    Properties props = new Properties();
    props.setProperty("app.gpuslave.first.ReflectionTest$InterfaceA",
        "app.gpuslave.first.ReflectionTest$ImplementationA2");
    props.setProperty("app.gpuslave.first.ReflectionTest$InterfaceB",
        "app.gpuslave.first.ReflectionTest$ImplementationB");

    try (FileOutputStream out = new FileOutputStream(propertiesPath)) {
      props.store(out, "Test configuration with alternative implementation");
    }

    // Создаем инжектор и тестовый бин
    Injector injector = new Injector(propertiesPath);
    TestBean testBean = new TestBean();

    // Внедряем зависимости
    testBean = injector.inject(testBean);

    // Проверяем, что зависимости внедрены
    assertNotNull("Поле A не должно быть null после внедрения", testBean.getFieldA());
    assertNotNull("Поле B не должно быть null после внедрения", testBean.getFieldB());

    // Проверяем, что внедрена альтернативная реализация
    assertEquals("Implementation A2", testBean.getFieldA().doSomething());
    assertEquals("Implementation B", testBean.getFieldB().doSomethingElse());
  }

  /**
   * Проверяет, что при отсутствии реализации в файле свойств
   * генерируется соответствующее исключение.
   * 
   * @throws Exception при ошибке внедрения зависимостей (ожидается)
   */
  @Test(expected = IllegalStateException.class)
  public void testMissingImplementation() throws Exception {
    // Создаем файл свойств без одной из реализаций
    Properties props = new Properties();
    props.setProperty("app.gpuslave.first.ReflectionTest$InterfaceB",
        "app.gpuslave.first.ReflectionTest$ImplementationB");

    try (FileOutputStream out = new FileOutputStream(propertiesPath)) {
      props.store(out, "Test configuration with missing implementation");
    }

    // Создаем инжектор и тестовый бин
    Injector injector = new Injector(propertiesPath);
    TestBean testBean = new TestBean();

    // Должно выбросить исключение IllegalStateException, так как реализация InterfaceA не определена
    injector.inject(testBean);
  }
}