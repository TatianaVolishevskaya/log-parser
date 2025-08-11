package logParser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private final List<Entry> entries = new ArrayList<>();
    private long errorRequests = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void addEntry(Entry entry) {
        entries.add(entry);

        if (startTime == null || entry.getTime().isBefore(startTime)) {
            startTime = entry.getTime();
        }
        if (endTime == null || entry.getTime().isAfter(endTime)) {
            endTime = entry.getTime();
        }
        if (entry.getStatusCode() >= 400 && entry.getStatusCode() < 600) {
            errorRequests++;
        }
    }
    public double getAverageVisitsPerHour() {
        if (startTime == null || endTime == null) return 0.0;

        long hours = ChronoUnit.HOURS.between(startTime, endTime) + 1;
        long visits = entries.stream()
                .filter(e -> !e.getUserAgent().toLowerCase().contains("bot"))
                .count();

        return hours > 0 ? (double) visits / hours : 0.0;
    }

    public double getAverageErrorsPerHour() {
        if (startTime == null || endTime == null) return 0.0;

        long hours = ChronoUnit.HOURS.between(startTime, endTime) + 1;
        return hours > 0 ? (double) errorRequests / hours : 0.0;
    }

    public double getAverageVisitsPerUser() {
        List<Entry> realUsers = entries.stream()
                .filter(e -> !e.getUserAgent().toLowerCase().contains("bot"))
                .toList();

        long uniqueIps = realUsers.stream()
                .map(Entry::getIpAddress)
                .distinct()
                .count();

        return uniqueIps > 0 ? (double) realUsers.size() / uniqueIps : 0.0;
    }
}