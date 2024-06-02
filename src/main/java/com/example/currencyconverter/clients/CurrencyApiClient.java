package com.example.currencyconverter.clients;

import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.web.HttpHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Client providing methods for calling <a href="https://getgeoapi.com/">https://getgeoapi.com/</a> APIs.
 *
 * @author Ivaylo Zahariev
 */
public class CurrencyApiClient {

    @Value("${getgeoapi.api.key}")
    private String apiKey;

    /**
     * Converts the given amount from one currency to another
     * @param from
     *  The initial currency
     * @param to
     *  The wanted currency
     * @param amount
     *  The amount to be converted
     * @return
     *  Converted value
     */
    public double convert(Currency from, Currency to, double amount) {
        Map<String, String> parameters = getParametersMap();
        parameters.put("from", from.name());
        parameters.put("to", to.name());
        parameters.put("amount", String.valueOf(amount));
        try {
            JSONObject jsonObject = new JSONObject(
                    HttpHelper.sendRequest(
                                    createUri("/convert"),
                                    HttpMethod.GET,
                                    parameters,
                                    null, null)
                            .body());
            return jsonObject.getJSONObject("rates").getJSONObject(to.name()).getDouble("rate_for_amount");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String createUri(String uriExtension) {
        return "https://api.getgeoapi.com/v2/currency" + uriExtension;
    }

    private Map<String, String> getParametersMap() {
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.put("api_key", apiKey);
        parametersMap.put("format", "json");

        return parametersMap;
    }
}
