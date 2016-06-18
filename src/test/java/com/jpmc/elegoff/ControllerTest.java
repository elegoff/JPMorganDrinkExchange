package com.jpmc.elegoff;




import java.util.List;


import com.jpmc.elegoff.model.Stock;
import com.jpmc.elegoff.model.Trade;
import com.jpmc.elegoff.model.TradeType;
import org.junit.Assert;
import org.junit.Test;

public class ControllerTest {
	
	
	

	@Test
	public void stockListTest() {
		List<Stock> stocks = Controller.getStockList();
		
		Assert.assertNotNull(stocks);
		
		
		//Initial stock list is created with 5 stocks
		Assert.assertEquals(5, stocks.size());
		
	}
	
	@Test
	public void tradeListTest() {
		List<Trade> trades = Controller.getTradeList();
		
		Assert.assertNotNull(trades);
		
	}
	
	
	@Test(expected= DrinkExchangeException.class)
	public void dividendYieldUnknownTest() throws DrinkExchangeException {
			Controller.getDividendYield("UNKNOWN", 10);
		
	}
	
	@Test(expected= DrinkExchangeException.class)
	public void PERatioUnknownTest() throws DrinkExchangeException {
			Controller.getPERatio("UNKNOWN", 10);
		
	}
	
	@Test(expected= DrinkExchangeException.class)
	public void dividendYieldPriceZero() throws DrinkExchangeException {
			Controller.getDividendYield("TEA", 0);
		
	}
	
	@Test(expected= DrinkExchangeException.class)
	public void PERatioDividendZero() throws DrinkExchangeException {
			Controller.getPERatio("TEA", 42);
		
	}
	
	
	@Test
	public void dividendYieldCommonOK() throws DrinkExchangeException {
			double res =Controller.getDividendYield("POP", 2);
		Assert.assertEquals(4, res,0);
	}
	
	@Test
	public void PERatioCommonOK() throws DrinkExchangeException {
			double res =Controller.getPERatio("POP", 2);
		Assert.assertEquals(0.25, res,0);
	}

	
	
	@Test
	public void dividendYieldPrefOK() throws DrinkExchangeException {
			double res =Controller.getDividendYield("GIN", 2);
		Assert.assertEquals(1, res,0);
	}
	
	@Test
	public void PERatioPrefOK() throws DrinkExchangeException {
			double res =Controller.getPERatio("GIN", 2);
		Assert.assertEquals(0.25, res,0);
	}
	
	
	@Test 
	public void recordBuyTradeTest() throws DrinkExchangeException {
		Trade trade = Controller.recordTrade("GIN", 12, TradeType.BUY, 42);
		Assert.assertNotNull(trade);
		List<Trade> trades = Controller.getTradeList();
		Assert.assertFalse(trades.isEmpty());
	}

	@Test 
	public void recordSellTradeTest() throws DrinkExchangeException {
		List<Trade> trades = Controller.getTradeList();
		int size = 0;
		if (trades != null){
			size = trades.size();
		}
		
		Trade trade = Controller.recordTrade("TEA", 5, TradeType.SELL, 24);
		Assert.assertNotNull(trade);
		trades = Controller.getTradeList();
		Assert.assertFalse(trades.isEmpty());
		Assert.assertEquals(size +1 , trades.size());
	}

	@Test(expected= DrinkExchangeException.class)
	public void recordTradeForUnknownStock() throws DrinkExchangeException {
		Controller.recordTrade("UNKNOWN", 12, TradeType.BUY, 42);
		
	}
	
	@Test
	public void calculateVolumeWeightedTest() throws DrinkExchangeException {
		
		String symbol = "TEA";
		double result = Controller.calculateVolumeWeighted(symbol, 5);
		
			//add at least one trade for symbol
			Controller.recordTrade(symbol, 10, TradeType.BUY, 10);
			Controller.recordTrade(symbol, 30, TradeType.BUY, 20);
			result = Controller.calculateVolumeWeighted(symbol, 5);
			Assert.assertEquals(17.5, result ,0);
		
		
	}
	
	@Test(expected= DrinkExchangeException.class)
	public void calculateVolumeWeightedUnkownTest() throws DrinkExchangeException {
		
		String symbol = "UNKNOWN";
		double result = Controller.calculateVolumeWeighted(symbol, 5);
		
			//add at least one trade for symbol
			Controller.recordTrade(symbol, 10, TradeType.BUY, 10);
			Controller.recordTrade(symbol, 30, TradeType.BUY, 20);
			result = Controller.calculateVolumeWeighted(symbol, 5);
			Assert.assertEquals(17.5, result ,0);
	}
	
	@Test
	public void calcJPMDE() throws DrinkExchangeException {
		String symbol = "TEA";
		Controller.recordTrade(symbol, 10, TradeType.BUY, 10);
		Controller.recordTrade(symbol, 30, TradeType.BUY, 20);//TEA weighted vol = 17.5
		Controller.recordTrade("GIN", 5, TradeType.SELL, 30);//GIN weighted vol = 30
		
		double jpmde = Controller.JPMDEAllShareIndex(5); //during the last 5 minutes
		
		Assert.assertEquals(Math.pow((17.5 * 30), 0.5), jpmde, 0.001);
		
	}
	
	
}
