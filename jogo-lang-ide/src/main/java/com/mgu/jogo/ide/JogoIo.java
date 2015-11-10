package com.mgu.jogo.ide;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.Optional;

public class JogoIo {

    public static Optional<File> saveDialog() {
        final File file = fileChooser().showSaveDialog(null);
        if (file == null) {
            return Optional.empty();
        } else {
            return Optional.of(file);
        }
    }

    private static FileChooser fileChooser() {
        final FileChooser fileChooser = new FileChooser();
        final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JOGO programs (*.jogo)", "*.jogo");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }

    public static void save(final String content, final File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<File> loadDialog() {
        final File file = fileChooser().showOpenDialog(null);
        if (file == null) {
            return Optional.empty();
        } else {
            return Optional.of(file);
        }
    }

    public static String load(final File file) {
        final StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
