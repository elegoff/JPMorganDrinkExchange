package com.jpmc.elegoff;

import java.util.List;

import com.budhash.cliche.Command;
import com.budhash.cliche.Param;

import com.jpmc.elegoff.model.Stock;
import com.jpmc.elegoff.model.Trade;
import com.jpmc.elegoff.model.TradeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;    

/**
 * @author elegoff
 * Class handling command line functions
 */
public class Commands {
	private static Logger logger = LoggerFactory.getLogger(Commands.class);

	/**
	 * @return a string which displays the current list of Stocks in memory
	 */
	@Command
	public String listCurrentStocks() {

		List<Stock> stocks = Controller.getStockList();
		StringBuffer sb = new StringBuffer("");
		for(Stock stock : stocks){
			sb.append(stock.toString());

		}
		return sb.toString();	

	}


	/**
	 * @return a string which displays the current list of Trades in memory
	 */
	@Command
	public String listCurrentTrades() {

		List<Trade> trades = Controller.getTradeList();
		StringBuffer sb = new StringBuffer("");
		for(Trade trade : trades){
			sb.append(trade.toString());

		}
		return sb.toString();	

	}


	/**
	 * @param symbol  : Stock symbol string, e.g "TEA" , "GIN" etc..
	 * @param price 
	 * @return command line output for dividendYield calculation
	 */
	@Command(description="Calculate dividend yield for a given stock symbol and a price", header="Dividend yield is : ")
	public String dividendYield(

			@Param(name="Stock symbol", description="A string identifying a stock symbol") 
			String symbol, 
			@Param(name="price", description="A number corresponding to the price")
			double price) {
		String result = Double.toString(Double.NaN);

		try {
			result = Double.toString(Controller.getDividendYield(symbol, price));
		} catch (DrinkExchangeException e) {
			
			logger.error("Exception raised : " + e.getMessage());
			return "Exception raised : " + e.getMessage();		}
		return result;
	}

	/**
	 * @param symbol: Stock symbol string, e.g "TEA" , "GIN" etc..
	 * @param price
	 * @return command line output for P/E Ratio
	 */
	@Command(description="Calculate P/E Ratio for a given stock symbol and a price", header="P/E Ratio is : ")
	public String PERatio(

			@Param(name="Stock symbol", description="A string identifying a stock symbol") 
			String symbol, 
			@Param(name="price", description="A number corresponding to the price")
			double price){
		String result = Double.toString(Double.NaN);

		try {
			result = Double.toString(Controller.getPERatio(symbol, price));
		} catch (DrinkExchangeException e) {
			logger.error("Exception raised : " + e.getMessage());
			return "Exception raised : " + e.getMessage();
		}
		return result;
	}

	/**
	 * @param symbol Stock symbol string, e.g "TEA" , "GIN" etc..
	 * @param quantity : number of Stocks to be bought
	 * @param price : buying price
	 * @return command line output of the Trade
	 */
	@Command(description="Buy a trade for a given stock", header="Bought trade is : ")
	public String buyTrade(
			@Param(name="Stock symbol", description="A string identifying a stock symbol")
			String symbol, 
			@Param(name="quantity", description="A number corresponding to quantity you buy")
			long quantity, 
			@Param(name="price", description="A number corresponding to the buying price")
			double price){
		Trade t = null;
		try {
			t = Controller.recordTrade(symbol, quantity, TradeType.BUY, price);
		} catch (DrinkExchangeException e) {
			logger.error("Exception raised : " + e.getMessage());
			return "Exception raised : " + e.getMessage();
		}
		return t.toString();
	}

	/**
	 * @param symbol : Stock symbol string, e.g "TEA" , "GIN" etc..
	 * @param quantity : number of Stocks to be sold
	 * @param price : selling price
	 * @return command line output of the Trade
	 */
	@Command(description="Sell a trade for a given stock", header="Sold trade is : ")
	public String sellTrade(
			@Param(name="Stock symbol", description="A string identifying a stock symbol")
			String symbol, 
			@Param(name="quantity", description="A number corresponding to quantity you sell")
			long quantity, 
			@Param(name="price", description="A number corresponding to the selling price")
			double price){
		Trade t = null;
		try {
			t = Controller.recordTrade(symbol, quantity, TradeType.SELL, price);
		} catch (DrinkExchangeException e) {
			logger.error("Exception raised : " + e.getMessage());	
			return "Exception raised : " + e.getMessage();
		}
		return t.toString();
	}

	/**
	 * @param symbol : Stock symbol string, e.g "TEA" , "GIN" etc..
	 * @param duration : period of the past in minutes
	 * @return command line output for Volume Weighted Stock Price
	 */
	@Command(description="Calculate Volume Weighted Stock Price based on trades", header="Volume Weighted Stock Price is : ")
	public String calculateVolumeWeighted(
			@Param(name="Stock symbol", description="A string identifying a stock symbol")
			String symbol, 
			@Param(name="past minutes", description="Number of minutes in the past")
			int duration
			){
		if (duration <= 0){
			logger.warn("Invalid duration parameter : " + duration);
			return "You need a positive value for the duration parameter";
		}
			

		String result = Double.toString(Double.NaN);
		try {
			result = Double.toString(
					Controller.calculateVolumeWeighted(symbol, duration));
		} catch (DrinkExchangeException e) {
			logger.error("Exception raised : " + e.getMessage());
			return "Exception raised : " + e.getMessage();	
		}
		return result;
	}

	/**
	 * @param duration : : period of the past in minutes
	 * @return command line output for JPMDE all share index
	 */
	@Command(description="JPMDE All Share Index", header="JPMDE All Share Index is : ")
	public double JPMDEAllShareIndex(@Param(name="past minutes", description="Number of minutes in the past")
	int duration){
		if (duration <= 0) return 0d;

		double result = 0d;
		try {
			result = Controller.JPMDEAllShareIndex(duration);
		} catch (DrinkExchangeException e) {
			logger.error("Exception raised : " + e.getMessage());			}

		return result;
	}



}
