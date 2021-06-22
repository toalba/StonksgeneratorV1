package Stonksgenerator;

import java.sql.Connection;
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

    private static ArrayList<Closevalue> close_;
    private static ArrayList<Avg> avg_;
    private double bank;
    private double depot;

    public void trading200()
    {
        for (Closevalue v: close_)
        {
            if (v.close>avg_.stream().filter(x->x.Date==v.Date).findFirst().get().avg)
            {
                if(v.Date.getDayOfWeek().equals(DayOfWeek.SATURDAY)||v.Date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
                {
                    double a = Math.floor(bank/v.close);

                }
            }
            if (v.close<avg_.stream().filter(x->x.Date==v.Date).findFirst().get().avg)
            {
                if(v.Date.getDayOfWeek().equals(DayOfWeek.SATURDAY)||v.Date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
                {
                    //sell
                }
            }
        }
    }
}
