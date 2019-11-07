package com.adaptivehuffman;

public class Node {
    public Node parent=null;
    public Node left = null;
    public Node right = null;
    char symbol;
    int id;
    int count;
    String code="";
    public char getSymbol() {
        return symbol;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public Node(char symbol, int id, int count,String code) {
        this.symbol = symbol;
        this.id = id;
        this.count = count;
        this.code=code;
    }

    public Node() {
        this.symbol=Character.MIN_VALUE;
        this.id=0;
        this.count=0;
    }
}
