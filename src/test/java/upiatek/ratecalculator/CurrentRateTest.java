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

    @BeforeEach
    void setup() {
        currentRate = new CurrentRate();
    }

    @Test
    void notNullJsonFromURL() throws IOException {
        assertNotNull(currentRate.getJsonFromURL("http://api.nbp.pl/api/exchangerates/rates/a/gbp/?format=json"));
    }

    @Test
    void notJsonFromURL() throws IOException {
        Exception exception = assertThrows(MalformedURLException.class, () -> currentRate.getJsonFromURL("not_url"));
        String expectedMessage = "no protocol: not_url";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void setRateCorrectResult() throws IOException, JSONException {
        currentRate.setRate("http://api.nbp.pl/api/exchangerates/rates/a/gbp/2012-01-02/?format=json");
        assertEquals(5.3480, currentRate.rate);
    }

    @Test
    void setRateException() throws IOException, JSONException {
        Exception exception = assertThrows(JSONException.class, () -> currentRate.setRate("http://api.nbp.pl/api/cenyzlota/?format=json"));
        String expectedMessage = "No value for rates";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void calculateGBPtoPLNCorrect() {
        currentRate.rate = 1.51;
        assertEquals(30.2, currentRate.calculate(1, "20"));
    }

    @Test
    void calculatePLNtoGBPCorrect() {
        currentRate.rate = 1.51;
        assertEquals(13.245033112582782, currentRate.calculate(2, "20"));
    }

    @Test
    void calculateGBPtoPLNIncorrect() {
        currentRate.rate = 11.0;
        assertEquals(-1.0, currentRate.calculate(0, "1"));
    }

    @Test
    void calculatePLNtoGBPIncorrect() {
        currentRate.rate = 11.0;
        assertEquals(-1.0, currentRate.calculate(0, "1"));
    }
}