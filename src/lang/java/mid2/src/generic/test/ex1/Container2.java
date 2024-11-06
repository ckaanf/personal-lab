package mid2.src.generic.test.ex1;

public class Container2<T> {

	private T item;

    public boolean isEmpty() {
        return item != null;
    }

    public T getItem() {
        return item;
    }

	public void setItem(T item) {
		this.item = item;
	}

}