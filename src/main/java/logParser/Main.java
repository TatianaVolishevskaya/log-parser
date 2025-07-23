package logParser;

import java.util.Scanner;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String path = "access.log";
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Файл не существует: " + path);
            return;
        }
        if (!file.isFile()) {
            System.err.println("Указанный путь не является файлом: " + path);
            return;
        }
        int lineCount = 0;
        int maxLength = Integer.MIN_VALUE;
        int minLength = Integer.MAX_VALUE;
        try (
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (length > 1024) {
                    throw new LineTooLongException("Обнаружена слишком длинная строка: " + length + " символов");
                }
                lineCount++;
                if (length > maxLength) {
                    maxLength = length;
                }
                if (length < minLength) {
                    minLength = length;
                }
            }
            System.out.println("Общее количество строк: " + lineCount);
            System.out.println("Максимальная длина строки: " + maxLength);
            System.out.println("Минимальная длина строки: " + minLength);
        } catch (LineTooLongException ex) {
            System.err.println("ОШИБКА: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}