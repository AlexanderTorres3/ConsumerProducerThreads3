package com.asg3.main;

import java.io.*;
import java.util.*;

import com.asg3.circularBuffer.*;
/**
 * @author Alexander Torres
 * Professor Phil Lamb
 * ICS 462-50
 * Assignment 3
 * 10/03/22
 *
 *  program were two threads share a common circular buffer. The producer thread produces values to insert into the
 *  circular buffer. The consumer thread removes values from the circular buffer and writes them into an output file.
 */
public class Main {
    private static CircularArray array = new CircularArray(5);

    public static void main(String[] args) {

        //start producer and consumer  threads
        new Thread(Main::producerRunWork).start();
        new Thread(Main::consumerRunWork).start();
    }

    private static void producerRunWork(){
        Random rand = new Random();
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(rand.nextInt(3000) + 1000);
            } catch (InterruptedException x1) {
                x1.printStackTrace();
            }
            synchronized(array) {

                while (array.isFull()) {
                    System.out.println("producer waiting");
                    try {
                        array.wait(1000);
                    } catch (InterruptedException x) {
                        x.printStackTrace();
                    }
                }

                if (i >= 99) {
                    array.add(-1);
                    return;
                }

                array.add(i);
                System.out.println("value " + i + " added");
            }
        }
    }

    private static void consumerRunWork() {
        try {
            PrintWriter output = new PrintWriter("output.txt", "UTF-16");
            System.out.println("file created");
            Random rand = new Random();
            while (true) {
                try {
                    Thread.sleep(rand.nextInt(3000) + 1000);
                } catch (InterruptedException x1) {
                    x1.printStackTrace();
                }
                synchronized(array) {
                    while (array.isEmpty()) {
                        try {
                            System.out.println("consumer waiting");
                            output.println("consumer waiting");
                            array.wait(1000);
                        } catch (InterruptedException x) {
                            x.printStackTrace();
                        }
                    }

                    int value = array.pop();
                    System.out.println("value " + value + " removed");
                    if (value == -1) {
                        output.println("consumer done");
                        output.close();
                        System.out.println("consumer returning");
                        return;
                    }
                    output.println(value);
                }
            }
        } catch (IOException x) {
            x.printStackTrace();
            System.out.println("file error");
        }


    }
}
