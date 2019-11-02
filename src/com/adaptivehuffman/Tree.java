package com.adaptivehuffman;

import java.util.ArrayList;

public class Tree {
    Node root = new Node();
    String seq="";

    static String curCode = "";
    ArrayList<ShortCodeModel> arr = new ArrayList<>();
    Node lastNYT=null;
    boolean choice=true;
    public Tree() {
        root.id = 100;
        seq = "";
        arr.add(new ShortCodeModel('A', "00"));
        arr.add(new ShortCodeModel('B', "01"));
        arr.add(new ShortCodeModel('C', "10"));
        arr.add(new ShortCodeModel('D', "11"));
        arr.add(new ShortCodeModel('E', "100"));
        arr.add(new ShortCodeModel('F', "101"));
    }

    public int searchIndex(char symbol) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getSymbol() == symbol)
                return i;
        }
        return -1;
    }
    public int searcByCode(String code){
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getShortCode() .equals(code))
                return i;
        }
        return -1;
    }

    public void add(char symbol) {
        if (root.left == null && root.right == null) {
            root.count++;
            root.left = new Node(Character.MIN_VALUE, root.id - 2, 0);
            root.left.parent = root;
            if(!choice){/**deCompress**/
                lastNYT=root.left;
                seq+=symbol;
            }
            root.right = new Node(symbol, root.id - 1, 1);
            root.right.parent = root;
            if(choice)
                seq += arr.get(searchIndex(symbol)).getShortCode();
            return;
        }
        Node current = search(symbol);
        if (current.symbol == symbol) {
            if (choice){/**Compress**/
                seq +=curCode;
                curCode = "";
            }
            else{
                seq+=symbol;
            }
            current.count++;
        } else {
            current=splitTree(current, symbol);
        }

        updateTree(current);
        /*if(!choice)
            System.out.println(lastNYT.id+" "+getLastNYTCode());*/
    }

    public String getLastNYTCode() {
        String NYTCode="";
        Node current=root;
        while (current!=lastNYT){
            if(current.left.symbol==Character.MIN_VALUE){
                NYTCode+='0';
                current=current.left;
            }
            else {
                NYTCode+='1';
                current=current.right;
            }
        }
        return NYTCode;
    }

    private Node splitTree(Node current, char symbol) {//for first time
        current.left = new Node(Character.MIN_VALUE, current.id - 2, 0);
        current.left.parent = current;
        if(!choice)/**deCompress**/
            lastNYT=current.left;
        current.right = new Node(symbol, current.id - 1, 1);
        current.right.parent = current;
        if(choice){/**Compress**/
            seq +=curCode;
            seq +=arr.get(searchIndex(symbol)).getShortCode();
            curCode = "";
        }
        else{
            seq+=symbol;
        }

        current = current.right;
        return current;
    }

    private void swap(Node currNode, Node swapNode) {
        Node tempNode;
        int temp = 0;
        temp = currNode.id;
        currNode.id = swapNode.id;
        swapNode.id = temp;
        if(currNode.parent==swapNode.parent){
            Node parent=currNode.parent;
            tempNode = currNode;
            parent.left=swapNode;
            parent.right=tempNode;
        }
        else {
            if(currNode.parent.left==currNode){
                currNode.parent.left=swapNode;
                if(swapNode.parent.right==swapNode)
                    swapNode.parent.right=currNode;
                else
                    swapNode.parent.left=currNode;

                Node parentTemp=swapNode.parent;
                swapNode.parent=currNode.parent;
                currNode.parent=parentTemp;
            }
            else {
                currNode.parent.right=swapNode;
                if(swapNode.parent.right==swapNode)
                    swapNode.parent.right=currNode;
                else
                    swapNode.parent.left=currNode;
                Node parentTemp=swapNode.parent;
                swapNode.parent=currNode.parent;
                currNode.parent=parentTemp;
            }
        }
    }

    private void updateTree(Node current) {
        Node check;
        check = current;
        if (check.count == 1) {
            while (check != root) {
                check.parent.count++;
                check = check.parent;
                if(check.left.count>check.right.count)
                    swap(check.left,check.right);
            }
        }
        else {
            Node check2=root;
            while(true){
                if(check2.left.symbol!=Character.MIN_VALUE){
                    if(check2.left==current)
                        break;
                    if(check2.left.count<current.count ) {
                        swap(check2.left, current);
                        break;
                    }
                    else{
                        check2=check2.right;
                    }
                }
                else {
                    if(check2.right==current)
                        break;
                    if(check2.right.count<current.count) {
                        swap(check2.right, current);
                        break;
                    }
                    else{
                        check2=check2.left;
                    }
                }
            }
            while (check != root) {
                check.parent.count=check.parent.left.count+check.parent.right.count;
                check = check.parent;
                if(check.left.count>check.right.count) {
                    swap(check.left, check.right);
                }
            }
        }
    }

    public Node search(char symbol) {
        Node current = root;
        while (current != null) {
            if (current.left.symbol == Character.MIN_VALUE) {
                if (current.right.symbol == symbol) {
                    if(choice)/**Compress**/
                        curCode += '1';
                    return current.right;
                } else {
                    if(choice)/**Compress**/
                        curCode += '0';
                    current = current.left;//Not last NYT
                    if (current.left == null && current.right == null)//last NYT
                        return current;
                }
            } else {
                if (current.left.symbol == symbol) {
                    if(choice)/**Compress**/
                        curCode += '0';
                    return current.left;
                } else {
                    if(choice)/**Compress**/
                        curCode += '1';
                    current = current.right;//Not last NYT
                    if (current.left == null && current.right == null)//last NYT
                        return current;
                }
            }
        }
        return null;
    }
    public int searchSymbolCode(String remainingSeq){
        int i=0;
        Node current =root;
        for(;i<remainingSeq.length();i++){
            if(remainingSeq.charAt(i)=='1')
                current=current.right;
            else
                current=current.left;

            if(current.right==null&&current.left==null){
                add(current.symbol);
                break;
            }
        }
        return (i+1);
    }
}
