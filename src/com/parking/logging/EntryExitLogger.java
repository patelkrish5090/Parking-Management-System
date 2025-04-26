package com.parking.logging;

import com.parking.users.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryExitLogger {
    private final Map<String, List<LogEntry>> userLogs;
    private final List<LogEntry> systemLog;

    public EntryExitLogger() {
        this.userLogs = new HashMap<>();
        this.systemLog = new ArrayList<>();
    }

    public void logEntry(User user, LocalDateTime time) {
        LogEntry entry = new LogEntry(user.getUserId(), time, true);
        logToSystem(entry);
        logToUserHistory(user.getUserId(), entry);
    }

    public void logExit(User user, LocalDateTime time) {
        LogEntry entry = new LogEntry(user.getUserId(), time, false);
        logToSystem(entry);
        logToUserHistory(user.getUserId(), entry);
    }

    private void logToSystem(LogEntry entry) {
        systemLog.add(entry);
    }

    private void logToUserHistory(String userId, LogEntry entry) {
        userLogs.computeIfAbsent(userId, k -> new ArrayList<>()).add(entry);
    }

    public List<String> getLogsByUser(String userId) {
        List<String> result = new ArrayList<>();
        if (userLogs.containsKey(userId)) {
            for (LogEntry entry : userLogs.get(userId)) {
                result.add(entry.toString());
            }
        }
        return result;
    }

    public String generateDailyReport(LocalDateTime date) {
        long entries = systemLog.stream()
                .filter(e -> e.getTime().toLocalDate().equals(date.toLocalDate()))
                .filter(LogEntry::isEntry)
                .count();

        long exits = systemLog.stream()
                .filter(e -> e.getTime().toLocalDate().equals(date.toLocalDate()))
                .filter(e -> !e.isEntry())
                .count();

        return String.format(
                "Daily Parking Report for %s\n" +
                        "Total Entries: %d\n" +
                        "Total Exits: %d\n" +
                        "Current Occupancy: %d",
                date.toLocalDate(),
                entries,
                exits,
                entries - exits
        );
    }

    private static class LogEntry {
        private final String userId;
        private final LocalDateTime time;
        private final boolean isEntry;

        public LogEntry(String userId, LocalDateTime time, boolean isEntry) {
            this.userId = userId;
            this.time = time;
            this.isEntry = isEntry;
        }

        public String getUserId() { return userId; }
        public LocalDateTime getTime() { return time; }
        public boolean isEntry() { return isEntry; }

        @Override
        public String toString() {
            return String.format("%s - %s: %s",
                    time.toString(),
                    isEntry ? "ENTRY" : "EXIT",
                    userId);
        }
    }
}