/**
 * Pankhuri Singhal
 * CSS 496 - Applied Computing Senior Capstone
 * 6/6/22
 * WarrenBot - Stock class
 * This file represents the Stock class for WarrenBot, a chatbot prototype that teaches users about diversification
 * principles. It has methods and fields to represent the features of a Stock object, such as a ticker, the average
 * return, and the standard deviation, a measure of the chance of losing money on the investment.
 */

public class Stock {
    String ticker;
    double avgReturn;
    double stdDev;

    /**
     * default constructor for the Stock class. Initializes ticker to a random three-letter string,
     * and average return and standard deviation to 0.
     */
    public Stock() {
        ticker = "XYZ";
        avgReturn = 0;
        stdDev = 0;
    }

    /**
     * two-arg constructor for the Stock class, reassigning only the ticker and the average return.
     * Meant to represent the (assumption, risk-free) US Treasury Bill rate.
     */
    public Stock(String theTicker, double theReturn) {
        ticker = theTicker;
        avgReturn = theReturn;
        stdDev = 0;
    }

    /**
     * three-arg constructor for the Stock class. Initializes ticker, average return, and
     * standard deviation to a regular stock in the stock market, with nonzero return and standard deviation.
     */
    public Stock(String theTicker, double theStdDev, double theReturn) {
        ticker = theTicker;
        stdDev = theStdDev;
        avgReturn = theReturn;
    }

    /**
     * accessor method for ticker
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * accessor method for average return
     */
    public double getAvgReturn() {
        return avgReturn;
    }

    /**
     * accessor method for standard deviation
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * mutator method for ticker
     */
    public void setTicker(String newticker) {
        this.ticker = newticker;
    }

    /**
     * mutator method for average return
     */
    public void setAvgReturn(double newavgReturn) {
        this.avgReturn = newavgReturn;
    }

    /**
     * mutator method for standard deviation
     */
    public void setStdDev(double newstdDev) {
        this.stdDev = newstdDev;
    }

    /**
     * returns a String representation of the stock, with ticker, return, and standard deviation.
     */
    @Override
    public String toString() {
        return ("Stock: " + ticker + ", Return Rate: " + avgReturn + "%, Standard Deviation: " + stdDev + "%.");
    }
}
