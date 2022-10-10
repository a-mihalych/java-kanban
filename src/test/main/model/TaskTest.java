package model;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void getStartTimeLine() {
        Task task = new Task(1, "Задача", "Тест", Status.NEW,
                             LocalDateTime.of(2022, 2, 20, 22, 20), 99);
        assertEquals("20.02.2022 22:20", task.getStartTimeLine(), "Время не совпало.");
    }

    @Test
    public void getDurationHoursMinutesLine() {
        Task task = new Task(1, "Задача", "Тест", Status.NEW,
                             LocalDateTime.of(2022, 2, 20, 22, 20), 99);
        assertEquals("1:39", task.getDurationHoursMinutesLine(), "Продолжительность не совпала.");
    }

    @Test
    public void getEndTime() {
        Task task = new Task(1, "Задача", "Тест", Status.NEW,
                             LocalDateTime.of(2022, 2, 20, 22, 20), 99);
        LocalDateTime endTime = LocalDateTime.of(2022, 2, 20, 23, 59);
        assertEquals(endTime, task.getEndTime(), "Время не совпало.");
    }

    @Test
    public void getEndTimeLine() {
        Task task = new Task(1, "Задача", "Тест", Status.NEW,
                             LocalDateTime.of(2022, 2, 20, 22, 20), 99);
        assertEquals("20.02.2022 23:59", task.getEndTimeLine(), "Время не совпало.");
    }

    @Test
    public void isIntersectionTasks() {
        Task task1 = new Task(1, "Задача1", "Тест", Status.NEW, null, 0);
        Task task2 = new Task(2, "Задача2", "Тест", Status.NEW, null, 0);
        assertFalse(task1.isIntersectionTasks(task2));
        LocalDateTime dateTime1 = LocalDateTime.of(2022, 2, 20, 22, 20);
        LocalDateTime dateTime2 = LocalDateTime.of(2022, 2, 20, 22, 10);
        task1.setStartTime(dateTime1);
        assertFalse(task1.isIntersectionTasks(task2));
        task1.setStartTime(null);
        task1.setDuration(99);
        assertFalse(task1.isIntersectionTasks(task2));
        task1.setStartTime(dateTime1);
        task2.setDuration(99);
        assertFalse(task1.isIntersectionTasks(task2));
        task2.setDuration(0);
        task2.setStartTime(dateTime2);
        assertFalse(task1.isIntersectionTasks(task2));
        task2.setStartTime(dateTime2.plus(Duration.ofMinutes(50)));
        assertFalse(task1.isIntersectionTasks(task2));
        task2.setDuration(55);
        assertTrue(task1.isIntersectionTasks(task2));
        task2.setStartTime(dateTime2.minus(Duration.ofMinutes(105)));
        assertFalse(task1.isIntersectionTasks(task2));
        task2.setStartTime(dateTime2.plus(Duration.ofMinutes(155)));
        assertFalse(task1.isIntersectionTasks(task2));
    }
}
