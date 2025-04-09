package app.gpuslave.first;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class ExpressionEvaluator {

  /**
   * Хранилище переменных и их значений.
   * Ключ - имя переменной, значение - числовое значение переменной.
   */
  private Map<String, Double> variables;

  /**
   * Сканер для чтения пользовательского ввода при запросе значений переменных.
   */
  private Scanner scanner;

  /**
   * Создает новый экземпляр вычислителя выражений.
   * Инициализирует хранилище переменных и сканер для пользовательского ввода.
   */
  public ExpressionEvaluator() {
    variables = new HashMap<>();
    scanner = new Scanner(System.in);
  }

  /**
   * Освобождает ресурсы, используемые вычислителем.
   * Закрывает сканер пользовательского ввода.
   */
  public void close() {
    if (scanner != null) {
      scanner.close();
    }
  }

  /**
   * Вычисляет значение математического выражения.
   * <p>
   * Процесс вычисления включает:
   * <ol>
   * <li>Удаление всех пробелов из выражения</li>
   * <li>Поиск переменных и запрос их значений у пользователя</li>
   * <li>Подстановка значений переменных в выражение</li>
   * <li>Проверка корректности расстановки скобок</li>
   * <li>Преобразование в постфиксную запись и вычисление результата</li>
   * </ol>
   *
   * @param expression строковое представление математического выражения
   * @return результат вычисления выражения
   * @throws IllegalArgumentException если выражение содержит синтаксические
   *                                  ошибки или
   *                                  если произошла ошибка при вычислении
   *                                  (например, деление на ноль)
   */
  public double evaluate(String expression) {
    if (!variables.isEmpty()) {
      variables = new HashMap<>();
      scanner = new Scanner(System.in);
    }

    try {
      expression = expression.replaceAll("\\s+", ""); // remove spaces
      System.out.println(expression);

      collectVariables(expression);

      for (String key : variables.keySet()) {
        System.out.println(key);
      }

      if (!variables.isEmpty()) {
        promptForVariables();

        // debug
        for (Map.Entry<String, Double> entry : variables.entrySet()) {
          System.out.println(String.format("key: %s, value: %s",
              entry.getKey(),
              entry.getValue()));
        }

        expression = replaceVariables(expression);
        System.out.println(expression);
      }

      // System.exit(0); // TEST -----------------------

      if (!checkBrackets(expression)) {
        throw new IllegalArgumentException("Некорректное выражение," +
            " ошибка в расстановке скобок");
      }

      return evaluateExpression(expression);

    } catch (Exception e) {
      System.err.println("Ошибка: " + e.getMessage());
      throw new IllegalArgumentException("Некорректное выражение");
    }
  }

  /**
   * Находит и собирает все переменные из выражения.
   * <p>
   * Метод идентифицирует все последовательности букв в выражении как переменные
   * и добавляет их в хранилище переменных со значением null.
   *
   * @param expression строковое представление выражения
   */
  private void collectVariables(String expression) {
    StringBuilder varName = new StringBuilder();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);

      if (Character.isLetter(c)) {
        varName.append(c);
      } else if (varName.length() > 0) {
        variables.put(varName.toString(), null);
        varName = new StringBuilder();
      }
    }

    // edge case when varaible is at the end
    if (varName.length() > 0) {
      variables.put(varName.toString(), null);
    }
  }

  /**
   * Запрашивает у пользователя значения для всех переменных.
   * <p>
   * Для каждой найденной переменной метод выводит приглашение к вводу
   * и сохраняет введенное значение в хранилище переменных.
   *
   * @throws IllegalArgumentException если введенное значение не может быть
   *                                  преобразовано в число
   */
  private void promptForVariables() {
    for (String var : variables.keySet()) {
      System.out.print("Введите значение для " + var + ": ");
      try {
        double value = scanner.nextDouble();
        variables.put(var, value);
      } catch (Exception e) {
        throw new IllegalArgumentException("Некорректное значения для переменной " +
            var);
      }
    }
  }

  /**
   * Заменяет все переменные в выражении их числовыми значениями.
   *
   * @param expression исходное выражение с переменными
   * @return выражение с подставленными значениями переменных
   */
  private String replaceVariables(String expression) {
    for (Map.Entry<String, Double> entry : variables.entrySet()) {
      expression = expression.replaceAll(entry.getKey(), entry.getValue().toString());
    }
    return expression;
  }

  /**
   * Проверяет корректность расстановки скобок в выражении.
   * <p>
   * Метод использует стек для отслеживания открывающих и закрывающих скобок
   * и проверяет их правильное соответствие.
   *
   * @param expression строковое представление выражения
   * @return true если все скобки правильно расставлены и сбалансированы, false в
   *         противном случае
   */
  private boolean checkBrackets(String expression) {
    Stack<Character> stack = new Stack<>();

    for (char c : expression.toCharArray()) {
      if (c == '(') {
        stack.push(c);
      } else if (c == ')') {
        if (stack.isEmpty() || stack.pop() != '(') {
          return false;
        }
      }
    }

    return stack.isEmpty();
  }

  // old version
  // private double evaluateExpression(String expression) {
  // List<String> tokens = tokenize(expression);
  // return calculate(tokens);
  // }

  // old version
  // private List<String> tokenize(String expression) {
  // List<String> tokens = new ArrayList<>();
  // StringBuilder numberBuilder = new StringBuilder();
  // boolean lastWasOperator = true;

  // for (int i = 0; i < expression.length(); i++) {
  // char c = expression.charAt(i);

  // if (Character.isDigit(c) || c == '.') {

  // numberBuilder.append(c);
  // lastWasOperator = false;

  // } else if (c == '(' || c == ')') {

  // if (numberBuilder.length() > 0) {
  // tokens.add(numberBuilder.toString());
  // numberBuilder = new StringBuilder();
  // }
  // tokens.add(String.valueOf(c));
  // // lastWasOperator = c == '(';

  // } else if (isOperator(c)) {

  // if (numberBuilder.length() > 0) {
  // tokens.add(numberBuilder.toString());
  // numberBuilder = new StringBuilder();
  // }

  // if

  // }
  // }
  // }

  /**
   * Проверяет, является ли символ арифметическим оператором.
   *
   * @param c символ для проверки
   * @return true если символ является оператором (+, -, *, /), false в противном
   *         случае
   */
  private boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/';
  }

  /**
   * Вычисляет значение выражения, используя алгоритм постфиксной нотации.
   * <p>
   * Процесс включает:
   * <ol>
   * <li>Разбиение выражения на токены</li>
   * <li>Преобразование инфиксной нотации в постфиксную</li>
   * <li>Вычисление результата по постфиксной записи</li>
   * </ol>
   *
   * @param expression строковое представление выражения
   * @return результат вычисления выражения
   */
  private double evaluateExpression(String expression) {
    List<String> infixTokens = tokenize(expression);

    List<String> postfixTokens = infixToPostfix(infixTokens);

    System.out.println("Постфиксная запись: " + String.join(" ", postfixTokens));

    return evaluatePostfix(postfixTokens);
  }

  /**
   * Разбивает строковое выражение на отдельные токены.
   * <p>
   * Метод идентифицирует числа, операторы и скобки в выражении
   * и преобразует их в список отдельных токенов.
   *
   * @param expression строковое представление выражения
   * @return список токенов (числа, операторы, скобки)
   */
  private List<String> tokenize(String expression) {
    List<String> tokens = new ArrayList<>();
    StringBuilder numberBuilder = new StringBuilder();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);

      if (Character.isDigit(c) || c == '.') {
        numberBuilder.append(c);
      } else if (c == '(' || c == ')' || isOperator(c)) {
        if (numberBuilder.length() > 0) {
          tokens.add(numberBuilder.toString());
          numberBuilder = new StringBuilder();
        }
        tokens.add(String.valueOf(c));
      }
    }

    if (numberBuilder.length() > 0) {
      tokens.add(numberBuilder.toString());
    }

    return tokens;
  }

  /**
   * Преобразует список токенов из инфиксной записи в постфиксную (обратную
   * польскую запись).
   * <p>
   * Метод использует алгоритм Сортировочной станции (Shunting Yard) для
   * преобразования
   * обычной инфиксной записи выражения (например, "2 + 3") в постфиксную форму
   * (например, "2 3 +"),
   * которая проще для вычисления без необходимости учета приоритетов.
   *
   * @param infixTokens список токенов в инфиксной нотации
   * @return список токенов в постфиксной нотации
   */
  private List<String> infixToPostfix(List<String> infixTokens) {
    List<String> postfix = new ArrayList<>();
    Stack<String> operators = new Stack<>();

    for (String token : infixTokens) {
      if (isNumber(token)) {
        postfix.add(token);
      } else if (token.equals("(")) {
        operators.push(token);
      } else if (token.equals(")")) {
        while (!operators.isEmpty() && !operators.peek().equals("(")) {
          postfix.add(operators.pop());
        }

        if (!operators.isEmpty() && operators.peek().equals("(")) {
          operators.pop();
        }
      }

      else if (isOperator(token.charAt(0))) {
        while (!operators.isEmpty() &&
            !operators.peek().equals("(") &&
            getPrecedence(operators.peek()) >= getPrecedence(token)) {
          postfix.add(operators.pop());
        }
        operators.push(token);
      }
    }

    while (!operators.isEmpty()) {
      postfix.add(operators.pop());
    }

    return postfix;
  }

  /**
   * Проверяет, является ли токен числовым значением.
   *
   * @param token строка для проверки
   * @return true если строка может быть преобразована в число, false в противном
   *         случае
   */
  private boolean isNumber(String token) {
    try {
      Double.parseDouble(token);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Возвращает приоритет арифметического оператора.
   * <p>
   * Метод определяет порядок выполнения операций при вычислении выражений.
   * Операторы с более высоким приоритетом выполняются раньше.
   * <ul>
   * <li>Умножение (*) и деление (/) имеют приоритет 2</li>
   * <li>Сложение (+) и вычитание (-) имеют приоритет 1</li>
   * </ul>
   *
   * @param operator строковое представление оператора
   * @return числовое значение приоритета оператора
   */
  private int getPrecedence(String operator) {
    switch (operator) {
      case "+":
      case "-":
        return 1;
      case "*":
      case "/":
        return 2;
      default:
        return 0;
    }
  }

  /**
   * Вычисляет значение выражения, представленного в постфиксной нотации.
   * <p>
   * Метод использует стек для последовательного вычисления значения выражения.
   * Он проходит по всем токенам постфиксного выражения и:
   * <ul>
   * <li>Если токен - число, помещает его на стек</li>
   * <li>Если токен - оператор, извлекает два верхних числа со стека,
   * применяет к ним оператор, и помещает результат обратно на стек</li>
   * </ul>
   * После обработки всех токенов, на стеке должно остаться одно число - результат
   * вычисления.
   *
   * @param postfixTokens список токенов в постфиксной нотации
   * @return результат вычисления выражения
   * @throws IllegalArgumentException если выражение некорректно (например,
   *                                  недостаточно операндов)
   * @throws ArithmeticException      если произошла арифметическая ошибка
   *                                  (например, деление на ноль)
   */
  private double evaluatePostfix(List<String> postfixTokens) {
    Stack<Double> stack = new Stack<>();

    for (String token : postfixTokens) {
      if (isNumber(token)) {
        stack.push(Double.parseDouble(token));
      } else if (isOperator(token.charAt(0))) {
        if (stack.size() < 2) {
          throw new IllegalArgumentException("Недостаточно операндов для оператора " + token);
        }

        double b = stack.pop();
        double a = stack.pop();

        switch (token) {
          case "+":
            stack.push(a + b);
            break;
          case "-":
            stack.push(a - b);
            break;
          case "*":
            stack.push(a * b);
            break;
          case "/":
            if (b == 0) {
              throw new ArithmeticException("Деление на ноль");
            }
            stack.push(a / b);
            break;
        }
      }
    }

    if (stack.size() != 1) {
      throw new IllegalArgumentException("Некорректное выражение");
    }

    return stack.pop();
  }
}
