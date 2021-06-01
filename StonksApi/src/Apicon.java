import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Apicon {

    // api key 1AD6CE6LV8OFT02F
    private final String key="&apikey=1AD6CE6LV8OFT02F";
    private String requestString ="https://www.alphavantage.co/query";
    private String function = "?function=";
    private String symbol = "&symbol=";

    private JSONObject WebRequest(String urlstring) {
        HttpURLConnection connection;
        try {
            URL url = new URL(urlstring);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            //Send request
            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                  StringBuilder response = new StringBuilder();
                  String responseLine;
                  while ((responseLine = br.readLine()) != null) {
                      response.append(responseLine.trim());
                  }
                  return new JSONObject(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject Requestbuilder(String aktie)
    {
        String url=requestString+function+"TIME_SERIES_DAILY&outputsize=full"+symbol+aktie+key;
        return WebRequest(url);
    }

    public JSONObject Webrequestsucher(String keyword) {
        HttpURLConnection connection;
        String urlstring="https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="+keyword+key;
        try {
            URL url = new URL(urlstring);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            //Send request
            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                return new JSONObject(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Datasheet> DataSheetResult(JSONObject json)
    {
        ArrayList<Datasheet> arrayList = new ArrayList<>();
        try
        {
            JSONObject info = json.getJSONObject("Meta Data");
            String symbol = (String)info.get("2. Symbol");
            System.out.println(symbol);
            DB.CreateTable(symbol);
            JSONObject result = json.getJSONObject("Time Series (Daily)");
            for(int i = 0; i < result.length(); i++)
            {
                String datum = result.names().get(i).toString();
                JSONObject wert = (JSONObject) result.get(datum);
                Double kurswert = Double.parseDouble(wert.get("4. close").toString());
                arrayList.add(new Datasheet(datum,kurswert));
            }
            for (Datasheet d:arrayList) {
                DB.InsertStatement(symbol,d.Datum,d.Wert,0);
            }
            DB.con.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }


}
