package Stonksgenerator;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


import java.io.FileNotFoundException;


public class GUI extends Application{
    Trading trader = new Trading();
    ArrayList<String> stonks = new ArrayList<String>();

    @Override
    public void start(Stage s) throws IOException, JSONException, SQLException, InterruptedException {

        //trader.CreateSTM();
        readFile();
        tradingseries();
        drawaktienverlauf(stonks);
        endtheme();
        /*for(int i = 0; i<stonks.size();i++) {
            String symbol = stonks.get(i);
            System.out.println(symbol);
            if (!check(symbol)) {
                trader.GetCloseValues(symbol);
                trader.UseSTM();
                trader.CreateTable(symbol);
                trader.InsertStatementClose(symbol);
                trader.getSplit(symbol);
                trader.selectInsertSplit(symbol);
                trader.split(symbol);
                trader.update(symbol);
                trader.SelectAVGStatement(symbol);
                trader.InsertStatementAvg(symbol);
                trader.createTradingTable(symbol);
                trader.fillDateTradeList(symbol, LocalDate.now());
                trader.trading200(symbol);
                trader.buyandHold(symbol);
                trader.trading200With3(symbol);
                trader.ListNull();
                trader.selectAll(symbol);

                // JAVAFX:
                try {
                    final CategoryAxis xAxis = new CategoryAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    yAxis.setAutoRanging(false);


                    yAxis.setLowerBound(trader.getLowerBound(symbol));
                    yAxis.setUpperBound(trader.getUpperBound(symbol));

                    xAxis.setLabel("date");
                    yAxis.setLabel("close-value");
                    final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
                    lineChart.setTitle("stock-price " + symbol);
                    XYChart.Series<String, Number> closeStat = new XYChart.Series();
                    closeStat.setName("close-value");
                    for (int k = 0; k < trader.arrayListClose.size(); k++) {
                        closeStat.getData().add(new XYChart.Data(trader.dateDB.get(k), trader.closeDB.get(k)));
                    }
                    XYChart.Series<String, Number> averageStat = new XYChart.Series();
                    averageStat.setName("moving average");
                    for (int j = 1; j < trader.arrayListAVG.size() - 1; j++) {
                        averageStat.getData().add(new XYChart.Data(trader.dateDB.get(j), trader.avgDB.get(j)));
                    }

                    Scene scene = new Scene(lineChart, 1080, 720);
                    lineChart.getData().add(closeStat);
                    lineChart.getData().add(averageStat);

                    lineChart.setCreateSymbols(false);
                    s.setScene(scene);
                    s.show();
                    saveAsPng(lineChart, "C:\\Users\\toalba\\Desktop\\schule\\StonksgeneratorV1\\src\\Stonksgenerator/img/chart-" + symbol +  "-stocks.png");
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    trader.startdate(LocalDate.of(2019,01,01));
                    trader.selectAll(symbol);
                    trader.SelectAVGStatement(symbol);
                    Thread.sleep(500);
                    trader.fillDateTradeList(symbol,LocalDate.now().minusDays(1));
                    Thread.sleep(500);
                    trader.trading200(symbol);
                    trader.buyandHold(symbol);
                    trader.trading200With3(symbol);
                    trader.ListNull();
                    trader.startm=100000;
                }catch (Exception e)
                {
                    System.out.println(e.toString());
                }

            }
        }
       drawaktienverlauf(stonks);*/
    }
    void readFile() throws FileNotFoundException
    {
        Scanner reader = new Scanner(new File ("src\\Stonksgenerator\\Aktien.txt"));
        while(reader.hasNextLine())
        {
            stonks.add(reader.nextLine());
        }
    }
    public static boolean check (String symbol)
    {
        File file = new File ("src\\Stonksgenerator/img/chart-" + symbol +  "-stocks.png");
        return file.exists();
    }
    public void saveAsPng(LineChart lineChart, String path) {
        WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void drawaktienverlauf(ArrayList<String> symbols)
    {
        Stage s = new Stage();

        try {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final NumberAxis yAxis2 = new NumberAxis();
            xAxis.setLabel("date");
            yAxis.setLabel("count");
            yAxis2.setLabel("money");
            final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
            final LineChart<String, Number> lineChart2 = new LineChart<String, Number>(xAxis, yAxis2);
            lineChart.setTitle("Tradingsimulation - Aktien");
            lineChart2.setTitle("Tradingsimulation - Money");
            for (String symbol:symbols) {
                XYChart.Series<String, Number> tradecountStat = new XYChart.Series();
                XYChart.Series<String, Number> trademoneyStat = new XYChart.Series();
                ArrayList<Tradetable> tradetables= trader.getTradetablebysymbol(symbol,"trading");
                tradecountStat.setName(symbol);
                trademoneyStat.setName(symbol);
                for (Tradetable tradetable:tradetables) {
                    tradecountStat.getData().add(new XYChart.Data(tradetable.date,tradetable.count));
                    trademoneyStat.getData().add(new XYChart.Data(tradetable.date,tradetable.money));
                }
                lineChart.getData().add(tradecountStat);
                lineChart2.getData().add(trademoneyStat);
            }
            Scene scene = new Scene(lineChart, 1080, 720);
            Scene scene1 = new Scene(lineChart2, 1080, 720);
            lineChart.setCreateSymbols(false);
            s.setScene(scene);
            s.show();
            Thread.sleep(100);
            saveAsPng(lineChart, "src\\Stonksgenerator/img/chart-" + LocalDate.now() +"-trading-count.png");
            s.setScene(scene1);
            s.show();
            Thread.sleep(100);
            saveAsPng(lineChart2, "src\\Stonksgenerator/img/chart-" + LocalDate.now() +  "-trading-money.png");
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void tradingseries() throws FileNotFoundException, SQLException, InterruptedException {
        int a = stonks.size();
        int money = 100000;
        for (String symbol:stonks) {
            Trading tr = new Trading();
            tr.droptradingtabelsbySymbol(symbol);
            tr.createTradingTable(symbol);
            tr.startm=money/a;
            tr.startdate(LocalDate.of(2016,1,1));
            tr.split(symbol);
            tr.fillDateTradeList(symbol,LocalDate.now().minusDays(1));
            Thread.sleep(500);
            tr.trading200(symbol);
            tr.ListNull();
        }

    }
    public void endtheme() throws MalformedURLException {
        Stage stage = new Stage();
        WebView webview = new WebView();
        webview.getEngine().load(
                "https://streamable.com/e/7fo13u?autoplay=1"
        );
        webview.setPrefSize(Screen.getPrimary().getBounds().getMaxX(), Screen.getPrimary().getBounds().getMaxY());

        stage.setScene(new Scene(webview));
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
