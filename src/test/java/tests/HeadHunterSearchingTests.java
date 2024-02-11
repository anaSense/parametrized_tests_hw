package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HeadHunterSearchingTests extends TestBase {

    @BeforeEach
    void setUp() {
        open("https://hh.ru/");
    }

    @Tag("SMOKE")
    @ParameterizedTest(name = "При выборе города {0} и последующем поиске " +
            "- первая вакансия в поисковой выдаче из города {0}")
    @ValueSource(strings = {
            "Воронеж",
            "Алматы"
    })
    void chooseTownForJobSearchSuccessfully(String town) {
        sleep(1000);

        $("#a11y-search-input").click();
        $("[data-qa=mainmenu_areaSwitcher]").click();
        $("[data-qa=area-switcher-welcome]").shouldBe(visible);
        $$(".area-switcher-city").findBy(text(town)).$("a").click();
        $("[data-qa=search-button]").click();
        $$(".serp-item").first().$("[data-qa=vacancy-serp__vacancy-address]")
                .shouldHave(text(town));
    }

    @Tag("SMOKE")
    @ParameterizedTest(name = "При расширенном поиске вакансии по ключевому слову {0}, " +
            "региону {1} и уровню дохода {2}" +
            "- первая вакансия в поисковой выдачи удовлетворяет всем критериям")
    @CsvFileSource(resources = "/test_data/advanceSearchVacancyByKeywordRegionSalary.csv")
    void advanceSearchVacancyByKeywordRegionSalary(String keyword, String region, int salary) {
        $("[data-qa=advanced-search]")
                .shouldBe(interactable, Duration.ofSeconds(30)).click();
        sleep(5000);
        $("[data-qa=vacancysearch__keywords-input]")
                .shouldBe(interactable, Duration.ofSeconds(30)).setValue(keyword);
        $("[data-qa=bloko-suggest-list] li")
                .shouldBe(visible, Duration.ofSeconds(30)).click();
        $("[data-qa=advanced-filter]").$$(".bloko-form-item-baseline")
                .first().$("label").click();
        $("[data-qa=bloko-tag__cross]").scrollTo().click();
        $("[data-qa=advanced-search-region-add]").scrollTo()
                .shouldBe(interactable, Duration.ofSeconds(30)).setValue(region);
        sleep(2000);
        $("[data-qa=advanced-search-salary]").scrollTo()
                .setValue(String.valueOf(salary)).pressEnter();
        $$(".serp-item").first().$("[data-qa=serp-item__title]")
                .shouldHave(text(keyword));
        $$(".serp-item").first().$("[data-qa=vacancy-serp__vacancy-address]")
                .shouldHave(text(region));
        String salaryString = $$(".serp-item").first().$("[data-qa=vacancy-serp__vacancy-compensation]").getText();
        Integer maxSalary = getMaxSalaryFromString(salaryString);
        assert salary <= maxSalary  : "Salary is out or less than was indicated in the search";
    }

    private Integer getMaxSalaryFromString(String salaryString) {
        salaryString = salaryString.replaceAll("[^\\d–]", "");
        String[] result = salaryString.split("–", 0);
        return Integer.valueOf(result[result.length-1]);
    }
}


