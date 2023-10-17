package cse214hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DynamicIntegerSet implements DynamicSet {
    private Node root; // the starting node of the tree only
    private Node tempRoot; //for traversing;
    private int size;
    public static class Node implements PrintableNode {
        Integer data;
        Node left, right;
        Node(int x) { this(x, null, null); }
        Node(int x, Node left, Node right) {
            this.data = x;
            this.left = left;
            this.right = right;
        }
        @Override
        public String getValueAsString() { return data.toString(); }
        @Override
        public PrintableNode getLeft() { return left; }
        @Override
        public PrintableNode getRight() { return right; }
    }
    // this method must be there exactly in this form
    public Node root() { return this.root; } // works

    @Override
    public int size() {
        return size;
    } // works

    @Override
    public boolean contains(Integer x) { //checks if the integer x is a node with in the set / tree
        if(root == null){
            return false;
        }
        tempRoot = root;
        while(true){
            if(x.intValue() == tempRoot.data){
                return true;
            }else if(x > tempRoot.data & hasRight(tempRoot)){
                tempRoot = tempRoot.right;
            }else if(x < tempRoot.data & hasLeft(tempRoot)) {
                tempRoot = tempRoot.left;
            }else{
                return false;
            }
        }
    } //works

    @Override
    public boolean add(Integer x) { //adds a new node to the tree
        if(root == null){
            root = new Node(x);
            size++;
            return true;
        }
        tempRoot = root;
        while(true){
            if(x > tempRoot.data & !hasRight(tempRoot)){
                tempRoot.right = new Node(x);
                size++;
                break;
            }else if(x < tempRoot.data & !hasLeft(tempRoot)){
                tempRoot.left = new Node(x);
                size++;
                break;
            }else if( x > tempRoot.data & hasRight(tempRoot)){
                tempRoot = tempRoot.right;
            }else if(x < tempRoot.data & hasLeft(tempRoot)){
                tempRoot = tempRoot.left;
            }else {
                break;
            }
        }
        return true;
    } //works

    @Override
    public boolean remove(Integer x) { //removes a new node to the tree
        if(!contains(x)){
            return false;
        }
        tempRoot = root;
        while(true){
            if(x > tempRoot.data){
                tempRoot = tempRoot.right;
            }else if(x < tempRoot.data){
                tempRoot = tempRoot.left;
            }else{
                break;
            }
        }
        if(!hasLeft(tempRoot) & !hasRight(tempRoot)){ // works
            Node parent = parent(x);
            if(tempRoot.data > parent.data){
                parent.right = null;
            }else{
                parent.left = null;
            }
            size--;
            return true;
        }else if((!hasLeft(tempRoot) & hasRight(tempRoot)) | (!hasRight(tempRoot) & hasLeft(tempRoot))){ //works
            Node parent = parent(x);
            Node child;
            if(hasLeft(tempRoot)){
                child = tempRoot.left;
            }else {
                child = tempRoot.right;
            }
            if(tempRoot.data > parent.data){
                parent.right = child;
            }else{
                parent.left = child;
            }
            size--;
            return true;
        }else { //works
            Node successor = tempRoot.right;
            Node temp = tempRoot;
            while(successor.left != null){
                successor = successor.left;
            }
            remove(successor.data);
            if(Objects.equals(temp.data, root.data)){
                successor.left = root.left;
                successor.right = root.right;
                root = successor;
            }else{
                tempRoot = temp;
                successor.left = tempRoot.left;
                successor.right = tempRoot.right;
                Node parent = parent(tempRoot.data);
                if(tempRoot.data > parent.data){
                    parent.right = successor;
                }else{
                    parent.left = successor;
                }
            }
            return true;
        }
    }
    private Node parent(Integer x){
        Node temp = root;
        Node Parent;
        while (true){
            if(x > temp.data){
                if(!Objects.equals(temp.right.data, x)){
                    temp = temp.right;
                } else {
                    Parent = temp;
                    break;
                }
            }else{
                if(!Objects.equals(temp.left.data, x)){
                    temp = temp.left;
                } else {
                    Parent = temp;
                    break;
                }
            }
        }
        return Parent;
    }

    private boolean hasRight(Node temp){
        return temp.right != null;
    }
    private boolean hasLeft(Node temp){
        return temp.left != null;
    }

    public static void printTree(PrintableNode node) {
        List<List<String>>           lines = new ArrayList<>();
        List<PrintableNode> level = new ArrayList<>();
        List<PrintableNode> next  = new ArrayList<>();

        level.add(node);
        int nn = 1;
        int widest = 0;
        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;
            for (PrintableNode n : level) {
                if (n == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.getValueAsString();
                    line.add(aa);
                    if (aa.length() > widest)
                        widest = aa.length();
                    next.add(n.getLeft());
                    next.add(n.getRight());
                    if (n.getLeft() != null)
                        nn++;
                    if (n.getRight() != null)
                        nn++;
                }
            }
            if (widest%2 == 1)
                widest++;
            lines.add(line);
            List<PrintableNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (String f : line) {
                if (f == null) f = "";
                final float a    = perpiece / 2f - f.length() / 2f;
                int   gap1 = (int) Math.ceil(a);
                int   gap2 = (int) Math.floor(a);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();
            perpiece /= 2;
        }
    }

}
