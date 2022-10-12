package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    @Test
    public void calculateDuration() {
        Epic epic = new Epic(3, "Эпик", "Тест", Status.NEW,
                             LocalDateTime.of(2022, 2, 22, 22, 20),
                             0, new ArrayList<>());
        epic.setEndTime(LocalDateTime.of(2022, 2, 22, 23, 40));
        assertEquals(0, epic.getDuration(), "Продолжительность не совпала.");
        epic.setDuration();
        assertEquals(80, epic.getDuration(), "Продолжительность не совпала.");
    }
}