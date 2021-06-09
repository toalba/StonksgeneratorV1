package Stonksgenerator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Apicon {
    private final String key="&apikey=1AD6CE6LV8OFT02F";
    private String requestString = "https://www.alphavantage.co/query?";
    private String function = "function=TIME_SERIES_DAILY_ADJUSTED&";
    private String prefsymbol = "&symbol=";

    public JSONObject Request(String urlString)
    {


        HttpURLConnection con;
        try
        {
            URL url = new URL(urlString);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try(BufferedReader br = new BufferedReader((new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))))
            {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while((responseLine = br.readLine()) != null)
                {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                return new JSONObject(response.toString());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public String StringBuilder(String symbol)
    {
        String s = requestString+function+prefsymbol+symbol+key;

        return s;
    }
}
