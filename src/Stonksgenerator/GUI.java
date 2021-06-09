package Stonksgenerator;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.json.JSONException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GUI extends Application{
    Trading trader = new Trading();
    ArrayList<String> stonks = new ArrayList<String>();

    @Override
    public void start(Stage s) throws IOException, JSONException, SQLException {
        /*DB Klasse, WebRequest - Abfrage vom Symbol*/

        //trader.CreateSTM();
        readFile();
        for(int i = 0; i<stonks.size();i++) {
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
                trader.fillDateTradeList(symbol);
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
                    saveAsPng(lineChart, "/home/toalba/Java2/StonksgeneratorV1/src/Stonksgenerator/img/chart-" + symbol +  "-stocks.png");
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    trader.selectAll(symbol);
                    trader.fillDateTradeList(symbol);
                    trader.trading200(symbol);
                    trader.buyandHold(symbol);
                    trader.trading200With3(symbol);
                    trader.ListNull();
                }catch (Exception e)
                {
                    System.out.println(e.toString());
                }

            }
        }
    }
    void readFile() throws FileNotFoundException
    {
        Scanner reader = new Scanner(new File ("/home/toalba/Java2/StonksgeneratorV1/src/Stonksgenerator/Aktien.txt"));
        while(reader.hasNextLine())
        {
            stonks.add(reader.nextLine());
        }
    }
    public static boolean check (String symbol)
    {
        File file = new File ("/home/toalba/Java2/StonksgeneratorV1/src/Stonksgenerator/img/chart-" + symbol +  "-stocks.png");
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
    public static void main(String args[]){
        launch(args);
    }
}
