import java.time.LocalDate;

public class Webrequestresult {
    String symbol;
    LocalDate time;
    double close;
    int volume;
    public Webrequestresult(String Symbol, LocalDate Time,double Close,int Volume)
    {
        this.symbol=Symbol;
        this.time =Time;
        this.close=Close;
        this.volume=Volume;
    }
}
