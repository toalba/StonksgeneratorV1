import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Apicon apicon = new Apicon();
        apicon.DataSheetResult(apicon.Requestbuilder("TSLA"));
        apicon.DataSheetResult(apicon.Requestbuilder("IBM"));
        System.out.println("Es is raus");
    }
}
