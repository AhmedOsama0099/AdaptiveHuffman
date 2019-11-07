package com.adaptivehuffman;
import java.util.ArrayList;
public class Tree {
    Node root;
    String seq = "";
    ArrayList<ShortCodeModel> arr = new ArrayList<>();
    ArrayList<Node> nodesWithHigherLevel = new ArrayList<>();
    Node lastNYT = null;
    boolean choice = true;
    Node test = null;
    public int searchIndex(char symbol) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getSymbol() == symbol)
                return i;
        }
        return -1;
    }
    public int searchByCode(String code) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getShortCode().equals(code))
                return i;
        }
        return -1;
    }
    public Tree() {
        root = new Node(Character.MIN_VALUE, 100, 0, "");
        seq = "";
        for (int i = 0; i < 128; i++) {
            String code = Integer.toBinaryString(i);
            while (code.length() < 7)
                code = '0' + code;
            arr.add(new ShortCodeModel((char) i, code));
        }
    }
    public void add(char symbol) {
        test = null;
        if (root.left == null && root.right == null) {
            root.count++;
            root.left = new Node(Character.MIN_VALUE, root.id - 2, 0, "0");
            root.left.parent = root;
            lastNYT = root.left;
            if (!choice) {/**deCompress**/
                seq += symbol;
            }
            root.right = new Node(symbol, root.id - 1, 1, "1");
            root.right.parent = root;
            if (choice)
                seq += arr.get(searchIndex(symbol)).getShortCode();
            return;
        }
        search(symbol, root);
        if (test == null) {
            updateTree(splitTree(lastNYT, symbol));
        } else {
            test.count++;
            if (choice)
                seq += test.code;
            else
                seq += test.symbol;
            updateTree(test);

        }

    }
    private Node splitTree(Node current, char symbol) {//for first time
        current.left = new Node(Character.MIN_VALUE, current.id - 2, 0, current.code + 0);
        current.right = new Node(symbol, current.id - 1, 1, current.code + 1);
        current.left.parent = current;
        current.right.parent = current;
        current.count = current.left.count + current.right.count;
        lastNYT = current.left;
        if (choice) {/**Compress**/
            seq += current.code;
            seq += arr.get(searchIndex(symbol)).getShortCode();
        } else {
            seq += symbol;
        }

        current = current.right;
        return current;
    }
    private void updateTree(Node current) {
        Node tmp = current;
        while (current != root) {
            nodesWithHigherLevel.clear();
            findlevelsHigherWithLessWeight(root, current);
            if (nodesWithHigherLevel.size() != 0) {
                for (int i = 1; i < nodesWithHigherLevel.size(); i++) {
                    if (nodesWithHigherLevel.get(i - 1).parent == nodesWithHigherLevel.get(i).parent) {
                        if (nodesWithHigherLevel.get(i).code.charAt(0) == current.code.charAt(0)) {
                            if (nodesWithHigherLevel.get(i - 1).id > nodesWithHigherLevel.get(i).id) {
                                nodesWithHigherLevel.remove(--i);
                            } else
                                nodesWithHigherLevel.remove(i);
                        } else {
                            if (nodesWithHigherLevel.get(i - 1).id < nodesWithHigherLevel.get(i).id) {
                                nodesWithHigherLevel.remove(i--);
                            } else
                                nodesWithHigherLevel.remove(--i);
                        }
                    } else if (nodesWithHigherLevel.get(i).code.length() == nodesWithHigherLevel.get(i - 1).code.length()) {
                        if (nodesWithHigherLevel.get(i).code.charAt(0) == current.code.charAt(0)) {
                            nodesWithHigherLevel.remove(i--);
                        } else {
                            nodesWithHigherLevel.remove(--i);
                        }
                    } else if (Math.abs(current.id - nodesWithHigherLevel.get(i).id) < Math.abs(current.id - nodesWithHigherLevel.get(i - 1).id)) {

                        nodesWithHigherLevel.remove(i--);
                    } else {
                        nodesWithHigherLevel.remove(--i);
                    }
                }
                swap(nodesWithHigherLevel.get(0), current);
                tmp = current;

            }
            current = current.parent;
            current.count = current.left.count + current.right.count;
        }
        while (tmp != root) {

            tmp = tmp.parent;
            tmp.count = tmp.left.count + tmp.right.count;
            if (tmp.left.count > tmp.right.count) {
                swap(tmp.left, tmp.right);

            }
        }
    }
    private void swap(Node currNode, Node swapNode) {
        Node tempNode;
        int temp = 0;
        temp = currNode.id;
        currNode.id = swapNode.id;
        swapNode.id = temp;

        String codeTemp = "";
        codeTemp = currNode.code;
        currNode.code = swapNode.code;
        swapNode.code = codeTemp;
        if (currNode.parent == swapNode.parent) {
            Node parent = currNode.parent;
            tempNode = currNode;
            parent.left = swapNode;
            parent.right = tempNode;
            changingCodes(parent.left, parent.left);
            changingCodes(parent.right, parent.right);
        } else {
            if (currNode.parent.left == currNode) {
                currNode.parent.left = swapNode;
                if (swapNode.parent.right == swapNode)
                    swapNode.parent.right = currNode;
                else
                    swapNode.parent.left = currNode;

                Node parentTemp = swapNode.parent;
                swapNode.parent = currNode.parent;
                currNode.parent = parentTemp;
            } else {
                currNode.parent.right = swapNode;
                if (swapNode.parent.right == swapNode)
                    swapNode.parent.right = currNode;
                else
                    swapNode.parent.left = currNode;
                Node parentTemp = swapNode.parent;
                swapNode.parent = currNode.parent;
                currNode.parent = parentTemp;
            }
            changingCodes(currNode, currNode);
            changingCodes(swapNode, currNode);
        }

    }
    private void changingCodes(Node mover, Node curr) {
        if (mover == null)
            return;
        if (mover != curr) {
            if (mover == mover.parent.left) {
                mover.code = mover.parent.code+"0";
            } else {
                mover.code = mover.parent.code+"1";
            }
        }
        if(mover.left==null&&mover.right==null&&mover.symbol==Character.MIN_VALUE)
            lastNYT=mover;
        changingCodes(mover.left, curr);
        changingCodes(mover.right, curr);
    }
    private void search(char symbol, Node curr) {

        if (curr == null) {
            return;
        }

        if (curr.symbol == symbol) {
            test = curr;
        }
        search(symbol, curr.right);
        search(symbol, curr.left);
    }
    public String getLastNYTCode() {
        return lastNYT.code;
    }
    private boolean isSubSeq(String seq1, String seq2) {
        for (int i = 0; i < seq1.length(); i++) {
            if (seq1.charAt(i) != seq2.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    private void findlevelsHigherWithLessWeight(Node mover, Node curr) {
        if (mover == null) {
            return;
        }
        if (mover.symbol != curr.symbol && curr.count > mover.count && curr.code.length() > mover.code.length() && mover != root) {
            if (!isSubSeq(mover.code, curr.code))
                nodesWithHigherLevel.add(mover);
        }
        findlevelsHigherWithLessWeight(mover.right, curr);
        findlevelsHigherWithLessWeight(mover.left, curr);
    }
    public int searchSymbolCode(String remainingSeq) {
        int i = 0;
        Node current = root;
        for (; i < remainingSeq.length(); i++) {
            if (remainingSeq.charAt(i) == '1')
                current = current.right;
            else
                current = current.left;

            if (current.right == null && current.left == null) {
                add(current.symbol);
                break;
            }
        }
        return (i + 1);
    }
    public void printTree(Node mover) {
        if (mover == null)
            return;
        System.out.println(mover.id + " " + mover.count + " " + mover.symbol + " " + mover.code);
        printTree(mover.left);
        printTree(mover.right);
    }
}
