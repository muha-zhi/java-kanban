package taskTrecker.history;

import com.google.gson.annotations.Expose;

public class Node<T> {
@Expose
    T data;

    Node<T> prev = null;


    Node<T> next = null;


    public Node(T data) {
        this.data = data;

    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            @SuppressWarnings("unchecked")
            Node<T> node = (Node<T>) o;
            if(node != null) {
                return data.equals(node.data) && next.equals(node.next) && prev.equals(node.prev);
            }
            return false;


    }

    @Override
    public int hashCode() {
        int hash = 17;


        if (next != null) {

            hash = hash + next.hashCode();
        }
        hash = hash * 31;
        if (prev != null) {

            hash = hash + prev.hashCode();
        }
        hash = hash * 31;
        if (data != null) {

            hash = hash + data.hashCode();
        }


        return hash;
    }
}
