package ru.netology.geo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.*;

class GeoServiceImplTest {


    @DisplayName("Проверка определения локации по IP")
    @ParameterizedTest(name = "{index} => IP={0} => City={1}, Country={2}")
    @CsvSource(value = {
            "127.0.0.1, null, null",
            "172.0.32.11, Moscow, RUSSIA",
            "96.44.183.149, New York, USA",
            "172.100.50.1, Moscow, RUSSIA",
            "96.100.50.1, New York, USA"
    }, nullValues = "null")
    void byIp_ShouldReturnExpectedLocation(String ip, String expectedCity, Country expectedCountry) {

        // ARRANGE
        GeoServiceImpl geoService = new GeoServiceImpl();

        // ACT
        Location actual = geoService.byIp(ip);

        // ASSERT
        if ("127.0.0.1".equals(ip)) {
            assertNull(actual.getCountry(), "Для localhost должно быть null");
            assertNull(actual.getCity(), "Для localhost должно быть null");
        } else {
            assertNotNull(actual, "Location не должно быть null");
            assertEquals(expectedCity, actual.getCity());
            assertEquals(expectedCountry, actual.getCountry());
        }
    }

    @DisplayName("Проверка выброса исключений")
    @ParameterizedTest(name = "{index} => latitude={0}, longitude={1}")
    @CsvSource({
            "55.7558, 37.6173",   // Москва
            "40.7128, -74.0060",  // Нью-Йорк
            "0.0, 0.0"            // Нулевая точка
    })
    void byCoordinates_ShouldThrowRuntimeException(double latitude, double longitude) {

        // ARRANGE

        GeoServiceImpl geoService = new GeoServiceImpl();

        // ACT & ASSERT

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> geoService.byCoordinates(latitude, longitude),
                "Метод byCoordinates должен выбрасывать RuntimeException");
        assertEquals("Not implemented", exception.getMessage());
    }
}
