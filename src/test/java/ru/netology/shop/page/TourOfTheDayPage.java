package ru.netology.shop.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import static ru.netology.shop.page.ElementsTexts.Headings;
import static ru.netology.shop.page.ElementsTexts.Buttons;

public class TourOfTheDayPage {
    private final SelenideElement heading = $("div[id=root] > div > h2.heading");
    private final ElementsCollection buttons = $$(".button .button__text");
    private final SelenideElement StringWithPrice = $$("ul li.list__item").findBy(text("руб."));

    public TourOfTheDayPage() {
        heading.shouldBe(visible).shouldHave(text(Headings.tourOfTheDay));
    }

    public void pressButton(String text) {
        buttons.findBy(text(text)).click();
    }

    public BuyTourPaymentPage clickBuy() {
        pressButton(Buttons.buy);
        return new BuyTourPaymentPage();
    }

    public BuyTourCreditPage clickCredit() {
        pressButton(Buttons.credit);
        return new BuyTourCreditPage();
    }

    public int getTourPrice() {
        var text = StringWithPrice.text().split("Всего")[1];
        var price = text.substring(0, text.indexOf("руб.")).replaceAll("\\s","");
        return Integer.parseInt(price);
    }
}