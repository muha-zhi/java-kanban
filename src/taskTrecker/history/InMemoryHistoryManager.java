package taskTrecker.history;

import taskTrecker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public CustomLinkedList<Task> history = new CustomLinkedList<>();
    public Map<Integer, Node<Task>> tasksToRemove = new HashMap<>();

    @Override
    public void remove(Task task) {
        if (task != null) {
            if (tasksToRemove.containsKey(task.getId())) {
                history.removeNode(tasksToRemove.get(task.getId()));
                tasksToRemove.remove(task.getId());
            }
        }

    }

    @Override
    public void add(Task task) {
        if (task != null) {

            if (tasksToRemove.containsKey(task.getId())) {


                history.removeNode(tasksToRemove.get(task.getId()));
                history.linkLast(tasksToRemove.get(task.getId()));
                tasksToRemove.put(task.getId(), tasksToRemove.get(task.getId()));
            } else {

                Node<Task> newNode = new Node<>(task);
                history.linkLast(newNode);
                tasksToRemove.put(task.getId(), newNode);

            }

            if (history.size() > 10) {
                history.removeHead();
            }
        }
    }

    @Override
    public List<Task> getHistory() {

        List<Task> tasks = new ArrayList<>();

        Node<Task> taskNode = history.head;
        if (!history.isEmpty()) {

            while (taskNode != null) {
                tasks.add(taskNode.data);
                taskNode = taskNode.next;
            }
        }
        return tasks;

    }

    static class CustomLinkedList<T> {

        private Node<T> head;
        private Node<T> tail;
        int size = 0;

        public CustomLinkedList() {
            this.head = null;
            this.tail = null;
        }

        private boolean isEmpty() {
            return head == null && tail == null;
        }

        public void linkLast(Node<T> newNode) {
            if (newNode != null) {
                if (isEmpty()) {
                    head = newNode;


                } else if (head != null && tail == null) {
                    tail = newNode;
                    head.next = tail;
                    tail.next = null;


                } else {

                    tail.next = newNode;
                    newNode.prev = tail;
                    tail = newNode;
                    tail.next = null;
                }

            }

            size++;
        }

        public void removeHead() {
            if (head.next == null) {
                head = null;
            } else {
                head.next.prev = null;
                head = head.next;
            }
            size--;

        }

        public void removeTail() {
            if (head.next == null) {
                head = null;
            } else {
                tail.prev.next = null;
                tail = tail.prev;
            }
            size--;


        }

        public void removeNode(Node<T> node) {
            if (node != null && !isEmpty()) {
                if (node.equals(head)) {
                    removeHead();
                } else if (node.equals(tail)) {
                    removeTail();
                } else {
                    Node<T> next = node.next;
                    Node<T> prev = node.prev;
                    if (next != null && prev != null) {
                        next.prev = node.prev;
                        prev.next = node.next;
                    }

                }

            }
            size--;
        }

        public int size() {
            return size;
        }


    }
}
