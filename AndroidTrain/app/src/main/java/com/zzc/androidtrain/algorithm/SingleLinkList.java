package com.zzc.androidtrain.algorithm;

/**
 * 单链表
 * <p>
 * Created by zczhang on 16/12/1.
 */

public class SingleLinkList {

    private Node mHead;
    private int mCount;

    public void insert(Node node) {
        if (node == null) return;
        if (mHead == null) {
            mHead = node;
        } else {
            node.next = mHead;
            mHead = node;
        }
        mCount++;
    }

    public void printSelf() {
        System.out.println("the list is : " + mHead);
    }

    public void reverse() {
        if (mCount < 2) return;

        Node first = mHead;
        Node second = mHead.next;
        mHead.next = null;
        while (second != null) {
            Node temp = second.next;
            second.next = first;

            first = second;
            second = temp;
        }
        mHead = first;
    }

    /**
     * 链表逆置要点：（遍历）
     * 1、记录当前节点的下一节点
     * 2、当前节点指向上一节点（断开了与下一节点的关系）
     * 3、上一节点引用指向当前节点， 当前节点引用指向下一节点
     * 4、重复上述过程
     *
     * @param current
     * @return
     */
    public Node reverse2(Node current) {
        Node previousNode = null;
        Node nextNode = null;
        while(current != null) {
            nextNode = current.next;

            current.next = previousNode;

            previousNode = current;
            current = nextNode;
        }

        return previousNode;
    }

    /**
     * 单链表逆序要点（使用递归）
     * 1、递归到最后一个节点
     * 2、上一个节点的次节点置空
     * 3、最后一个节点指向上一个节点
     * 4、返回最后一个节点
     * @param current
     * @return
     */
    public Node reverse3(Node current) {
        if(current == null) return null;
        if(current.next == null) return current;

        Node nextNode = current.next;

        Node resetNode = reverse3(nextNode);
        nextNode.next = current;
        current.next = null;


        return resetNode;
    }

    public static class Node {
        private int id;
        private String name;
        public Node next;

        public Node(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", next=" + next +
                    '}';
        }
    }

    public static void main(String[] args) {
        SingleLinkList singleLinkList = new SingleLinkList();
        Node node1 = new Node(1,"1");
        Node node2 = new Node(2,"2");
        Node node3 = new Node(3,"3");
        Node node4 = new Node(4,"4");
        Node node5 = new Node(5,"5");
        singleLinkList.insert(node1);
        singleLinkList.insert(node2);
        singleLinkList.insert(node3);
        singleLinkList.insert(node4);
        singleLinkList.insert(node5);

        singleLinkList.printSelf();
//
//        singleLinkList.reverse();
//
//        singleLinkList.printSelf();

//        Node newHead = singleLinkList.reverse2(singleLinkList.mHead);
        Node newHead = singleLinkList.reverse3(singleLinkList.mHead);

        System.out.println("逆序后 : " + newHead.toString());
    }
}
