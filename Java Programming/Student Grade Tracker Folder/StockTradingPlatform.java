import java.io.*;
import java.util.*;

// Stock class
class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

// Transaction class
class Transaction {
    String type; // "BUY" or "SELL"
    String stockSymbol;
    int quantity;
    double price;

    Transaction(String type, String stockSymbol, int quantity, double price) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
    }
}

// User class
class User {
    String name;
    double balance;
    Map<String, Integer> portfolio = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();

    User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > balance) {
            System.out.println("Not enough balance!");
            return;
        }
        balance -= cost;
        portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
        transactions.add(new Transaction("BUY", stock.symbol, quantity, stock.price));
        System.out.println("Bought " + quantity + " of " + stock.symbol);
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (owned < quantity) {
            System.out.println("Not enough stock to sell!");
            return;
        }
        balance += stock.price * quantity;
        portfolio.put(stock.symbol, owned - quantity);
        transactions.add(new Transaction("SELL", stock.symbol, quantity, stock.price));
        System.out.println("Sold " + quantity + " of " + stock.symbol);
    }

    void showPortfolio() {
        System.out.println("\nPortfolio:");
        for (String symbol : portfolio.keySet()) {
            System.out.println(symbol + " - " + portfolio.get(symbol) + " shares");
        }
        System.out.println("Balance: $" + balance);
    }

    void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(name);
            out.println(balance);
            for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
                out.println(entry.getKey() + " " + entry.getValue());
            }
            System.out.println("Portfolio saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }

    void loadFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            name = scanner.nextLine();
            balance = Double.parseDouble(scanner.nextLine());
            while (scanner.hasNext()) {
                String sym = scanner.next();
                int qty = scanner.nextInt();
                portfolio.put(sym, qty);
            }
            System.out.println("Portfolio loaded.");
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }
}

// Market class
class Market {
    Map<String, Stock> stocks = new HashMap<>();

    Market() {
        stocks.put("APPLE", new Stock("APPLE", 170.50));
        stocks.put("GOOGLE", new Stock("GOOGLE", 2400.75));
        stocks.put("TESLA", new Stock("TESLA", 720.30));
    }

    void showMarket() {
        System.out.println("\nMarket Stocks:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock.symbol + " - $" + stock.price);
        }
    }

    Stock getStock(String symbol) {
        return stocks.get(symbol);
    }
}

// Main Application
public class StockTradingPlatform {
    public static void main(String[] args) {    
        Scanner scanner = new Scanner(System.in);
        Market market = new Market();
        User user = new User("Devang", 10000.00);

        while (true) {
            System.out.println("\n1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Save Portfolio");
            System.out.println("6. Load Portfolio");
            System.out.println("7. Exit");
            System.out.print("Enter option: ");
            int option = scanner.nextInt();

            if (option == 1) {
                market.showMarket();
            } else if (option == 2) {
                System.out.print("Enter stock symbol: ");
                String symbol = scanner.next().toUpperCase();
                System.out.print("Enter quantity: ");
                int qty = scanner.nextInt();
                Stock stock = market.getStock(symbol);
                if (stock != null) user.buyStock(stock, qty);
                else System.out.println("Stock not found!");
            } else if (option == 3) {
                System.out.print("Enter stock symbol: ");
                String symbol = scanner.next().toUpperCase();
                System.out.print("Enter quantity: ");
                int qty = scanner.nextInt();
                Stock stock = market.getStock(symbol);
                if (stock != null) user.sellStock(stock, qty);
                else System.out.println("Stock not found!");
            } else if (option == 4) {
                user.showPortfolio();
            } else if (option == 5) {
                user.saveToFile("portfolio.txt");
            } else if (option == 6) {
                user.loadFromFile("portfolio.txt");
            } else if (option == 7) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
