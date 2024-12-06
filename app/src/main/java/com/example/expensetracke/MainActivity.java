package com.example.expensetracke;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText etAccountBalance, etRiskPercentage, etStopLoss;
    private Spinner spinnerAssets;
    private Button btnCalculate;
    private TextView tvResult;

    // Map to hold default pip values for assets
    private final HashMap<String, Double> assetPipValues = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAccountBalance = findViewById(R.id.etAccountBalance);
        etRiskPercentage = findViewById(R.id.etRiskPercentage);
        etStopLoss = findViewById(R.id.etStopLoss);
        spinnerAssets = findViewById(R.id.spinnerAssets);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        // Initialize default pip values
        initializePipValues();

        // Set up the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assetPipValues.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssets.setAdapter(adapter);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLotSize();
            }
        });
    }

    private void calculateLotSize() {
        try {
            // Parse user inputs
            double accountBalance = Double.parseDouble(etAccountBalance.getText().toString());
            double riskPercentage = Double.parseDouble(etRiskPercentage.getText().toString());
            double stopLossPips = Double.parseDouble(etStopLoss.getText().toString());

            // Get the selected asset and retrieve its pip value from the map
            String selectedAsset = (String) spinnerAssets.getSelectedItem();
            double pipValue = assetPipValues.get(selectedAsset);

            // Risk amount in currency
            double riskAmount = (accountBalance * riskPercentage) / 100;

            // Convert stop-loss from pips to price
            double stopLossInPrice = stopLossPips * pipValue;

            // Lot size calculation
            double lotSize = riskAmount / stopLossInPrice;

            // Loss in money if stop loss is hit
            double lossInMoney = stopLossInPrice * lotSize;

            // Determine lot type based on lot size
            String lotType = "Nano Lot"; // Default
            if (lotSize >= 1) {
                lotType = "Standard Lot";
            } else if (lotSize >= 0.1) {
                lotType = "Mini Lot";
            } else if (lotSize >= 0.01) {
                lotType = "Micro Lot";
            }

            // Display results including the lot type
            tvResult.setText(String.format("Lot Size: %.2f\nLoss in Money: %.2f\n%s", lotSize, lossInMoney, lotType));
        } catch (NumberFormatException e) {
            tvResult.setText("Please enter valid numbers in all fields.");
        }
    }

    private void initializePipValues() {
        // Major Pairs
        assetPipValues.put("EUR/USD", 10.0);
        assetPipValues.put("GBP/USD", 10.0);
        assetPipValues.put("USD/JPY", 100.0);
        assetPipValues.put("USD/CHF", 10.0);
        assetPipValues.put("AUD/USD", 10.0);
        assetPipValues.put("USD/CAD", 10.0);
        assetPipValues.put("NZD/USD", 10.0);

        // Minor Pairs
        assetPipValues.put("EUR/GBP", 10.0);
        assetPipValues.put("EUR/AUD", 10.0);
        assetPipValues.put("GBP/JPY", 100.0);
        assetPipValues.put("EUR/JPY", 100.0);
        assetPipValues.put("CHF/JPY", 100.0);
        assetPipValues.put("AUD/JPY", 100.0);
        assetPipValues.put("NZD/JPY", 100.0);

        // Exotic Pairs
        assetPipValues.put("USD/SEK", 10.0);
        assetPipValues.put("USD/NOK", 10.0);
        assetPipValues.put("USD/TRY", 10.0);
        assetPipValues.put("USD/ZAR", 10.0);
        assetPipValues.put("EUR/TRY", 10.0);
        assetPipValues.put("GBP/ZAR", 10.0);

        // Commodities
        assetPipValues.put("Gold (XAU/USD)", 0.1); // Pip value for Gold
        assetPipValues.put("Silver (XAG/USD)", 0.01); // Pip value for Silver
        assetPipValues.put("Oil (WTI)", 1.0); // Pip value for Oil
        assetPipValues.put("Brent Oil", 1.0);

        // Indices
        assetPipValues.put("US30 (Dow Jones)", 1.0);
        assetPipValues.put("SPX500 (S&P 500)", 1.0);
        assetPipValues.put("NAS100 (Nasdaq 100)", 1.0);
        assetPipValues.put("DAX30 (Germany 30)", 1.0);
        assetPipValues.put("FTSE100 (UK 100)", 1.0);

        // Cryptocurrencies
        assetPipValues.put("BTC/USD (Bitcoin)", 0.01);
        assetPipValues.put("ETH/USD (Ethereum)", 0.01);
        assetPipValues.put("LTC/USD (Litecoin)", 0.01);
        assetPipValues.put("XRP/USD (Ripple)", 0.01);
    }
}
