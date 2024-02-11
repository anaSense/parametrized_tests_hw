package tests;

import com.codeborne.selenide.Selenide;
import data.University;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.codeborne.selenide.Condition.href;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class HeadHunterUniversityRatingTests {

    @BeforeEach
    void setUp(){
        open("https://hh.ru/rating");
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWindow();
    }

    @Tag("SMOKE")
    @ParameterizedTest(name = "Университет находится на верном месте в рейтинге")
    @EnumSource(University.class)
    void checkUniversityOnRightPlaceInRating(University university) {
        chooseMoscowRatingAndCategory(university.category);
        $(".content__rating").$$(".card__rating")
                .get(university.ratingPlace-1).$(".name__university")
                .shouldHave(text(university.fullname));
    }

    @Tag("LOW")
    @ParameterizedTest(name = "Для университета указана верная ссылка")
    @EnumSource(University.class)
    void checkUniversityLinkIsCorrect(University university) {
        chooseMoscowRatingAndCategory(university.category);
        $(".content__rating").$$(".card__rating")
                .get(university.ratingPlace-1).$(".card__rating a")
                .shouldHave(href(university.url));
    }

    private final void chooseMoscowRatingAndCategory(String category) {
        $(".university-button_green").click();
        Selenide.switchTo().window(1);
        $(".rating__moscow").$(byAttribute("data-faculty", category)).click();
    }
}
