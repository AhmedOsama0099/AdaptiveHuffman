package com.adaptivehuffman;

import java.util.ArrayList;

public class Tree {
    Node root = new Node();
    String seq;
    static String curCode = "";
    ArrayList<ShortCodeModel> arr = new ArrayList<>();

    public Tree() {
        root.id = 100;
        seq = "";
        arr.add(new ShortCodeModel('A', "a"));
        arr.add(new ShortCodeModel('B', "b"));
        arr.add(new ShortCodeModel('C', "c"));
        arr.add(new ShortCodeModel('D', "d"));
        arr.add(new ShortCodeModel('E', "e"));
        arr.add(new ShortCodeModel('F', "f"));
    }

    public int searchIndex(char symbol) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getSymbol() == symbol)
                return i;
        }
        return -1;
    }

    public void add(char symbol) {
        if (root.left == null && root.right == null) {
            root.count++;
            root.left = new Node(Character.MIN_VALUE, root.id - 2, 0);
            root.left.parent = root;
            root.right = new Node(symbol, root.id - 1, 1);
            root.right.parent = root;
            seq += arr.get(searchIndex(symbol)).getShortCode();
            return;
        }
        Node current = search(symbol);
        if (current.symbol == symbol) {
            seq += curCode;
            curCode = "";
            current.count++;
        } else {
            current=splitTree(current, symbol);
        }

        updateTree(current);
    }

    private Node splitTree(Node current, char symbol) {//for first time
        current.left = new Node(Character.MIN_VALUE, current.id - 2, 0);
        current.left.parent = current;
        current.right = new Node(symbol, current.id - 1, 1);
        current.right.parent = current;
        seq += curCode;
        curCode = "";
        seq += arr.get(searchIndex(symbol)).getShortCode();
        current = current.right;
        return current;
    }

    private void swap(Node currNode, Node swapNode) {
        Node tempNode;
        int temp = 0;
        temp = currNode.id;
        currNode.id = swapNode.id;
        swapNode.id = temp;
         if(Math.abs(currNode.id-swapNode.id)==1){
             Node parent=currNode.parent;
             tempNode = currNode;
             parent.left=swapNode;
             parent.right=tempNode;
         }
        else {

        }
    }

    private void updateTree(Node current) {
        Node check = new Node();
        check = current;
        if (check.count == 1) {
            while (check != root) {
                check.parent.count++;

                check = check.parent;
                if(check.left.count>check.right.count)
                    swap(check.left,check.right);
            }
        }
    }

    public Node search(char symbol) {
        Node current = new Node();
        current = root;
        while (current != null) {
            if (current.left.symbol == Character.MIN_VALUE) {
                if (current.right.symbol == symbol) {
                    curCode += '1';
                    return current;
                } else {
                    curCode += '0';
                    current = current.left;//Not last NYT
                    if (current.left == null && current.right == null)//last NYT
                        return current;

                }
            } else {
                if (current.left.symbol == symbol) {
                    curCode += '0';
                    return current;
                } else {
                    curCode += '1';
                    current = current.right;//Not last NYT
                    if (current.left == null && current.right == null)//last NYT
                        return current;

                }
            }
        }

        return null;
    }
}
