package com.apploidxxx.notex.compare.basic;

public class BasicException {
    private int calc() {
        // some computing code can be there...
        return 1/0;
    }

    public int main () {
        int result;

        try {
            result = calc();
        } catch (ArithmeticException exception) {
            result = 0;
        }

        return result;
    }

}
