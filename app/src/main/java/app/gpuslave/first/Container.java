package app.gpuslave.first;

/**
 * Класс {@code Container} представляет собой обобщенный контейнер для хранения
 * элементов любого типа.
 * Он реализован на основе массива и позволяет добавлять, получать, удалять
 * элементы, а также
 * узнавать текущий размер контейнера.
 *
 * @param <T> Тип элементов, хранящихся в контейнере.
 */
public class Container<T> {
  /**
   * Массив для хранения элементов контейнера.
   */
  private T[] elements;
  /**
   * Текущий размер контейнера (количество элементов в нем).
   */
  private int size;
  /**
   * Размер контейнера по умолчанию при создании.
   */
  private static final int DEFAULT_CAPACITY = 10;

  /**
   * Создает новый пустой контейнер с размером по умолчанию.
   */
  @SuppressWarnings("unchecked")
  public Container() {
    elements = (T[]) new Object[DEFAULT_CAPACITY];
    size = 0;
  }

  /**
   * Добавляет элемент в конец контейнера.
   * Если контейнер заполнен, происходит автоматическое увеличение размера
   * массива.
   *
   * @param element Элемент, который необходимо добавить.
   */
  public void add(T element) {
    if (size == elements.length) {
      grow();
    }
    elements[size++] = element;
  }

  /**
   * Возвращает элемент из контейнера по указанному индексу.
   *
   * @param index Индекс элемента, который необходимо получить.
   * @return Элемент, находящийся по указанному индексу.
   * @throws IndexOutOfBoundsException Если индекс находится вне допустимого
   *                                   диапазона.
   */
  public T get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    return elements[index];
  }

  /**
   * Удаляет элемент из контейнера по указанному индексу.
   * После удаления элементы, находящиеся после удаленного, сдвигаются влево.
   *
   * @param index Индекс элемента, который необходимо удалить.
   * @throws IndexOutOfBoundsException Если индекс находится вне допустимого
   *                                   диапазона.
   */
  public void remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    int numMoved = size - index - 1;
    if (numMoved > 0) {
      System.arraycopy(elements, index + 1, elements, index, numMoved);
    }
    elements[--size] = null;
  }

  /**
   * Возвращает текущий размер контейнера (количество элементов в нем).
   *
   * @return Текущий размер контейнера.
   */
  public int size() {
    return size;
  }

  /**
   * Увеличивает размер массива, используемого для хранения элементов, в два раза.
   */
  @SuppressWarnings("unchecked")
  private void grow() {
    int newCapacity = elements.length * 2;
    T[] newElements = (T[]) new Object[newCapacity];
    System.arraycopy(elements, 0, newElements, 0, size);
    elements = newElements;
  }

  /**
   * Выводит в консоль содержимое контейнера в формате [element1, element2, ...,
   * elementN].
   */
  public void print() {
    System.out.print("[");
    for (int i = 0; i < size; i++) {
      System.out.print(elements[i]);
      if (i < size - 1) {
        System.out.print(", ");
      }
    }
    System.out.println("]");
  }
}
