package zad2;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Practicum {

    // запросите у пользователя его координаты (долгота и широта) и затем
    // выведите расписание рассветов и закатов на сегодня
    // и ближайшую неделю в формате РАССВЕТ - ЗАКАТ
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите широту:");
        double userLatitude = scanner.nextDouble();

        System.out.println("Введите долготу: ");
        double userLongtitude = scanner.nextDouble();

        System.out.println("Введите вашу временную зону (например, для Москвы +3): ");
        int userTimezone = scanner.nextInt();

        System.out.println("Введите текущий год в формате unix (10 цифр): ");
        long startOfYearEpoch = scanner.nextLong();

        Instant startOfYearMoment = Instant.ofEpochSecond(startOfYearEpoch);

        Instant thisMoment = Instant.now();

        Instant lastMoment = thisMoment.plus(7, ChronoUnit.DAYS);

        System.out.println("Рассвет - Закат, график на неделю:");
        do {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(thisMoment, ZoneId.of("UTC").normalized());
            int day = zonedDateTime.getDayOfYear();

            // эти вычисления нужны для расчёта времени рассвета и заката,
            // но вы можете изучить их позже, когда освоите класс Instant
            int noonMinutes = localNoonMinutes(day, userTimezone, userLongtitude);
            double hourDelta = sunsetTimeHours(day, userLatitude);
            double noonHour = 12 + (noonMinutes / 60.0);
            double sunriseHour = noonHour - hourDelta;
            double sunsetHour = noonHour + hourDelta;

            System.out.printf("%s - %s\n", hhmmFromDouble(sunriseHour), hhmmFromDouble(sunsetHour));

            // эта конструкция позволит вам высчитать следующий день
            thisMoment = thisMoment.plus(1, ChronoUnit.DAYS);
        } while (thisMoment.isBefore(lastMoment)); // вам нужно вывести график на семь дней

    }

    private static int dayOfYearFromInstant(Instant startOfYear, Instant time) {
        long fromStartOfYear = time.toEpochMilli() - startOfYear.toEpochMilli();
        return (int) (fromStartOfYear / (1000 * 60 * 60 * 24));
    }

    static double sunsetTimeHours(int dayOfYear, double latitude) {
        double rad = (Math.PI / 180);
        double factor = -1 * Math.tan(rad * latitude) * Math.tan(23.44 * rad * Math.sin(rad * (360 / 365.0) * (dayOfYear + 284.0)));
        if (factor <= -1 || factor >= 1) {
            return 0.0;
        }
        return Math.abs(Math.acos(factor)) / (rad * 15);
    }

    static int equationOfTimeMinutes(int dayOfYear) {
        return (int) Math.round(-7.655 * Math.sin(2 * Math.PI * dayOfYear / 365) + 9.873 * Math.sin(4 * Math.PI * dayOfYear / 365 + 3.588));
    }

    static int localNoonMinutes(int dayOfYear, int timeZone, double longtitude) {
        return (int) Math.round(4.0 * (longtitude - (15 * (timeZone)))) - equationOfTimeMinutes(dayOfYear);
    }

    static String hhmmFromDouble(double hour) {
        return String.format("%d:%02d", (int) Math.floor(hour), (int) Math.round(60 * (hour - Math.floor(hour))));
    }
}