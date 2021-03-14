package upiatek.ratecalculator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CurrentRate {

    private Double rate;

    public JSONObject getJsonFromURL(String url) throws IOException {
        //get url and convert its components to json object
        JSONObject json = new JSONObject();
        try(InputStream inputStream = new URL(url).openStream();
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(isr)) {
            StringBuilder stringBuilder = new StringBuilder();
            int cp;
            while ((cp = bufferedReader.read()) != -1) {
                stringBuilder.append((char) cp);
            }
            String jsonText = stringBuilder.toString();
            json = new JSONObject(jsonText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return json file to work with
        return json;
    }

    public Double getRate() {
        return rate;
    }


    public void setRate(String url) throws IOException, JSONException {
        //get specific rate from json object
        JSONObject jsonObject = getJsonFromURL(url);
        this.rate = jsonObject.getJSONArray("rates").getJSONObject(0).getDouble("mid");
    }

    public Double calculate(int choice, Double amount) {
        //calculate rate depending on choice
        //1st is BGP to PLN, 2nd is PLN to GBP
        Double calculatedAmount = 0.0;

        if (rate!=0.0) {
            if (choice == 1) {
                calculatedAmount = rate * amount;
            } else if (choice == 2) {
                calculatedAmount = amount / rate;
            } else calculatedAmount = -1.0;
        }

        return calculatedAmount;
    }
}
