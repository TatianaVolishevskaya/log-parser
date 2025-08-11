package logParser;

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

        Statistics statistics = new Statistics();

        try (
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                Entry entry = Entry.parse(line);
                if (entry != null) {
                    statistics.addEntry(entry);
                }
            }
            System.out.println("Среднее количество посещений в час: " + statistics.getAverageVisitsPerHour());
            System.out.println("Среднее количество ошибок в час: " + statistics.getAverageErrorsPerHour());
            System.out.println("Средняя посещаемость одним пользователем: " + statistics.getAverageVisitsPerUser());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}