import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageSenderImplTest {

    @DisplayName("Проверка возвращаемого сообщения с валидным IP")
    @Test
    void testSend_withValidIp_returnsLocalizedMessage() {

        // ARRANGE
        GeoService geoService = Mockito.mock(GeoService.class);
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        String ip = "172.123.12.19";
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        Location location = new Location("Moscow", Country.RUSSIA, "Leningradskaya street", 9);

        Mockito.when(geoService.byIp(ip)).thenReturn(location);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        // ACT
        String result = messageSender.send(headers);

        // ASSERT
        Mockito.verify(geoService).byIp(ip);
        verify(localizationService, times(2)).locale(Country.RUSSIA);
        assertEquals("Добро пожаловать", result);
    }

    @DisplayName("Проверка возвращаемого сообщения с невалидным IP")
    @Test
    void testSend_withNoIp_returnsDefaultMessage() {

        // ARRANGE
        GeoService geoService = Mockito.mock(GeoService.class);
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "");

        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        // ACT
        String result = messageSender.send(headers);

        // ASSERT
        Mockito.verify(localizationService).locale(Country.USA);
        Mockito.verifyNoInteractions(geoService);
        assertEquals("Welcome", result);
    }
}
