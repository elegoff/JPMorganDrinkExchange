package com.jpmc.elegoff;



import java.util.List;

import com.jpmc.elegoff.model.Stock;
import com.jpmc.elegoff.model.StockType;
import com.jpmc.elegoff.model.Trade;

/**
 * @author elegoff
 * Utility class dedicated to calculation functions
 */
public class Calculator {
	
	
	
	/**
	 * @param s : the Stock
	 * @param price
	 * @return the dividend Yield result
	 * @throws DrinkExchangeException
	 */
	public static double calculateDividendYield(Stock s , double price) throws DrinkExchangeException {
		double result =0;
		if (price <= 0) {
			throw new DrinkExchangeException("Dividend Yield calculation requires a positive price");
		}
		
			if (s.getStockType() == StockType.PREFERRED){
				result = (s.getFixedDividend() * s.getParValue())/(price * 100);
			}else{ //StockType.COMMON
				result= s.getLastDividend()/price;
			}
		return result;
	}
	
	/**
	 * @param s : the Stock
	 * @param price
	 * @return result of P/E Ratio calculation
	 * @throws DrinkExchangeException
	 */
	public static double calculatePERatio(Stock s, double price) throws DrinkExchangeException {
		double result =0;
		
		if (s.getLastDividend() <= 0) {
			throw new DrinkExchangeException("P/E Ratio calculation requires a positive dividend");
		}
		
				result= price/s.getLastDividend();
		
		return result;
	}
	
	/**
	 * @param trades : list of trades for a given Stock
	 * @return result of Volume Weighted Stock price
	 */
	public static double calcVolumeWeightedStockPrice(List<Trade> trades){
		if (trades.isEmpty()) return 0d;
		
		double num = 0d, denum = 0d;
		
		for(Trade t:trades){
			num += t.getPrice() * t.getQuantity();
			denum += t.getQuantity();
		}
		
		return num/denum;
	}
	
	/**
	 * @param serie : a serie of double numbers
	 * @return the geometric mean of the serie
	 */
	public static double geometricMean(List<Double> serie){
		int size = serie.size();
		
	    if (size == 0)
	        return Double.NaN;
		double product = 1d;
		int count = 0;
		
		for (Double d : serie) {
	        
	        if (d != Double.NaN) {
	        	
	            product = product * d;
	            count ++;
	        }
		}		
		if (count == 0)
	        return Double.NaN;
		
		return Math.pow (product, 1d / (double) count);
		
	}
	

}
