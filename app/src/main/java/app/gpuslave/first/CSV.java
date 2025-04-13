package app.gpuslave.first;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для работы с CSV файлами, содержащими данные о сотрудниках.
 * Предоставляет методы для чтения CSV файлов и создания объектов Person с
 * ассоциированными объектами Division.
 */
public class CSV {

  /**
   * Конструктор по умолчанию. Не используется напрямую, так как класс 
   * предоставляет только статические методы.
   */
  public CSV() {
    // Конструктор не используется
  }

  /**
   * Класс, представляющий отдел, в котором работает сотрудник.
   */
  public static class Division {
    /** Счетчик для автоматической генерации идентификаторов отделов. */
    private static int nextId = 1;
    
    /** Уникальный идентификатор отдела. */
    private final int id;
    
    /** Название отдела. */
    private final String title;

    /**
     * Создает новый отдел с указанным названием.
     * ID генерируется автоматически.
     * 
     * @param title Название отдела
     */
    public Division(String title) {
      this.id = nextId++;
      this.title = title;
    }

    /**
     * Возвращает ID отдела.
     * 
     * @return ID отдела
     */
    public int getId() {
      return id;
    }

    /**
     * Возвращает название отдела.
     * 
     * @return Название отдела
     */
    public String getTitle() {
      return title;
    }
  }

  /**
   * Класс, представляющий сотрудника.
   */
  public static class Person {
    /** Уникальный идентификатор сотрудника. */
    private final int id;
    
    /** Имя сотрудника. */
    private final String name;
    
    /** Пол сотрудника ('M' - мужской, 'F' - женский). */
    private final char gender;
    
    /** Дата рождения сотрудника. */
    private final Date dateOfBirth;
    
    /** Отдел, в котором работает сотрудник. */
    private final Division division;
    
    /** Зарплата сотрудника. */
    private final int salary;

    /**
     * Создает нового сотрудника с указанными параметрами.
     * 
     * @param id          Идентификатор сотрудника
     * @param name        Имя сотрудника
     * @param gender      Пол сотрудника ('M' или 'F')
     * @param dateOfBirth Дата рождения сотрудника
     * @param division    Отдел, в котором работает сотрудник
     * @param salary      Зарплата сотрудника
     */
    public Person(int id, String name, char gender, Date dateOfBirth, Division division, int salary) {
      this.id = id;
      this.name = name;
      this.gender = gender;
      this.dateOfBirth = dateOfBirth;
      this.division = division;
      this.salary = salary;
    }

    /**
     * Возвращает идентификатор сотрудника.
     * 
     * @return Идентификатор сотрудника
     */
    public int getId() {
      return id;
    }

    /**
     * Возвращает имя сотрудника.
     * 
     * @return Имя сотрудника
     */
    public String getName() {
      return name;
    }

    /**
     * Возвращает пол сотрудника.
     * 
     * @return Пол сотрудника ('M' или 'F')
     */
    public char getGender() {
      return gender;
    }

    /**
     * Возвращает дату рождения сотрудника.
     * 
     * @return Дата рождения сотрудника
     */
    public Date getDateOfBirth() {
      return dateOfBirth;
    }

    /**
     * Возвращает отдел, в котором работает сотрудник.
     * 
     * @return Отдел сотрудника
     */
    public Division getDivision() {
      return division;
    }

    /**
     * Возвращает зарплату сотрудника.
     * 
     * @return Зарплата сотрудника
     */
    public int getSalary() {
      return salary;
    }
  }

  /**
   * Читает данные из CSV файла по указанному пути и создает список объектов
   * Person.
   * 
   * @param csvFilePath Путь к CSV файлу
   * @param separator   Разделитель полей в CSV файле
   * @return Список объектов Person
   */
  public static List<Person> parseCSV(String csvFilePath, char separator) {
    List<Person> people = new ArrayList<>();
    Map<String, Division> divisionCache = new HashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();

    try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath))
        .withCSVParser(parser)
        .build()) {
      String[] header = reader.readNext();
      if (header == null) {
        throw new RuntimeException("CSV файл пуст или имеет недопустимый формат");
      }

      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        if (nextLine.length < 6) {
          System.err.println("Предупреждение: Пропуск некорректной строки с менее чем 6 полями");
          continue;
        }

        try {
          int id = Integer.parseInt(nextLine[0]);
          String name = nextLine[1];
          char gender = nextLine[2].equalsIgnoreCase("Male") ? 'M' : 'F';

          Date dateOfBirth;
          try {
            dateOfBirth = dateFormat.parse(nextLine[3]);
          } catch (ParseException e) {
            System.err.println("Предупреждение: Неверный формат даты для ID " + id + ": " + nextLine[3]);
            dateOfBirth = new Date(0);
          }

          String divisionTitle = nextLine[4];
          Division division = divisionCache.computeIfAbsent(divisionTitle, Division::new);
          int salary = Integer.parseInt(nextLine[5]);

          Person person = new Person(id, name, gender, dateOfBirth, division, salary);
          people.add(person);

        } catch (NumberFormatException e) {
          System.err.println("Предупреждение: Не удалось преобразовать числовое значение: " + e.getMessage());
        }
      }
    } catch (IOException | CsvValidationException e) {
      throw new RuntimeException("Ошибка при чтении CSV файла: " + e.getMessage(), e);
    }

    return people;
  }

  /**
   * Читает данные из CSV файла из папки ресурсов и создает список объектов
   * Person.
   * 
   * @param csvFileName Имя CSV файла в папке ресурсов
   * @param separator   Разделитель полей в CSV файле
   * @return Список объектов Person
   */
  public static List<Person> parseCSVFromResources(String csvFileName, char separator) {
    List<Person> people = new ArrayList<>();
    Map<String, Division> divisionCache = new HashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    try (InputStream is = CSV.class.getClassLoader().getResourceAsStream(csvFileName)) {

      if (is == null) {
        throw new RuntimeException("Ресурс не найден: " + csvFileName);
      }

      CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();

      try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is))
          .withCSVParser(parser)
          .build()) {

        String[] header = reader.readNext();
        if (header == null) {
          throw new RuntimeException("CSV файл пуст или имеет недопустимый формат");
        }

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
          if (nextLine.length < 6) {
            System.err.println("Предупреждение: Пропуск некорректной строки с менее чем 6 полями");
            continue;
          }

          try {
            int id = Integer.parseInt(nextLine[0]);
            String name = nextLine[1];
            char gender = nextLine[2].equalsIgnoreCase("Male") ? 'M' : 'F';

            Date dateOfBirth;
            try {
              dateOfBirth = dateFormat.parse(nextLine[3]);
            } catch (ParseException e) {
              System.err.println("Предупреждение: Неверный формат даты для ID " + id + ": " + nextLine[3]);
              dateOfBirth = new Date(0);
            }

            String divisionTitle = nextLine[4];
            Division division = divisionCache.computeIfAbsent(divisionTitle, Division::new);
            int salary = Integer.parseInt(nextLine[5]);

            Person person = new Person(id, name, gender, dateOfBirth, division, salary);
            people.add(person);

          } catch (NumberFormatException e) {
            System.err.println("Предупреждение: Не удалось преобразовать числовое значение: " + e.getMessage());
          }
        }
      }
    } catch (IOException | CsvValidationException e) {
      throw new RuntimeException("Ошибка при чтении CSV файла: " + e.getMessage(), e);
    }

    return people;
  }
}
