package com.jpmc.elegoff.model;

/**
 * @author elegoff
 * Model object representing a Stock
 */
public class Stock {
	
	
	private String symbol;
	
	
	//Buy or Sell enum
	private StockType stockType;
	
	private Double lastDividend;
	
	//%age between 0 and 100
	private int fixedDividend; 
	
	private Double parValue;
	
	

	/**
	 * Public constructor
	 * 
	 * @param symbol
	 * @param stockType
	 * @param lastDividend
	 * @param fixedDividend
	 * @param parValue
	 */
	public Stock(String symbol, StockType stockType, Double lastDividend, int fixedDividend, Double parValue) {
		super();
		this.symbol = symbol;
		this.stockType = stockType;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
	}


	/**
	 * @return the stock symbol
	 */
	public String getSymbol() {
		return symbol;
		
	}

	
	/**
	 * @return the enum COMMON or PREFERRED
	 */
	public StockType getStockType() {
		return stockType;
	}


	/**
	 * @return the last dividend
	 */
	public Double getLastDividend() {
		return lastDividend;
	}


	/**
	 * @return the fixed dividend
	 */
	public int getFixedDividend() {
		return fixedDividend;
	}


	public Double getParValue() {
		return parValue;
	}

	
	@Override
	public String toString(){
		
		StringBuffer sb = new StringBuffer(symbol);
		sb.append(" | ");
		sb.append(stockType);
		sb.append(" | ");
		sb.append(lastDividend);
		sb.append(" | ");
		sb.append(fixedDividend);
		sb.append(" | ");
		sb.append(parValue);
		sb.append("\n");
		
		return sb.toString();
		
	}
	
	
	

}
