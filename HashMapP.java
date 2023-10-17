import java.io.*;

public class HashMapP {
    static final class Node {
        Object key;
        Node next;
        int count;
        int value;
        int[] document = new int[10];
        Node(Object k, Node n) { key = k; next = n; }
    }
    Node[] table = new Node[128]; // always a power of 2
    int size = 0;
    boolean contains(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }
    void count(Object key){
        int h = key.hashCode();
        int i = h & (table.length -1);
        for(Node e = table[i]; e != null; e = e.next){
            if(key.equals(e.key)){
                e.count++;
            }
        }
    }

    void docUpdate(Object key,int doc){
        int h = key.hashCode();
        int i = h & (table.length -1);
        for(Node e = table[i]; e != null; e = e.next){
            if(key.equals(e.key)){
                e.document[doc] = doc+1;
            }
        }
    }
    void add(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.count++;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        table[i].count = 1;
        ++size;
        if ((float)size/table.length >= 0.75f)
            resizeV2();
    }
    void resizeV2() { // avoids unnecessary creation
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            Node e = oldTable[i];
            while (e != null) {
                Node next = e.next;
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                e.next = newTable[j];
                newTable[j] = e;
                e = next;
            }
        }
        table = newTable;
    }
    void remove(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        Node e = table[i], p = null;
        while (e != null) {
            if (key.equals(e.key)) {
                if (p == null)
                    table[i] = e.next;
                else
                    p.next = e.next;
                break;
            }
            p = e;
            e = e.next;
        }
    }
    void printAll() {
        for (int i = 0; i < table.length; ++i)
            for (Node e = table[i]; e != null; e = e.next)
                System.out.println(e.key);
    }
    private void writeObject(ObjectOutputStream s) throws Exception {
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
            }
        }
    }
    private void readObject(ObjectInputStream s) throws Exception {
        s.defaultReadObject();
        int n = s.readInt();
        for (int i = 0; i < n; ++i)
            add(s.readObject());
    }
}
