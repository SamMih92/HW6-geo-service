import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizationServiceImplTest {

    @DisplayName("Тестируем язык приветствия для заданной локализации")
    @ParameterizedTest(name = "Test locale for country: {0}")
    @EnumSource(Country.class)
    void testLocale(Country country) {

        // ARRANGE
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();

        // ACT
        String actual = localizationService.locale(country);

        // ASSERT
        String expected = switch (country) {
            case RUSSIA -> "Добро пожаловать";
            default -> "Welcome";
        };
        assertEquals(expected, actual, "Cообщение для: " + country);
    }
}
