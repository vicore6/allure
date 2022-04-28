package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now();
        LocalDate newDate = date.plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(newDate);
    }

    public static String generateCity(String locale) {
        if (locale.equals("ru")) {
            Random random = new Random();
            String[] cities = {"Москва", "Владимир", "Майкоп", "Севастополь", "Смоленск", "Волгоград"};
            int index = random.nextInt(cities.length);
            return cities[index];
        } else {
            Faker faker = new Faker(new Locale(locale));
            return faker.address().city();
        }
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    public static String generatePhone(int length) {
        Faker faker = new Faker(new Locale("ru"));
        return faker.phoneNumber().subscriberNumber(length);
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String loc, String locale, int length) {
            return new UserInfo(
                    generateCity(loc),
                    generateName(locale),
                    generatePhone(length)
            );
        }
    }

    @Value
    public static class UserInfo {
        private String city;
        private String name;
        private String phone;
    }
}