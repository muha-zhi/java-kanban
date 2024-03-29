package taskTrecker.history;

import com.google.gson.annotations.Expose;
import taskTrecker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    @Expose
    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    @Expose
    private final Map<Integer, Node<Task>> tasksToRemove = new HashMap<>();

    @Override
    public void clearHistory() {
        List<Task> tasks = new ArrayList<>();
        for (Integer k : tasksToRemove.keySet()) {
            tasks.add(tasksToRemove.get(k).data);
        }
        for (Task t : tasks) {
            remove(t);
        }
    }

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

    public Map<Integer, Node<Task>> getTasksToRemove() {
        return tasksToRemove;
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
        @Expose
        private Node<T> head;
        @Expose
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
                if (head == null) {
                    head = newNode;


                } else if (tail == null) {
                    tail = newNode;
                    head.next = tail;
                    tail.next = null;
                    tail.prev = head;


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
                if(head.next == tail){
                    head = tail;
                    head.prev = null;
                    head.next = null;
                    tail = null;


                } else {
                    head.next.prev = null;
                    head = head.next;
                }
            }
            size--;

        }

        public void removeTail() {
            if (head != null) {
                if (head.next == null) {
                    head = null;
                } else {
                    tail.prev.next = null;
                    tail = tail.prev;
                }
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
                    size--;

                }

            }

        }

        public int size() {
            return size;
        }


    }
}
