package logParser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    // Задание №1 — Среднее количество посещений сайта за час (не боты)
    public double getAverageVisitsPerHour() {
        if (startTime == null || endTime == null) return 0.0;
        long hours = ChronoUnit.HOURS.between(startTime, endTime) + 1;
        long visits = entries.stream()
                .filter(e -> !e.getUserAgent().toLowerCase().contains("bot"))
                .count();
        return hours > 0 ? (double) visits / hours : 0.0;
    }

    // Задание №1 — Среднее количество ошибочных запросов в час
    public double getAverageErrorsPerHour() {
        if (startTime == null || endTime == null) return 0.0;
        long hours = ChronoUnit.HOURS.between(startTime, endTime) + 1;
        return hours > 0 ? (double) errorRequests / hours : 0.0;
    }

    // Задание №1 — Средняя посещаемость одним пользователем
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

    // Задание №2 — Пиковая посещаемость в секунду
    public long getPeakVisitsPerSecond() {
        return entries.stream()
                .filter(e -> !e.getUserAgent().toLowerCase().contains("bot"))
                .collect(Collectors.groupingBy(
                        e -> e.getTime().withNano(0),
                        Collectors.counting()
                ))
                .values()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
    }

    // Задание №2 — Список доменов из referer-ов
    public Set<String> getRefererDomains() {
        return entries.stream()
                .map(Entry::getReferer)
                .filter(r -> r != null && !r.isBlank() && !r.equals("-"))
                .map(r -> {
                    try {
                        return new java.net.URI(r).getHost();
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(host -> host.startsWith("www.") ? host.substring(4) : host)
                .collect(Collectors.toSet());
    }

    // Задание №2 — Максимальная посещаемость одним пользователем
    public long getMaxVisitsByOneUser() {
        return entries.stream()
                .filter(e -> !e.getUserAgent().toLowerCase().contains("bot"))
                .collect(Collectors.groupingBy(
                        Entry::getIpAddress,
                        Collectors.counting()
                ))
                .values()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
    }
}