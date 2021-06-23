package Stonksgenerator;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Simulation {

    public static Connection con;
    private static String hostname = "localhost";
    private static String dbName = "Aktien";
    private String dBPort = "3306";
    private static String userName = "root";
    private static String password = "wa22er!wasser";

    public LocalDate startdate;
    public LocalDate enddate;
    public String symbol_;

    private static ArrayList<Closevalue> close_ = new ArrayList<>();
    private static ArrayList<Avg> avg_ = new ArrayList<>();
    private double bank=10000;
    private double depot;
    public ArrayList<Totalcount> totalcount =  new ArrayList<>();

    private void opencon() throws SQLException {
        con=DriverManager.getConnection("jdbc:mysql://"+hostname+"/"+dbName+"?user="+userName+"&password="+password);
    }
    public void fillbank(double bank)
    {
        this.bank=bank;
    }

    public void getWerte_()
    {
        String sql = "select datum,close from " + symbol_ + " where datum between \'" + startdate + "\' AND \'" + enddate + "\' order by datum ASC ;";
        String sqlAvg = "select AVERAGE from " + symbol_ + "avg where datum between \'" + startdate + "\' " + "AND \'" + enddate + "\' order by datum ASC;";
        try {

            opencon();
            Statement stm = con.createStatement();
            ResultSet rsc = stm.executeQuery(sql);
            ResultSet rsa = stm.executeQuery(sqlAvg);
            while (rsc.next()&&rsa.next())
            {
                Closevalue c = new Closevalue();
                Avg a = new Avg();
                c.close = rsc.getDouble("close");
                c.Date = LocalDate.parse(rsc.getString("datum"));
                a.avg = rsa.getDouble("AVERAGE");
                a.Date = LocalDate.parse(rsc.getString("datum"));
                close_.add(c);
                avg_.add(a);
            }
            con.close();

        }catch (SQLException e)
        {
            e.toString();
        }
    }
    public void trading203() {
        getWerte_();
        for (Closevalue v: close_)
        {
            if (v.close>avg_.stream().filter((x)->x.Date.equals(v.Date)).findFirst().get().avg*1.03)
            {
                if(bank>10)
                {
                    double a = Math.floor(bank/v.close);
                    depot = depot+a;
                    bank = bank-a*v.close;
                    System.out.println("trade for depot"+depot+" "+v.Date);
                }
            }
            if (v.close<avg_.stream().filter((x)->x.Date.equals(v.Date)).findFirst().get().avg*1.03)
            {
                if(depot>=1)
                {
                    double a = depot*v.close;
                    System.out.println("A: "+a+" depot "+depot+" "+v.Date);
                    bank = bank+a;
                    depot=0;
                    System.out.println("trade for bank"+bank+" "+v.Date);
                }
            }
            Totalcount todaysvalue = new Totalcount();
            todaysvalue.Date=v.Date;
            System.out.println(depot*v.close+bank);
            todaysvalue.value=depot*v.close+bank;
            totalcount.add(todaysvalue);
        }
        inserttotalcount("_203");

    }


    public void trading200() {
        getWerte_();
        for (Closevalue v: close_)
        {
           if (v.close>avg_.stream().filter((x)->x.Date.equals(v.Date)).findFirst().get().avg)
            {
                if(bank>10)
                {
                    double a = Math.floor(bank/v.close);
                    depot = depot+a;
                    bank = bank-a*v.close;
                    System.out.println("trade for depot"+depot+" "+v.Date);
                }
            }
            if (v.close<avg_.stream().filter((x)->x.Date.equals(v.Date)).findFirst().get().avg)
            {
                if(depot>=1)
                {
                    double a = depot*v.close;
                    System.out.println("A: "+a+" depot "+depot+" "+v.Date);
                    bank = bank+a;
                    depot=0;
                    System.out.println("trade for bank"+bank+" "+v.Date);
                }
            }
            Totalcount todaysvalue = new Totalcount();
            todaysvalue.Date=v.Date;
            System.out.println(depot*v.close+bank);
            todaysvalue.value=depot*v.close+bank;
            totalcount.add(todaysvalue);
        }
        inserttotalcount("_200");

    }
    public void inserttotalcount(String art)
    {
        String dropTable = "drop table if exists " + symbol_+"_totalcount_"+art;
        String createTable = "CREATE TABLE IF NOT EXISTS "+symbol_+"_totalcount_"+art+" (datum DATE PRIMARY KEY unique,val DOUBLE);";
        try {
            opencon();
            Statement stm = con.createStatement();
            stm.executeQuery(dropTable);
            stm.executeQuery(createTable);
            System.out.println("Testinsertodings");
            for (Totalcount c : totalcount){
                String insert = "INSERT INTO "+symbol_+"_totalcount_"+art+" (datum,val ) values (\""+c.Date.toString()+"\","+c.value+");";
                stm.executeQuery(insert);

            }
            con.close();
        }catch (SQLException e)
        {
            e.toString();
        }
    }
    public void printgraf()
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("date");
        yAxis.setLabel("possible money");
        final LineChart<String,Number> lineChart= new LineChart<String, Number>(xAxis,yAxis);
        lineChart.setTitle("Tradingsimulation ↗️");
        XYChart.Series<String, Number> tradecountStat = new XYChart.Series();
        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        tradecountStat.setNode(rect);
        tradecountStat.setName("200er");
        for (Totalcount e:totalcount)
        {
            System.out.println("Date "+e.Date+" Value"+e.value);
            XYChart.Data data = new XYChart.Data<>(e.Date.toString(),e.value);
            data.setNode(rect);
            tradecountStat.getData().add(data);
        }
        lineChart.getData().add(tradecountStat);
        Stage s =new Stage();
        s.setScene(new Scene(lineChart,1080,720));
        s.show();
        GUI.saveAsPng(lineChart,"src/Stonksgenerator/img/chart-" + symbol_ + LocalDate.now().toString()+"-trading.png");
        s.close();
        return;
    }

}
