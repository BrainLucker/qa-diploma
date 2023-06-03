package ru.netology.shop.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static ru.netology.shop.page.ElementsTexts.Headings;

public class BuyTourPaymentPage extends BuyTourPage {

    public BuyTourPaymentPage() {
        heading.shouldBe(visible).shouldHave(text(Headings.PAYMENT));
    }
}