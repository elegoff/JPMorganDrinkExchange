package com.jpmc.elegoff.actors;


import akka.actor.*;
import akka.japi.Creator;
import com.jpmc.elegoff.DrinkExchangeException;
import com.jpmc.elegoff.Calculator;
import com.jpmc.elegoff.StockHelper;
import com.jpmc.elegoff.model.Stock;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author elegoff
 * Actor classes handling Stock related actions and keeping track of list of Stock objects 
 */
public class StockActor extends UntypedActor{
	private static Logger logger = LoggerFactory.getLogger(StockActor.class);
	
	
	/**
	 * @author elegoff
	 * Stock actor creator
	 */
	public static class StockActorC implements Creator<StockActor> {		
		private static final long serialVersionUID = 1L;

		@Override public StockActor create() {
		    try {
				return new StockActor(StockHelper.loadJson());
			} catch (Exception e) {
				logger.error(e.getMessage());
				
			}
		    return null;
		  }
		}
	
	
	//Keep track in memory of current known stocks
	private List<Stock> stocks;

	
	
	
	/**
	 * Public contructor
	 * @param stocks : a list of initial Stock objects
	 */
	public StockActor(List<Stock> stocks) {
		super();
		this.stocks = stocks;
	}




	/* (non-Javadoc)
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object msg) throws Exception {
	
		//Requesting a list of Stocks
		if (msg instanceof StockMsg.ListStocks){
			getSender().tell(stocks, getSelf());
		}
		//Requesting Dividend Yield calculation
		else if(msg instanceof StockMsg.DividendYield){
			StockMsg.DividendYield m = (StockMsg.DividendYield)msg;
			try{
				Stock stock = StockHelper.getStockBySymbol(m.getSymbol(), stocks);
				double dy = Calculator.calculateDividendYield(stock, m.getPrice());
				getSender().tell(dy, getSelf());	
			}catch(DrinkExchangeException be){
				getSender().tell(new Status.Failure(be), getSelf());	
			}catch(NoSuchElementException nse){
				getSender().tell(new Status.Failure(new DrinkExchangeException("Symbol : " + m.getSymbol() + " is not found"  )), getSelf());
			}
			
			
			
		}
		//Requesting P/E Ratio
			else if(msg instanceof StockMsg.PERatio){
				StockMsg.PERatio m = (StockMsg.PERatio)msg;
				try{
					Stock stock = StockHelper.getStockBySymbol(m.getSymbol(), stocks);
					double per = Calculator.calculatePERatio(stock, m.getPrice());
					getSender().tell(per, getSelf());
				}catch(DrinkExchangeException be){
					getSender().tell(new Status.Failure(be), getSelf());	
				}catch(NoSuchElementException nse){
					getSender().tell(new Status.Failure(new DrinkExchangeException("Symbol : " + m.getSymbol() + " is not found"  )), getSelf());
			}
				
				
			
		}else{
			unhandled(msg);
		}
	}
	
	
	
	
	

}
