package com.company;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class ProgramReader {
    private File file;

    ProgramReader(File file) {
        this.file = file;
    }

    ArrayList<String> readProgram() {
        ArrayList<String> fileLines = new ArrayList<>();
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (IOException ex) {
            System.out.println("The specfied file was not found.");
            System.out.println("Program will halt.");
            System.exit(0);
        }

        return fileLines;
    }
}
