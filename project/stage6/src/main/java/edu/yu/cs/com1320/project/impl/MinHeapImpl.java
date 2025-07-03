package edu.yu.cs.com1320.project.impl;

import java.util.Arrays;
import java.util.NoSuchElementException;

import edu.yu.cs.com1320.project.MinHeap;

public class MinHeapImpl <E extends Comparable<E>> extends MinHeap<E>{
    public MinHeapImpl(){
        this.elements = (E[]) new Comparable[100];
    }
    
    @Override
    public void reHeapify(E element) {
        int elementIndex = this.getArrayIndex(element);
        upHeap(elementIndex);
        downHeap(elementIndex);
    }
    
    @Override
    /**@throws NoSuchElementException */
    protected int getArrayIndex(E element) { 
        for (int i = 0; i < this.elements.length; i++){
            if (this.elements[i] != null){
                if (this.elements[i].equals(element)){
                    return i;
                }
            }
        }
        //not in the array is an exception
        throw new NoSuchElementException();
    }

    @Override
    protected void doubleArraySize() {
        this.elements = Arrays.copyOf(this.elements, this.elements.length*2);
    }
}
