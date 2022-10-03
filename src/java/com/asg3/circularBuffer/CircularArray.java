package com.asg3.circularBuffer;
/**
 * @author Alexander Torres
 * Professor Phil Lamb
 * ICS 462-50
 * Assignment 3
 * 10/03/22
 * Implementation of a circular array
 */
public class CircularArray {
    private int[] array;
    private int size;
    private int count;
    private int head;
    private int tail;


    public CircularArray(int size) {
        synchronized(this) {
            array = new int[size];
            this.size = size;
            count = 0;
            head = 0;
            tail = 0;
        }
    }

    public boolean add(int value) {
        synchronized (this) {
            if (this.isFull()) {
                return false;
            }

            array[(tail%size)] = value;
            if (!this.isEmpty()) {
                tail += 1;
            }
            this.notifyAll();
            count += 1;
            return true;
        }
    }

    public int peep() {
        return array[head];
    }

    public int pop() {
        synchronized (this) {
            int value = array[(head%size)];
            head +=1;
            count -=1;
            this.notifyAll();
            return value;
        }
    }

    public boolean isFull() {
        return count == size;
    }

    public boolean isEmpty() {
        return count == 0;
    }

}
