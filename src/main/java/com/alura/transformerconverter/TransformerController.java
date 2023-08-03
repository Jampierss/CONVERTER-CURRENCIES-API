package com.alura.transformerconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;
import javafx.scene.control.TextField;

/**
 * Controller class for the TransformerConverter application.
 * @author Jimmy Florian
 */

public class TransformerController implements Initializable {

    @FXML
    private TextField currencySourceTextField;
    @FXML
    private TextField dollarTextField;
    @FXML
    private TextField euroTextField;
    @FXML
    private TextField yenTextField;
    @FXML
    private TextField sterlingTextField;
    @FXML
    private TextField koreanTextField;

    // List of currency codes
    final private String[] currency = {"PEN", "USD", "EUR", "GBP", "JPY", "KRW"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up listeners for exchange rate fields
        setExchangeRateListeners();
        // Set up listeners for clearing text fields on mouse click
        setClearFieldsListeners();
    }

    private void setExchangeRateListeners() {
        // Create a list of text fields for exchange rates
        List<TextField> textFields = Arrays.asList(
                currencySourceTextField, dollarTextField, euroTextField,
                yenTextField, sterlingTextField, koreanTextField
        );

        // Set a key typed event listener for each text field
        for (int i=0; i<textFields.size(); i++) {
            int finalIndex = i;
            textFields.get(i).setOnKeyTyped(event -> ExchangeRateConverter(textFields.get(finalIndex), finalIndex));
        }
    }

    private void setClearFieldsListeners() {
        // Create a list of text fields for exchange rates
        List<TextField> textFields = Arrays.asList(
                currencySourceTextField, dollarTextField, euroTextField,
                yenTextField, sterlingTextField, koreanTextField
        );

        // Set a mouse clicked event listener for clearing each text field
        for (TextField textField : textFields) {
            textField.setOnMouseClicked(event -> clearAllTextFields(textFields.toArray(new TextField[0])));
        }
    }

    public void ExchangeRateConverter(TextField current, int index) {

        // Parse the amount from the current text field
        double amountSource = Double.parseDouble(current.getText());
        List<Double> list_rates = null;

        try {
            // Retrieve exchange rates for the selected currencies
            list_rates = ExchangeRate(currency[index]);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        // Create a map to associate a text field with its index
        Map<TextField, Integer> textFieldMap = new HashMap<>();
        textFieldMap.put(currencySourceTextField, 0);
        textFieldMap.put(dollarTextField, 1);
        textFieldMap.put(euroTextField, 2);
        textFieldMap.put(sterlingTextField, 3);
        textFieldMap.put(yenTextField, 4);
        textFieldMap.put(koreanTextField, 5);

        // Update all text fields with their new exchange rate
        for (Map.Entry<TextField, Integer> entry : textFieldMap.entrySet()) {
            TextField textField = entry.getKey();

            if (textField != current) { // Avoid updating the currently edited text field
                int textFieldIndex = entry.getValue();
                double result = amountSource * list_rates.get(textFieldIndex);
                result = (double) Math.round(result * 100) / 100;

                textField.setText(String.valueOf(result));
            }
        }

    }

    public List<Double> ExchangeRate (String symbolCurr) throws URISyntaxException, IOException {
        List<Double> ratesList = new ArrayList<>();

        // Setting URL of ExchangeRate-API
        String url_str = "https://v6.exchangerate-api.com/v6/335483cb06a442d108fc898d/latest/" + symbolCurr;

        try {
            // Making Request
            URI uri = new URI(url_str);
            URL url = uri.toURL();

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Create a Gson instance
            Gson gson = new Gson();
            JsonElement root = gson.fromJson(new InputStreamReader((InputStream) request.getContent()), JsonElement.class);
            JsonObject jsonobj = root.getAsJsonObject();

            // Accessing object
            String req_result = jsonobj.get("result").getAsString();

            if (req_result.equals("success")) {
                JsonObject conversionRates = jsonobj.getAsJsonObject("conversion_rates");
                for (String currencyCode : currency) {
                    JsonElement rateElement = conversionRates.get(currencyCode);
                    if (rateElement != null) {
                        double rate = rateElement.getAsDouble();
                        ratesList.add(rate);
                    }
                }
            }
        } catch (URISyntaxException | IOException exception) {
            exception.printStackTrace();
        }

        if (ratesList.isEmpty()) {
            throw new IllegalArgumentException("No exchange rates found for currencies.");
        }
        System.out.println(ratesList);
        return ratesList;
    }

    private void clearAllTextFields(TextField... textFields) {
        // Clear all text fields
        for (TextField textField : textFields) {
            textField.setText("");
        }
    }

}