package com.zzc.androidtrain.algorithm;

/**
 * 判断单链表是否存在环
 * <p/>
 * Created by zczhang on 16/9/19.
 */
public class LinkedListCycle {

    static class Node {
        public String name;
        public Node next;

        public Node(String name) {
            this.name = name;
        }
    }

    /**
     * 是否存在环
     *
     * @param head
     * @return
     */
    public boolean hasCycle(Node head) {

        return firstMeetNodeInCycle(head) != null;
    }

    /**
     * 第一次环中相遇点
     *
     * @param head 头结点
     * @return
     */
    public Node firstMeetNodeInCycle(Node head) {
        if (head == null || head.next == null) {
            return null;
        }

        Node slow = head;
        Node fast = head;

        while ((slow = slow.next) != null && (fast = fast.next.next) != null) {
            if (slow == fast) {
                return slow;
            }
        }
        return null;
    }

    /**
     * 环中第一个节点
     * @param head
     * @return
     */
    public Node fistNodeInCycle(Node head) {
        Node meetNode = firstMeetNodeInCycle(head);
        if(meetNode != null) {
            while(head != meetNode) {
                head = head.next;
                meetNode = meetNode.next;
            }
            return head;
        }
        return null;
    }

    public static void main(String[] args) {
        Node head = new Node("node0");
        Node n1 = new Node("node1");
        Node n2 = new Node("node2");
        Node n3 = new Node("node3");
        Node n4 = new Node("node4");
        Node n5 = new Node("node5");
        Node n6 = new Node("node6");
        Node n7 = new Node("node7");
        head.next = n1;
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n6;
        n6.next = n7;
        n7.next = n3;

        LinkedListCycle linkedListCycle = new LinkedListCycle();

        boolean hasCycle = linkedListCycle.hasCycle(head);

        System.out.println("单链表中是否存在环 = " + hasCycle);

        Node meetNode = linkedListCycle.firstMeetNodeInCycle(head);

        System.out.println("环中第一次相遇节点名称 = " + meetNode.name);

        Node fistNode = linkedListCycle.fistNodeInCycle(head);

        System.out.println("环中第一个节点名称 = " + fistNode.name);
    }

}
