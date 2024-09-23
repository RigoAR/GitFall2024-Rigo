import java.io.*;
import java.util.*;

public class SlayTheSpireDeck {

    //Initializing :}
    private List<Card> validCards = new ArrayList<>();
    private List<String> invalidCards = new ArrayList<>();
    private int[] energyCostHistogram = new int[7]; // Index 0-6 for costs
    private String deckId;
    private double totalCost = 0;

    public static void main(String[] args) {
        SlayTheSpireDeck manage = new SlayTheSpireDeck();
        manage.askForFile();
        manage.generateReport();
    }


    //Asks user for a file name nicely and loads the deck from that file
    private void askForFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the filename please :] : ");
        String fileName = scanner.nextLine();
        loadDeck(fileName);
    }


    //Loads cards from a specified input file but if not good sends an error
    private void loadDeck(String fileName) {
        try {
            //Since big files need buff reader to make faster
            FileReader fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) {
                processCard(line.trim());
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }


    //Processes a single card entry. in the format <card name>:<cost>
    private void processCard(String cardEntry) {
        // Split the card entry
        String name = "";
        String costStr = "";
        boolean validEntry = false;

        for (int i = 0; i < cardEntry.length(); i++) {
            if (cardEntry.charAt(i) == ':') {
                name = cardEntry.substring(0, i).trim();
                costStr = cardEntry.substring(i + 1).trim();
                validEntry = true;
                break;
            }
        }
        //Adds cards if valid, if not doesn't :)

        if (validEntry) {
            if (name.isEmpty() || !isValidCost(costStr)) {
                invalidCards.add(cardEntry);
                return;
            }

            int cost = Integer.parseInt(costStr);
            validCards.add(new Card(name, cost));
            totalCost += cost;
            energyCostHistogram[cost]++;
        } else {
            invalidCards.add(cardEntry);
        }
    }


    //Checks if the cost is a valid energy cost (0-6)
    private boolean isValidCost(String costStr) {
        try {
            int cost = Integer.parseInt(costStr);
            return cost >= 0 && cost <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    //Generates a report based on the loaded cards but checks if it's more than 1k or less than 10
    private void generateReport() {
        deckId = generateDeckId();

        if (validCards.size() > 1000 || invalidCards.size() > 10) {
            createVoidReport();
        } else {
            createNormalReport();
        }
    }


     //Generates a unique 9-digit deck ID and returns it
    private String generateDeckId() {
        Random random = new Random();
        String deckId = "";


        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            deckId += digit;
        }

        return deckId;
    }

    //Creates a void report meaning not what it is looking for like format
    private void createVoidReport() {
        String reportName = "SpireDeck " + deckId + "(VOID).pdf";
        System.out.println("Generated report: " + reportName);
    }

    //Creates report of cards used and energy used
    private void createNormalReport() {
        String reportName = "SpireDeck " + deckId + ".pdf";
        System.out.println("Generated report: " + reportName);
        System.out.println("Total cost: " + totalCost);
        System.out.println("Energy Cost Histogram:");
        for (int i = 0; i < energyCostHistogram.length; i++) {
            System.out.println(i + " energy: " + energyCostHistogram[i] + " cards");
        }
    }

    //Initializes a card object with a specific name and energy cost
    class Card {
        String name;
        int cost;

        public Card(String name, int cost) {
            this.name = name;
            this.cost = cost;
        }
    }
}
