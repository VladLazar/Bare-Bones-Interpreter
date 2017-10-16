package com.company;

public class BareBones {

    public static void main(String[] args) {
        Interpreter bareBonesInterpreter = new Interpreter(args[0]);
        bareBonesInterpreter.interpretProgram();
        bareBonesInterpreter.printAllVariables();
    }
}
