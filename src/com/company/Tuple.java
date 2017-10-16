package com.company;

public class Tuple {
    public final String variableName;
    public final Integer exitValue;
    public final Integer whileLine;


    public Tuple(String variableName, Integer exitValue, Integer whileLine) {
        this.variableName = variableName;
        this.exitValue = exitValue;
        this.whileLine = whileLine;
    }
}
