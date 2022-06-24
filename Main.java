import java.util.*;

/**
 * Pankhuri Singhal
 * CSS 496 - Applied Computing Senior Capstone
 * 6/6/22
 * WarrenBot - Driver class
 * This file represents the main class for WarrenBot, a chatbot prototype that teaches users about diversification
 * principles. It has methods to represent commands, such as +work, +help invest, +choose, and +invest <number>.
 */

public class Main {
    /**
     * Pre: none
     * Post: depending on what user enters as command, will jump to respective method and execute it.
     *
     * Main method for WarrenBot driver class. Includes command branching and stock initialization.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double bal = 0;
        double investBal = 0;

        Stock stockFund = new Stock("Rs", 30, 20);
        Stock bondFund = new Stock("Rb", 15, 12);
        Stock tBill = new Stock("^IRX", 8);

        Stock [] stocks = {stockFund, bondFund};

        runIntro();
        int numDaysWorked = 0;
        while (true) {
            String newInput = sc.nextLine();
            if (newInput.equalsIgnoreCase("+help")) {
                runHelpCommands();
            } else if (newInput.equalsIgnoreCase("+work")) {
                bal = runWorkCommand(bal, numDaysWorked); //x = change(x), remember? updates balance field
                numDaysWorked++;
            } else if (newInput.equalsIgnoreCase("+quit")) {
                System.out.println("Thank you for visiting WarrenBot!");
                break;
            } else if (newInput.equalsIgnoreCase("+help invest")) {
                investBal = runHelpInvestCommand(sc, stocks, bal, investBal, tBill);
            } else if (newInput.contains("+choose")) { //choose stocks to invest in
                runChooseStocksCommand(investBal, newInput, stocks, tBill);
            }
        }
    }

    /**
     * Pre: none, as this message appears by default when running the program.
     * Post: intro message is outputted and user is directed to what command to run.
     *
     * Introduction message method, outputted when running the program.
     */
    public static void runIntro() {
        System.out.println("Welcome to WarrenBot v1.6-- a bot to help you with your investment decisions! \n" +
                "Enter +help for a list of commands.");
        System.out.println();
    }

    /**
     * Pre: user enters +help into console.
     * Post: help message is outputted and user is given a list of commands to choose from.
     *
     * Help message method, outputted when entering +help command.
     */
    public static void runHelpCommands() {
        System.out.println("Here's a list of commands you can use: \n" +
                "\t+work : Go to work to earn money! ($100 at a time) \n" +
                "\t+help invest : Learn about the select stocks you can invest in \n" +
                "\t+quit : To exit the bot"  );
        System.out.println();
    }

    /**
     * Pre: user enters +work into console.
     * Post: bot adds to user's balance as income. User recieves notification from console output that working earned
     * them an additional $100.
     *
     * Work message command method, outputted when entering +work command. Note - after every 10 days of working,
     * the user recieves a 2% bonus.
     */
    public static double runWorkCommand(double balance, int daysWorked) {
        double addedBal = 100;
        double raisePercent;
        if (daysWorked % 10 == 0 && daysWorked != 0) {
            System.out.println("You received a raise for working 10 days!");
            raisePercent = 2;
            addedBal = 100 * (1 + (raisePercent / 100));
        }
        balance += addedBal;
        System.out.println("You went to work and earned $" + addedBal + "\n" +
                            "Your balance is now $" + balance);
        System.out.println();
        return balance;
    }

    /**
     * Pre: user enters +help invest into console.
     * Post: the bot outputs a list of stocks the user can choose to invest from. Later, once user specifies investment
     * amount, bot outputs either message for "too large amount" or "appropriate amount". If appropriate, bot deducts
     * investment balance from total balance and asks user to enter specific stock tickers.
     *
     * Investment list message command method, outputted when entering +help invest command. Has commands inside method -
     * notably, +invest <number> and +choose <ticker> commands. Leads to portfolio creation of user's assets.
     */
    public static double runHelpInvestCommand(Scanner sc, Stock[] stocks, double balance, double investBal,
                                            Stock tBill) {
        System.out.println("Here's a list of stocks you can invest in: ");
        for (Stock thisStock : stocks) {
            System.out.println(thisStock.toString());
        }
        System.out.println("How much would you like to invest? Enter +invest <number> to specify an amount.");
        String inputStr = sc.nextLine();
        String theAmt = inputStr.replaceAll("[^0-9]", "");
        investBal = Double.parseDouble(theAmt);
        if (investBal > balance) {
            System.out.println("You can't invest more than what you've earned! Keep working, or invest a smaller amount.");
            return 0;
        } else {
            System.out.println("You've decided to invest $" + investBal + ". Do you wish to invest in " +
                    "the stock fund (" + stocks[0].getTicker() + "), the bond fund (" + stocks[1].getTicker() +
                    ") or both? \nEnter +choose, and then a space-separated list of tickers.\n" +
                    "EXAMPLE: +choose MSFT AAPL");
            return investBal;
        }
    }

