package com.jpmc.elegoff.actors;

import java.io.Serializable;

/**
 * @author elegoff
 * Class which encapsulates message types handled by a StokActor
 */
public class StockMsg {
	
	/**
	 * @author elegoff
	 * Message sent to StockActor so that it returns its List of Stocks
	 */
	public static class ListStocks implements Serializable{

		private static final long serialVersionUID = 2628795812002053017L;
	}
	
	/**
	 * @author elegoff
	 * Message sent to StockActor to calculate a Dividend Yield
	 */
	public static class DividendYield implements Serializable{
		private static final long serialVersionUID = -4532063710410940715L;
		private String symbol;
		private double price;
		
		public DividendYield(String symbol, double price){
			this.symbol =symbol;
			this.price=price;
		}
		
		public String getSymbol(){
			return this.symbol;
		}
		
		
		public double getPrice(){
			return this.price;
		}
		
	}

	/**
	 * @author elegoff
	 * Message sent to StockActor to request P/E Ratio calculation
	 */
	public static class PERatio implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3315264995858618939L;
		private String symbol;
		private double price;
		
		public PERatio(String symbol, double price){
			this.symbol =symbol;
			this.price=price;
		}
		
		public String getSymbol(){
			return this.symbol;
		}
		
		
		public double getPrice(){
			return this.price;
		}
		
	}
	
	
}
