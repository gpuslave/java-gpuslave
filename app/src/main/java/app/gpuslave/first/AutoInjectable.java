package app.gpuslave.first;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Аннотация, используемая для пометки полей, в которые должны быть внедрены
 * зависимости с помощью механизма рефлексии.
 * Поля, помеченные данной аннотацией, будут автоматически инициализированы
 * классом {@link Injector}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoInjectable {
}

/**
 * Класс, реализующий механизм внедрения зависимостей через рефлексию.
 * Находит поля, помеченные аннотацией {@link AutoInjectable}, и инициализирует их
 * экземплярами классов, указанных в файле свойств.
 * <p>
 * Файл свойств должен содержать пары "интерфейс = реализация" в формате:
 * <pre>
 * пакет.ИмяИнтерфейса=пакет.ИмяРеализации
 * </pre>
 */
class Injector {

  /**
   * Файл свойств, содержащий сопоставление интерфейсов и их реализаций.
   */
  private final Properties properties;

  /**
   * Создает инжектор, используя путь к файлу свойств по умолчанию "injection.properties".
   * 
   * @throws IOException при ошибке чтения файла свойств
   */
  public Injector() throws IOException {
    this("injection.properties");
  }

  /**
   * Создает инжектор, используя указанный путь к файлу свойств.
   * 
   * @param propertiesPath путь к файлу свойств
   * @throws IOException при ошибке чтения файла свойств
   */
  public Injector(String propertiesPath) throws IOException {
    this.properties = new Properties();
    try (InputStream is = new FileInputStream(propertiesPath)) {
      properties.load(is);
    }
  }

  /**
   * Внедряет зависимости в переданный объект.
   * Метод ищет все поля, помеченные аннотацией {@link AutoInjectable},
   * и инициализирует их экземплярами классов, указанных в файле свойств.
   * 
   * @param <T>    тип объекта
   * @param object объект, в который внедряются зависимости
   * @return объект с внедренными зависимостями
   * @throws Exception при ошибке внедрения:
   *                   - если не найдена реализация для интерфейса
   *                   - если возникла ошибка создания экземпляра класса
   *                   - если возникла ошибка доступа к полю
   */
  public <T> T inject(T object) throws Exception {
    Class<?> objectClass = object.getClass();

    for (Field field : objectClass.getDeclaredFields()) {
      if (field.isAnnotationPresent(AutoInjectable.class)) {
        field.setAccessible(true);

        Class<?> fieldType = field.getType();
        String interfaceName = fieldType.getName();

        String implementationClassName = properties.getProperty(interfaceName);
        if (implementationClassName == null) {
          throw new IllegalStateException("No implementation found for " + interfaceName);
        }

        Class<?> implementationClass = Class.forName(implementationClassName);
        Object implementationInstance = implementationClass.getDeclaredConstructor().newInstance();

        field.set(object, implementationInstance);
      }
    }

    return object;
  }
}
