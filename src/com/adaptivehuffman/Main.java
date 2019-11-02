package com.adaptivehuffman;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Scanner sc2 = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.print("1-Compress 2-Decompress 3-close \nChoice: ");
            int choice = sc2.nextInt();
            if(choice==3)
                System.exit(0);
            System.out.print("Enter Seq: ");
            String seq = sc.nextLine();
            if (choice == 1) {
                Tree tree = new Tree();
                tree.choice = true;
                for (int i = 0; i < seq.length(); i++)
                    tree.add(seq.charAt(i));
                System.out.println(tree.seq);
            }
            if (choice == 2) {
                Tree tree = new Tree();
                tree.choice = false;
                int currPositionInSeq = 0;
                int symbolIndex = tree.searcByCode(seq.substring(currPositionInSeq, 7));/////////

                tree.add(tree.arr.get(symbolIndex).getSymbol());//add first char
                currPositionInSeq += 7;//////after first char
                while (currPositionInSeq < seq.length()) {
                    String lastNytCode = tree.getLastNYTCode();//get Last NYT to add in it
                    if ((seq.length() - currPositionInSeq) >= lastNytCode.length()) {//to check if it's new symbol or not
                        //00 0 01
                        if (seq.substring(currPositionInSeq, currPositionInSeq+ lastNytCode.length()).equals(lastNytCode)) {
                            currPositionInSeq += lastNytCode.length();
                            symbolIndex = tree.searcByCode(seq.substring(currPositionInSeq, currPositionInSeq+7));///////
                            tree.add(tree.arr.get(symbolIndex).getSymbol());
                            currPositionInSeq += 7;/////////
                        } else {
                            currPositionInSeq+=tree.searchSymbolCode(seq.substring(currPositionInSeq,seq.length()));
                        }
                    } else {//sure it won't be new symbol
                        currPositionInSeq+=tree.searchSymbolCode(seq.substring(currPositionInSeq,seq.length()));
                    }
                }
                System.out.println(tree.seq);
            }

        }

    }
}