    /**
     * Pre: user enters +choose <ticker> <ticker>... <ticker> into console.
     * Post: the bot calls other methods to evaluate the portfolio based on user's chosen stocks. Once equally-weighted
     * portfolio outputted, user has option to optimize portfolio, and that respective portfolio is also computed and
     * printed.
     *
     * Method to act as a "messenger" that references another method to compute portfolios and another to
     * print them. Has a maximization algorithm to pick out the optimal risky portfolio (the one that simultaneously
     * maximizes expected return and minimizes chances of losing from the investment, while accounting for the
     * Treasury bill).
     */
    public static void runChooseStocksCommand(double investBal, String userInput, Stock[] stocks, Stock tBill) {
        String allTickers = userInput.substring(8);
        String [] separateTickers = allTickers.split(" ");
        System.out.println("Here's what happens when you invest equally, as in \n " +
                "$" + investBal/separateTickers.length + " " +
                "into each stock:");
        Object [] portfolioInfo = evaluatePortfolio(stocks, tBill, separateTickers, investBal, 0.5);
        printPortfolioInfo(portfolioInfo);

        System.out.println("Would you like to optimize this portfolio? (y/n)");
        Scanner optimizeOrNot = new Scanner(System.in);
        String userOptimize = optimizeOrNot.nextLine();
        if (userOptimize.equalsIgnoreCase("y")) {
            System.out.println("Enter +optimize to view the optimal portfolio!");
            String yesOptimize = optimizeOrNot.nextLine();
            if (yesOptimize.equalsIgnoreCase("+optimize")) {
                double [] sharpeArray = new double[101];
                Object[][] portfolioStorage = new Object[101][1];
                for (double alphaTracker = 0; alphaTracker <= 1; alphaTracker += 0.01) {
                    Object[] currAlphaTrackerArray =
                            evaluatePortfolio(stocks, tBill, separateTickers, investBal, alphaTracker);
                    double currSharpe = new Double(currAlphaTrackerArray[6].toString());
                    int indexOfSharpe = (int) (100 * alphaTracker);
                    portfolioStorage[indexOfSharpe] = currAlphaTrackerArray;
                    sharpeArray[indexOfSharpe] = currSharpe;
                }

                double maxSharpe = sharpeArray[0];
                int indexOfMaxSharpe = 0;
                for (int i = 0; i < sharpeArray.length; i++) {
                    if (sharpeArray[i] > sharpeArray[indexOfMaxSharpe]) {
                        maxSharpe = sharpeArray[indexOfMaxSharpe];
                        indexOfMaxSharpe = i;
                    }
                }
                portfolioInfo[5] = (indexOfMaxSharpe)/100.0;
                portfolioInfo[6] = maxSharpe;
                printPortfolioInfo(portfolioStorage[indexOfMaxSharpe]);
            }
        }
    }

