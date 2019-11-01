package com.adaptivehuffman;

public class Node {
    public Node parent=null;
    public Node left = null;
    public Node right = null;
    char symbol;
    int id;
    int count;
    public char getSymbol() {
        return symbol;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public Node(char symbol, int id, int count) {
        this.symbol = symbol;
        this.id = id;
        this.count = count;
    }

    public Node() {
        this.symbol=Character.MIN_VALUE;
        this.id=0;
        this.count=0;
    }
}
