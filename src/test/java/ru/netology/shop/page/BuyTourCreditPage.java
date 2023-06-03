package ru.netology.shop.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static ru.netology.shop.page.ElementsTexts.Headings;

public class BuyTourCreditPage extends BuyTourPage {

    public BuyTourCreditPage() {
        heading.shouldBe(visible)
                .shouldHave(text(Headings.CREDIT));
    }
}