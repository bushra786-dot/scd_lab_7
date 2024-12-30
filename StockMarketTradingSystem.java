package scd_lab7;

//StockMarketTradingSystem.java
public class StockMarketTradingSystem {

// Stock class to represent a stock with a price and quantity
static class Stock {
   private String symbol;
   private int quantity;
   private double price;

   public Stock(String symbol, int quantity, double price) {
       this.symbol = symbol;
       this.quantity = quantity;
       this.price = price;
   }

   // Synchronized method to update stock price
   public synchronized void updatePrice(double newPrice) {
       this.price = newPrice;
       System.out.println("Stock " + symbol + " price updated to " + newPrice);
   }

   // Synchronized method to handle buying of stock
   public synchronized boolean buyStock(int quantity) {
       if (this.quantity >= quantity) {
           this.quantity -= quantity;
           System.out.println("Bought " + quantity + " units of " + symbol + ". Remaining: " + this.quantity);
           return true;
       }
       System.out.println("Not enough stock available to buy " + quantity + " units of " + symbol);
       return false;
   }

   // Synchronized method to handle selling of stock
   public synchronized boolean sellStock(int quantity) {
       this.quantity += quantity;
       System.out.println("Sold " + quantity + " units of " + symbol + ". Remaining: " + this.quantity);
       return true;
   }

   // Method to get current stock price (synchronized for thread safety)
   public synchronized double getPrice() {
       return price;
   }

   public String getSymbol() {
       return symbol;
   }

   public int getQuantity() {
       return quantity;
   }
}

// Trader class representing a trader placing buy/sell orders
static class Trader extends Thread {
   private Stock stock;
   private String action; // "buy" or "sell"
   private int quantity;

   public Trader(Stock stock, String action, int quantity) {
       this.stock = stock;
       this.action = action;
       this.quantity = quantity;
   }

   @Override
   public void run() {
       try {
           // Simulate delay in placing the order
           Thread.sleep(500);

           if ("buy".equals(action)) {
               boolean success = stock.buyStock(quantity);
               if (success) {
                   System.out.println(Thread.currentThread().getName() + " successfully bought " + quantity + " units of " + stock.getSymbol());
               }
           } else if ("sell".equals(action)) {
               stock.sellStock(quantity);
               System.out.println(Thread.currentThread().getName() + " successfully sold " + quantity + " units of " + stock.getSymbol());
           }
       } catch (InterruptedException e) {
           System.out.println(Thread.currentThread().getName() + " was interrupted.");
       }
   }
}

public static void main(String[] args) {
   // Create a stock with initial quantity of 100 and price of $50
   Stock appleStock = new Stock("AAPL", 100, 50.0);

   // Create multiple trader threads for different actions
   Trader trader1 = new Trader(appleStock, "buy", 30);
   Trader trader2 = new Trader(appleStock, "sell", 20);
   Trader trader3 = new Trader(appleStock, "buy", 50);
   Trader trader4 = new Trader(appleStock, "sell", 40);
   Trader trader5 = new Trader(appleStock, "buy", 10);
   Trader trader6 = new Trader(appleStock, "sell", 5);

   // Start trader threads
   trader1.start();
   trader2.start();
   trader3.start();
   trader4.start();
   trader5.start();
   trader6.start();

   try {
       // Wait for all threads to finish
       trader1.join();
       trader2.join();
       trader3.join();
       trader4.join();
       trader5.join();
       trader6.join();
   } catch (InterruptedException e) {
       e.printStackTrace();
   }

   // Output the final stock quantity after all transactions
   System.out.println("Final stock status of " + appleStock.getSymbol() + ": Quantity = " + appleStock.getQuantity() + ", Price = $" + appleStock.getPrice());
}
}
