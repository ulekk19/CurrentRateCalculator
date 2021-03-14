package upiatek.ratecalculator;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class CurrentRateTest {

    CurrentRate currentRate;
    private static final String PREVIOUS_RATE_URL = "http://api.nbp.pl/api/exchangerates/rates/a/gbp/2012-01-02/?format=json";
    private static final String CURRENT_RATE_URL = "http://api.nbp.pl/api/exchangerates/rates/a/gbp/?format=json";
    private static final String INVALID_RATES_URL = "http://api.nbp.pl/api/cenyzlota/?format=json";

    @BeforeEach
    void setup() {
        currentRate = new CurrentRate();
    }

    @Test
    void notNullJsonFromURL() throws IOException {
        assertNotNull(currentRate.getJsonFromURL(CURRENT_RATE_URL));
    }

    @Test
    void setRateCorrectResult() throws IOException, JSONException {
        currentRate.setRate(PREVIOUS_RATE_URL);
        assertEquals(5.3480, currentRate.getRate());
    }

    @Test
    void setRateException() {
        Exception exception = assertThrows(JSONException.class, () -> currentRate.setRate(INVALID_RATES_URL));
        String expectedMessage = "No value for rates";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void calculateGBPtoPLNCorrect() throws IOException, JSONException {
        currentRate.setRate(PREVIOUS_RATE_URL);
        assertEquals(106.96, currentRate.calculate(1, 20.0));
    }

    @Test
    void calculatePLNtoGBPCorrect() throws IOException, JSONException {
        currentRate.setRate(PREVIOUS_RATE_URL);
        assertEquals(3.7397157816005984, currentRate.calculate(2, 20.0));
    }

    @Test
    void calculateGBPtoPLNIncorrect() throws IOException, JSONException {
        currentRate.setRate(PREVIOUS_RATE_URL);
        assertEquals(-1.0, currentRate.calculate(0, 2.0));
    }

    @Test
    void calculatePLNtoGBPIncorrect() throws IOException, JSONException {
        currentRate.setRate(PREVIOUS_RATE_URL);
        assertEquals(-1.0, currentRate.calculate(0, 1.0));
    }
}