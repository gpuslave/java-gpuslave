package app.gpuslave.first;

public class Container<T> {
  private T[] elements;
  private int size;
  private static final int DEFAULT_CAPACITY = 10;

  @SuppressWarnings("unchecked")
  public Container() {
    elements = (T[]) new Object[DEFAULT_CAPACITY];
    size = 0;
  }

  public void add(T element) {
    if (size == elements.length) {
      grow();
    }
    elements[size++] = element;
  }

  public T get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    return elements[index];
  }

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

  public int size() {
    return size;
  }

  @SuppressWarnings("unchecked")
  private void grow() {
    int newCapacity = elements.length * 2;
    T[] newElements = (T[]) new Object[newCapacity];
    System.arraycopy(elements, 0, newElements, 0, size);
    elements = newElements;
  }
}
