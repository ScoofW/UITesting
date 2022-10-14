package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryFormTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    public void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void shouldOrderCardDelivery() {

        String planningDate = generateDate(50);

        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Маркс Карл");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("span.spin").shouldBe(Condition.visible);
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);


    }

    @Test
    void shouldErrorOrderCardDelivery() {

        String planningDate = generateDate(1);

        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Маркс Карл");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id='date']")
                .shouldHave(Condition.text("Заказ на выбранную дату невозможен"));


    }
}