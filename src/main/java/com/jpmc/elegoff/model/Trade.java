package com.jpmc.elegoff.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author elegoff
 * Model object representing a Trade
 */
/**
 * @author elegoff
 *
 */
public class Trade {
	
	
	private Stock stock;
	
	private long timestamp;
	
	private long quantity;
	
	private TradeType operation;
	
	private Double price;

	/**
	 * Public constructor
	 * 
	 * @param stock
	 * @param quantity
	 * @param operation
	 * @param price
	 */
	public Trade(Stock stock, long quantity, TradeType operation, Double price) {
		super();
		this.stock = stock;
		this.quantity = quantity;
		this.operation = operation;
		this.price = price;
		this.timestamp = Calendar.getInstance().getTimeInMillis();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		

		StringBuffer sb = new StringBuffer(stock.toString());
		sb.append(" |q= ");
		sb.append(quantity);
		sb.append(" | ");
		sb.append(operation);
		sb.append(" |p= ");
		sb.append(price);
		sb.append(" | ");
		sb.append(formatter.format(new Date(timestamp)));
		sb.append("\n");
		
		return sb.toString();
		
	}

	/**
	 * @return the Stock object for the trade
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * @return the timestamp of the recorded Trade
	 */
	public long getTimestamp() {
		return timestamp;
	}

	
	/**
	 * @return the traded quantity
	 */
	public long getQuantity() {
		return quantity;
	}

	/**
	 * @return either BUY or SELL
	 */
	public TradeType getOperation() {
		return operation;
	}

	/**
	 * @return the traded price
	 */
	public Double getPrice() {
		return price;
	}
	
	
	
	

}
