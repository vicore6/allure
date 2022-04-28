package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        val validUser = DataGenerator.Registration.generateUser("ru", "ru", 11);
        val daysToAddForFirstMeeting = 4;
        val firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        val daysToAddForSecondMeeting = 7;
        val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(secondMeetingDate);
        $(".button").click();
        $$(".button").find(exactText("Перепланировать")).click();
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }

    @Test
    void shouldIncorrectFillingCity() {
        val invalidUser = DataGenerator.Registration.generateUser("en", "ru", 11);
        $("[data-test-id='city'] input").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldIncorrectFillingDate() {
        val invalidUser = DataGenerator.Registration.generateUser("ru", "ru", 11);
        $("[data-test-id='city'] input").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(2));
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldIncorrectFillingName() {
        val invalidUser = DataGenerator.Registration.generateUser("ru", "en", 11);
        $("[data-test-id='city'] input").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldIncorrectFillingPhone() {
        val invalidUser = DataGenerator.Registration.generateUser("ru", "ru", 10);
        $("[data-test-id='city'] input").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible);
    }

    @Test
    void shouldCheckboxIsNotSelected() {
        val invalidUser = DataGenerator.Registration.generateUser("ru", "ru", 11);
        $("[data-test-id='city'] input").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldFieldCityIsEmpty() {
        val validUser = DataGenerator.Registration.generateUser("ru", "ru", 11);
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFieldDateIsEmpty() {
        val validUser = DataGenerator.Registration.generateUser("ru", "ru", 11);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(" ");
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldFieldNameIsEmpty() {
        val validUser = DataGenerator.Registration.generateUser("ru", "ru", 11);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(" ");
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldPhoneFieldIsEmpty() {
        val invalidUser = DataGenerator.Registration.generateUser("ru", "ru", 0);
        $("[data-test-id='city'] input").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(DataGenerator.generateDate(4));
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSubmittingEmptyForm() {
        val invalidUser = DataGenerator.Registration.generateUser("ru", "ru", 0);
        $("[data-test-id='city'] input").setValue(" ");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(" ");
        $("[data-test-id='name'] input").setValue(" ");
        $("[data-test-id='phone'] input").setValue(invalidUser.getPhone());
        $$(".button").find(exactText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }
}