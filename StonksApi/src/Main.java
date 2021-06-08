import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Apicon apicon = new Apicon();
        apicon.DataSheetResult(apicon.Requestbuilder("TSLA"));
        apicon.DataSheetResult(apicon.Requestbuilder("IBM"));
        apicon.DataSheetResult(apicon.Requestbuilder("AMZN"));
        apicon.DataSheetResult(apicon.Requestbuilder("AAPL"));
        apicon.DataSheetResult(apicon.Requestbuilder("ADM"));
        System.out.println("Es is raus");
    }
}
