package edu.yu.cs.com1320.project.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.yu.cs.com1320.project.Stack;

public class StackImplTest {
    private Stack<String> myStrStack;
    private String first;
    private String second;
    private String third;
    private String fourth;
    private String fifth;
    private String sixth;
    private String seventh;
    private String eigth;
    
    @BeforeEach
    void b4(){
        myStrStack = new StackImpl<>();
        this.first = "first";
        this.second = "second";
        this.third = "third";
        this.fourth = "fourth";
        this.fifth = "fifth";
        this.sixth = "sixth";
        this.seventh = "seventh";
        this.eigth = "eigth";
    }

    //push, pop, peek, size
    @Test
    void allTests(){
        assertEquals(0, myStrStack.size());
        assertNull(myStrStack.pop());
        assertNull(myStrStack.peek());

        myStrStack.push(this.first);
        myStrStack.push(this.second);
        assertEquals(this.second, myStrStack.peek());
        assertEquals(this.second, myStrStack.pop());
        assertEquals(this.first, myStrStack.pop());
        assertEquals(null, myStrStack.pop());
        assertEquals(0, myStrStack.size());
        assertNull(myStrStack.peek());

        myStrStack.push(null);
        System.out.println(myStrStack);
        assertEquals(0, myStrStack.size());


        myStrStack.push(this.first);
        myStrStack.push(this.second);
        myStrStack.push(this.third);
        myStrStack.push(this.fourth);
        myStrStack.push(this.fifth);
        assertEquals(5, myStrStack.size());
        myStrStack.push(null);
        assertEquals(5, myStrStack.size());
        assertEquals(this.fifth, myStrStack.pop());
        assertEquals(4, myStrStack.size());
        assertEquals(this.fourth, myStrStack.pop());
    }
}
