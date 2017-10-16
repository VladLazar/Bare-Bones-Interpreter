package com.company;

import java.io.*;
import java.util.*;


class Interpreter {
    private HashMap<String, Integer> variablesMap = new HashMap<>();
    private Stack<Tuple> whileStack = new Stack<>();
    private ArrayList<String> fileLines = new ArrayList<>();
    private String filePath;
    private int currentLine;
    private ErrorOutputer errorOutputer;

    Interpreter(String filePath) {
        this.filePath = filePath;
        this.errorOutputer = new ErrorOutputer();

        ProgramReader reader = new ProgramReader(new File(filePath));

        this.fileLines = reader.readProgram();
    }

    void printAllVariables() {
        for(HashMap.Entry<String, Integer> entry : variablesMap.entrySet()) {
            String output = String.format("%s : %d", entry.getKey(), entry.getValue());
            System.out.println(output);
        }
    }

    void interpretProgram() {
        //Technically not a loop. Used to determine when program reaches the end.
        Tuple stopTuple = new Tuple("Flag", -1, -1);
        whileStack.push(stopTuple);

        currentLine = 0;
        while(!whileStack.empty()) {
            processCurrentLine();
            currentLine++;
            //Pops the stopTuple after all the lines have been interpreted.
            if(currentLine == fileLines.size())
                whileStack.pop();
        }
    }

    private void processCurrentLine() {
        boolean matchedFlag = false;
        String updatedFile = fileLines.get(currentLine).replaceAll(";$", "");
        String[] wordsOnLine = updatedFile.trim().split("\\s+");
        if(matchesClear(wordsOnLine)) {
            applyClear(wordsOnLine[1]);
            matchedFlag = true;
        }
        if(matchesIncrease(wordsOnLine)) {
            applyIncrease(wordsOnLine[1]);
            matchedFlag = true;
        }
        if(matchesDecrease(wordsOnLine)) {
            applyDecrease(wordsOnLine[1]);
            matchedFlag = true;
        }
        if(matchesWhile(wordsOnLine)) {
            applyWhile(wordsOnLine[1], wordsOnLine[3]);
            matchedFlag = true;
        }
        if(matchesEnd(wordsOnLine)) {
            applyEnd();
            matchedFlag = true;
        }

        if(!matchedFlag) {
            errorOutputer.outputError(String.
                    format("Error: Line %2d => Could not match line to a valid instruction.",
                            currentLine + 1));
        }
    }

    private void applyClear(String variableName) {
        if(variablesMap.containsKey(variableName)) {
            variablesMap.replace(variableName, 0);
        } else {
            variablesMap.put(variableName, 0);
        }
    }

    private void applyIncrease(String variableName) {
        if(variablesMap.containsKey(variableName)) {
            variablesMap.replace(variableName, variablesMap.get(variableName) + 1);
        } else {
            errorOutputer.outputError(String.
                    format("Error: Line %2d => Variable %s has not been declared.",
                            currentLine + 1, variableName));
        }
    }

    private void applyDecrease(String variableName) {
        if(variablesMap.containsKey(variableName)) {
            variablesMap.replace(variableName, variablesMap.get(variableName) - 1);
        } else {
            errorOutputer.outputError(String.
                    format("Error: Line %2d => Variable %s has not been declared.",
                            currentLine + 1, variableName));
        }
    }

    private void applyWhile(String variableName, String exitValue) {
        whileStack.push(new Tuple(variableName, Integer.parseInt(exitValue), currentLine));
    }

    private void applyEnd() {
        Tuple currentWhile = whileStack.peek();
        if(variablesMap.containsKey(currentWhile.variableName)) {
            if(variablesMap.get(currentWhile.variableName).equals(currentWhile.exitValue)) {
                whileStack.pop();
            } else {
                currentLine = currentWhile.whileLine;
            }
        } else {
            errorOutputer.outputError(String.
                    format("Error: Line %2d => Variable %s has not been declared.",
                            currentLine + 1, currentWhile.variableName));
        }
    }

    private boolean matchesClear(String[] wordsOnLine) {
        return (wordsOnLine.length == 2 && wordsOnLine[0].equals("clear")
                && !wordsOnLine[1].isEmpty());

    }

    private boolean matchesIncrease(String[] wordsOnLine) {
        return (wordsOnLine.length == 2 && wordsOnLine[0].equals("incr")
                && !wordsOnLine[1].isEmpty());
    }

    private boolean matchesDecrease(String[] wordsOnLine) {
        return (wordsOnLine.length == 2 && wordsOnLine[0].equals("decr")
                && !wordsOnLine[1].isEmpty());
    }

    private boolean matchesWhile(String[] wordsOnLine) {
        return (wordsOnLine.length == 5 && wordsOnLine[0].equals("while")
                && !wordsOnLine[1].isEmpty() && wordsOnLine[2].equals("not")
                && !wordsOnLine[3].isEmpty() && wordsOnLine[4].equals("do"));
    }

    private boolean matchesEnd(String[] wordsOnLine) {
        return (wordsOnLine.length == 1 && wordsOnLine[0].equals("end"));
    }
}
