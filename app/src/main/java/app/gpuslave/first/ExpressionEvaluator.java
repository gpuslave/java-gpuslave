package app.gpuslave.first;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class ExpressionEvaluator {

  private Map<String, Double> variables;

  private Scanner scanner;

  public ExpressionEvaluator() {
    variables = new HashMap<>();
    scanner = new Scanner(System.in);
  }

  public void close() {
    if (scanner != null) {
      scanner.close();
    }
  }

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
   * Находит переменные из выражения.
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
   * Запрашивает у пользователя переменные из выражения.
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
   * Подставляет переменные в выражение.
   */
  private String replaceVariables(String expression) {
    for (Map.Entry<String, Double> entry : variables.entrySet()) {
      expression = expression.replaceAll(entry.getKey(), entry.getValue().toString());
    }
    return expression;
  }

  /**
   * Проверяет правильность расстановки скобок.
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

  private boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/';
  }

  private double evaluateExpression(String expression) {
    List<String> infixTokens = tokenize(expression);

    List<String> postfixTokens = infixToPostfix(infixTokens);

    System.out.println("Постфиксная запись: " + String.join(" ", postfixTokens));

    return evaluatePostfix(postfixTokens);
  }

  /**
   * Разбивает выражение на токены.
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
   * Преобразует список токенов из инфиксной записи в постфиксную.
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
   * Проверяет, является ли токен числом.
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
   * Возвращает приоритет оператора.
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
   * Вычисляет значение выражения в постфиксной записи.
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
