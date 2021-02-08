package kz.proffix4.telegrambot2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.json.JSONObject;

public class Utils {

    // Шифрование строки
    public static String encode(String text) {
        byte[] encoded = Base64.getEncoder().encode(text.getBytes());
        return new String(encoded);

    }

    // Расшифровка строки
    public static String decode(String text) {
        String result = new String();
        try {
            byte[] decoded = Base64.getDecoder().decode(text);
            return new String(decoded);
        } catch (Exception ex) {
            return "Ошибка расшифровки";
        }

    }

    // Получение значения биткоина через Http и библиотеку JSON
    public static String getBTC() {
        HttpURLConnection connection = null;
        String coin_url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        String query = coin_url;
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                System.out.println(response);
                StringBuilder data = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.append(line);
                    }
                } catch (IOException e) {
                }
                JSONObject obj = new JSONObject(data.toString());
                // USD
                JSONObject bpi = (JSONObject) obj.get("bpi");
                JSONObject USD = (JSONObject) bpi.get("USD");
                String usd = USD.get("rate").toString();
                // GBP
                JSONObject GBP = (JSONObject) bpi.get("GBP");
                String gbp = GBP.get("rate").toString();
                //EUR
                JSONObject EUR = (JSONObject) bpi.get("EUR");
                String eur = EUR.get("rate").toString();
                String bitcoin_currency = "1 Bitcoin=\nUSD: " + usd + ";\nGBP: " + gbp + ";\nEUR: " + eur;
                return bitcoin_currency;
            } else {
                return "Error";
            }
        } catch (Exception ignored) {
        } finally {
            connection.disconnect();
        }
        return "Error";
    }

}
