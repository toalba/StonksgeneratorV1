package Stonksgenerator;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TheGreateown {

    public static Connection con;
    private static String hostname = "localhost";
    private static String dbName = "Aktien";
    private String dBPort = "3306";
    private static String userName = "root";
    private static String password = "wa22er!wasser";

    private static ArrayList<Totalcount> ultimatetable = new ArrayList<>();

    public void printgraf(ArrayList<String> symbols,ArrayList<String> arten)
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("date");
        yAxis.setLabel("possible money");
        final LineChart<String,Number> lineChart= new LineChart<String, Number>(xAxis,yAxis);
        lineChart.setTitle("Tradingsimulation ↗️");
        for (String art: arten) {
            getTotalcountof(symbols,art);
            XYChart.Series<String, Number> tradecountStat = new XYChart.Series();
            tradecountStat.setName(art);
            for (Totalcount e:ultimatetable)
            {

                Rectangle rect = new Rectangle(0, 0);
                rect.setVisible(false);
                XYChart.Data data = new XYChart.Data<>(e.Date.toString(),e.value);
                data.setNode(rect);
                tradecountStat.getData().add(data);
            }
            lineChart.getData().add(tradecountStat);
            ultimatetable.clear();
        }

        Stage s =new Stage();
        s.setScene(new Scene(lineChart,1080,720));
        s.showAndWait();
        GUI.saveAsPng(lineChart,"src/Stonksgenerator/img/chart-"+ LocalDate.now().toString()+"-trading.png");
    }
    public void getTotalcountof(ArrayList<String> symbols,String art)
    {
        for (String s: symbols)
        {
            String sql = "select * from " + s+"_totalcount_"+art+";";
            ArrayList<Totalcount> to = new ArrayList<>();
            try {
                Connection con;
                con = DriverManager.getConnection("jdbc:mysql://"+hostname+"/"+dbName+"?user="+userName+"&password="+password);
                Statement stm = con.createStatement();
                ResultSet rsc = stm.executeQuery(sql);
                while (rsc.next())
                {
                    Totalcount t = new Totalcount();
                    t.value = rsc.getDouble("val");
                    t.Date = LocalDate.parse(rsc.getString("datum"));
                    if(ultimatetable.stream().filter(x->x.Date.equals(t.Date)).findFirst().isPresent())
                    {
                        ultimatetable.stream().filter(x->x.Date.equals(t.Date)).findFirst().get().value+=t.value;
                    }
                    else
                    {
                        ultimatetable.add(t);
                    }
                }
                con.close();
            }catch (SQLException e)
            {
                e.toString();
            }
        }
    }

}
