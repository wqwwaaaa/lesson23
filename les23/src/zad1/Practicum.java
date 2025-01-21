package zad1;

import java.time.Instant;

import static java.time.Instant.ofEpochMilli;

public class Practicum {
    public static void main(String[] args) {
        long millis = 9_000_000_000L; // количество миллисекунд
        Instant afterMill = Instant.ofEpochMilli(millis);
        Instant fromMill = Instant.ofEpochMilli(-millis);
        System.out.println(afterMill); // дата millis миллисекунд после Unix-эпохи
        System.out.println(fromMill); // дата millis миллисекунд до Unix-эпохи
    }
}