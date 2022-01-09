import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Play(String name, String type) {
}

/**
 * Performance
 */
record Performance(String playId, int audience) {
}

record Invoice(String cust, List<Performance> perf) {
}

public class Ch1 {

    public static void main(String[] args) {

        Map<String, Play> plays = Stream.of(new String[][] {
                { "hamlet", "Hamlet", "tragedy" },
                { "as-like", "As You Like It", "comedy" },
                { "othello", "Othello", "tragedy" }

        }).collect(Collectors.toMap(d -> d[0], d -> new Play(d[1], d[2])));

        List<Performance> perfs = List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40));

        Invoice invoice = new Invoice("Bade Babu", perfs);
        System.out.println(statement(invoice, plays));
    }

    public static String statement(Invoice invoice, Map<String, Play> plays) {
        String result = "Statement for " + invoice.cust();
        int totalAmoutn = 0;
        int totalVolCredit = 0;
        for (Performance perf : invoice.perf()) {
            Play play = plays.get(perf.playId());
            int amount = 0;
            switch (play.type()) {
                case "tragedy":
                    amount = 40000;
                    if (perf.audience() > 40) {
                        amount += 1000 * (perf.audience() - 30);
                    }
                    break;

                case "comedy":
                    amount = 30000;
                    if (perf.audience() > 30) {
                        amount += 1000 + 500 * (perf.audience() - 30);
                    }
                    amount += 300 * perf.audience();
                    break;

                default:
                    throw new Error("unknown type : " + perf.playId());
            }

            totalAmoutn += amount;

            int volCredit = Math.max(perf.audience() - 30, 0);
            if (play.type() == "Comedy")
                volCredit += Math.floor(perf.audience() / 10);
            totalVolCredit += volCredit;

            result += "\n     " + play.name() + " : " + amount + "(" + perf.audience() + ")";
        }

        result += "\n Amount owed is " + totalAmoutn + "\n You earned " + totalVolCredit + " credits";
        return result;
    }
}