package app.gpuslave.first;

import java.beans.Expression;
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

      // return evaluateExpression(expression);
      return Double.valueOf(0);

    } catch (Exception e) {
      System.err.println("Ошибка: " + e.getMessage());
      throw new IllegalArgumentException("Некорректное выражение");
    }
  }

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

  private String replaceVariables(String expression) {
    for (Map.Entry<String, Double> entry : variables.entrySet()) {
      expression = expression.replaceAll(entry.getKey(), entry.getValue().toString());
    }
    return expression;
  }

}
