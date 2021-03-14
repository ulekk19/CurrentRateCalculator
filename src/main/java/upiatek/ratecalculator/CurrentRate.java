package upiatek.ratecalculator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class CurrentRate {

    Double rate;

    public JSONObject getJsonFromURL(String url) throws IOException {
        //get url and convert its components to json object
        InputStream inputStream = new URL(url).openStream();
        JSONObject json = new JSONObject();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder stringBuilder = new StringBuilder();
            int cp;
            while ((cp = bufferedReader.read()) != -1) {
                stringBuilder.append((char) cp);
            }
            String jsonText = stringBuilder.toString();
            json = new JSONObject(jsonText);
            inputStream.close();
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return json file to work with
        return json;
    }

    public void setRate(String url) throws IOException, JSONException {
        //get specific rate from json object
        JSONObject jsonObject = getJsonFromURL(url);
        this.rate = jsonObject.getJSONArray("rates").getJSONObject(0).getDouble("mid");
    }

    public Double calculate(int choice, String amount) {
        //calculate rate depending on choice
        //1st is BGP to PLN, 2nd is PLN to GBP
        Double amountToCalculate = 0.0, calculatedAmount = 0.0;
        amountToCalculate = Double.parseDouble(amount);

        //throws exception when user gives negative number
        if (amountToCalculate < 0) throw new NumberFormatException();

        if (rate!=0.0) {
            if (choice == 1) {
                calculatedAmount = rate * amountToCalculate;
            } else if (choice == 2) {
                calculatedAmount = amountToCalculate / rate;
            } else calculatedAmount = -1.0;
        }

        return calculatedAmount;
    }
}