    /**
     * Pre: +choose <ticker> <ticker>... <ticker> command is run by user.
     * Post: the bot computes a portfolio based on a specified arbitrary allocation into the first stock
     * (this allocation known as alpha). An array of Objects containing the portfolio information is returned,
     * to be either printed or stored determined on whether a portfolio is being computed and compared against other
     * portfolios, or just printed (in the case of the optimal risky portfolio and the equally-weighted portfolio).
     *
     * Method to simulate Excel Solver and compute a portfolio based on an alpha value as the changing variable.
     * Currently works for 2 stocks. Includes correlation coefficient matrix, covariance matrix calculation, and
     * resultant values of expected return, variance, risk premium, and reward-to-variability ratio (Sharpe ratio).
     */
    public static Object[] evaluatePortfolio(Stock[] stocks, Stock tBill, String[] userTickers,
                                         double investBal, double alpha) {
        double [][] corrCoeffs = new double[userTickers.length][userTickers.length];
        corrCoeffs[0][0] = corrCoeffs[1][1] = 1; //corr(Rs, Rs) and corr(Rb, Rb)
        corrCoeffs[0][1] = corrCoeffs[1][0] = 0.1; //corr(Rs, Rb)

        double [][] weightedCovarMatrix = new double[userTickers.length][userTickers.length];
        double[][] weightsOfBothStocks = new double[stocks.length][stocks.length];
        weightsOfBothStocks[0][0] = Math.pow(alpha, 2);
        weightsOfBothStocks[1][1] = Math.pow((1 - alpha), 2);
        weightsOfBothStocks[0][1] = weightsOfBothStocks[1][0] = alpha * (1 - alpha);

        double[][] stdDevsOfBothStocks = new double[stocks.length][stocks.length];
        stdDevsOfBothStocks[0][0] = Math.pow(stocks[0].getStdDev(), 2);
        stdDevsOfBothStocks[1][1] = Math.pow(stocks[1].getStdDev(), 2);
        stdDevsOfBothStocks[0][1] = stdDevsOfBothStocks[1][0] = stocks[0].getStdDev() * stocks[1].getStdDev();

        for (int i = 0; i < weightedCovarMatrix.length; i++) {
            for (int j = 0; j < weightedCovarMatrix[i].length; j++) {
                weightedCovarMatrix[i][j] = weightsOfBothStocks[i][j] * stdDevsOfBothStocks[i][j] * corrCoeffs[i][j];
            }
        }
        double expectedRet = alpha * stocks[0].getAvgReturn() + (1 - alpha) * (stocks[1].getAvgReturn());
        double variance = 0;
        for (int i = 0; i < weightedCovarMatrix.length; i++) {
            for (int j = 0; j < weightedCovarMatrix[i].length; j++) {
                variance += weightedCovarMatrix[i][j];
            }
        }
        double stdDevOverall = Math.sqrt(variance);
        double riskPrem = expectedRet - tBill.getAvgReturn();
        double sharpe = riskPrem/stdDevOverall;

        Object[] portfolioInfo = {userTickers[0], userTickers[1], investBal, expectedRet, variance, alpha, sharpe};
        return portfolioInfo;
    }

    /**
     * Pre: +choose <ticker> <ticker>... <ticker> command is run by user, evaluatePortfolio() method run.
     * Post: the bot prints a portfolio out onto console. An array of Objects containing the portfolio information is
     * printed. It contains exactly how much to invest in each stock as it has the investment balance in the array.
     *
     * Method to print portfolio information to user. Outputs expected return, standard deviation (as a measure of risk -
     * the chance of losing money on the investment), and how much money to invest per stock.
     */
    public static void printPortfolioInfo(Object[] portfolioInfo) {
        String ticker1 = portfolioInfo[0].toString();
        String ticker2 = portfolioInfo[1].toString();
        double variance = new Double(portfolioInfo[4].toString());
        double stdDevOverall = Math.sqrt(variance);
        double alpha = new Double(portfolioInfo[5].toString());
        double expectedRetOverall = new Double(portfolioInfo[3].toString());
        double investBal = new Double(portfolioInfo[2].toString());
        System.out.println("\nPortfolio with weight allocation of " + Math.round((alpha * 100) * 100.0)/100.0 +
                "% to " + ticker1 + " and " + Math.round(((1 - alpha) * 100) * 100.0)/100.0 + "% to " + ticker2);

        System.out.println("Expected return: $" + expectedRetOverall);
        System.out.println("Standard Deviation (a measure of risk): $" + Math.round(stdDevOverall * 100.0) / 100.0);

        System.out.println("Invest $" + Math.round((alpha * investBal) * 100.0)/100.0 + " into " + ticker1 + " and $" +
                Math.round(((1-alpha) * investBal) * 100.0)/100.0 + " into " + ticker2);
        System.out.println();
    }
}