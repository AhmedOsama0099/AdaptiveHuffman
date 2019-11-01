package com.adaptivehuffman;

import java.util.Scanner;

public class Main {
   static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) {
        String line=sc.nextLine();
        Tree tree=new Tree();
        for (int i=0;i<line.length();i++)
            tree.add(line.charAt(i));
        System.out.println(tree.seq);
    }
}
