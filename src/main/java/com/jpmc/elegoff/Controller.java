package com.jpmc.elegoff;

import java.util.List;

import akka.pattern.Patterns;
import com.jpmc.elegoff.actors.StockActor;
import com.jpmc.elegoff.actors.StockMsg;
import com.jpmc.elegoff.actors.TradeActor;
import com.jpmc.elegoff.actors.TradeMsg;
import com.jpmc.elegoff.model.Stock;
import com.jpmc.elegoff.model.Trade;
import com.jpmc.elegoff.model.TradeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import java.util.NoSuchElementException;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static akka.pattern.Patterns.ask;

/**
 * @author elegoff
 * class responsible to handle the Akka actors, and dispatch them messages
 */
public class Controller {
	private static Logger logger = LoggerFactory.getLogger(Controller.class);

	private static ActorRef stockActor;
	private static ActorRef tradeActor;

	public static ActorRef getStockActor(){
		if (Controller.stockActor == null){
			ActorSystem system=ActorSystem.create("drink-exchange-system");
			Controller.stockActor = system.actorOf(Props.create(new StockActor.StockActorC()));
		}
		return Controller.stockActor;
	}

	public static ActorRef getTradeActor(){
		if (Controller.tradeActor == null){
			ActorSystem system=ActorSystem.create("drink-exchange-system");
			Controller.tradeActor = system.actorOf(Props.create(new TradeActor.TradeActorC()));
		}
		return Controller.tradeActor;
	}

	/**
	 * @return the list of Stocks currently in memory
	 */
	@SuppressWarnings("unchecked")
	public static List<Stock> getStockList(){

		List<Stock> result = null;


		Timeout timeout = new Timeout(Duration.create(1, "seconds"));
		Future<Object> future = Patterns.ask(Controller.getStockActor(), new StockMsg.ListStocks(), timeout);//1 second timeout
		try {
			result = (List<Stock>)Await.result(future, timeout.duration());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}


		return result;
	}


	/**
	 * @return the list of Trades currently in memory
	 */
	@SuppressWarnings("unchecked")
	public static List<Trade> getTradeList(){

		List<Trade> result = null;


		Timeout timeout = new Timeout(Duration.create(1, "seconds"));
		Future<Object> future = Patterns.ask(Controller.getTradeActor(), new TradeMsg.ListTrades(), timeout);//1 second timeout
		try {
			result = (List<Trade>)Await.result(future, timeout.duration());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}


		return result;
	}



	
	/**
	 * @param symbol : Stock symbol
	 * @param price
	 * @return Given any price as input, calculate the dividend yield for a given Stock 
	 * @throws DrinkExchangeException
	 */
	public static double getDividendYield(String symbol, double price) throws DrinkExchangeException {
		double result = 0;

		Timeout timeout = new Timeout(Duration.create(1, "seconds"));
		Future<Object> future =ask(Controller.getStockActor(), new StockMsg.DividendYield(symbol, price), timeout);//1 second timeout
		try {
			result = (double)Await.result(future, timeout.duration());

		} 
		catch (DrinkExchangeException e) {
			throw e;
		}

		catch (Exception e) {
			logger.error(e.getMessage());
		}


		return result;

	}

	
	/**
	 * @param symbol : Stock symbol
	 * @param price
	 * @return Given any price as input, calculate the P/E Ratio for a given Stock
	 * @throws DrinkExchangeException
	 */
	public static double getPERatio(String symbol, double price) throws DrinkExchangeException {
		double result = 0;

		Timeout timeout = new Timeout(Duration.create(1, "seconds"));
		Future<Object> future =ask(Controller.getStockActor(), new StockMsg.PERatio(symbol, price), timeout);//1 second timeout
		try {
			result = (double)Await.result(future, timeout.duration());

		} 
		catch (DrinkExchangeException e) {
			throw e;
		}

		catch (Exception e) {
			logger.error(e.getMessage());
		}


		return result;

	}

	

	/**
	 * @param symbol
	 * @param quantity
	 * @param buyOrSell
	 * @param price
	 * @return Record a trade, with timestamp, quantity, buy or sell indicator and price for a given Stock
	 * @throws DrinkExchangeException
	 */
	public static Trade recordTrade(String  symbol, long quantity, TradeType buyOrSell , double price) throws DrinkExchangeException {
		Trade t = null;

		try{
			List<Stock> stocks = Controller.getStockList();
			Stock stock = StockHelper.getStockBySymbol(symbol, stocks);


			Timeout timeout = new Timeout(Duration.create(1, "seconds"));
			Future<Object> future;
			if (buyOrSell == TradeType.BUY){
				future =ask(Controller.getTradeActor(), new TradeMsg.Buy(stock, quantity, price), timeout);//1 second timeout
			}else
			{
				future =ask(Controller.getTradeActor(), new TradeMsg.Sell(stock, quantity, price), timeout);//1 second timeout
			}

			t = (Trade)Await.result(future, timeout.duration());



		}catch(NoSuchElementException nse){
			throw new DrinkExchangeException("Unknown stock with symbol : " + symbol);
		}
		catch (Exception e) {
			throw new DrinkExchangeException(e.getMessage());
		}




		return t;
	}


	

	/**
	 * @param symbol : Stock symbol
	 * @param duration :  number of minutes back in time
	 * @return Calculate Volume Weighted Stock Price based on trades in past minutes (e.g last 5 minutes)
	 * @throws DrinkExchangeException
	 */
	public static double calculateVolumeWeighted(String symbol, int duration) throws DrinkExchangeException {

		double result =0d;
		try{
			List<Stock> stocks = Controller.getStockList();
			Stock stock = StockHelper.getStockBySymbol(symbol, stocks);
			Timeout timeout = new Timeout(Duration.create(1, "seconds"));
			Future<Object> future =ask(Controller.getTradeActor(), new TradeMsg.VolumeWeighted(stock, duration), timeout);//1 second timeout

			result = (double)Await.result(future, timeout.duration());

		}catch(NoSuchElementException nse){
			throw new DrinkExchangeException("Unknown stock with symbol : " + symbol);
		}
		catch (Exception e) {
			throw new DrinkExchangeException(e.getMessage());
		}

		return result;
	}

	/**
	 * @param duration : number of minutes back in time
	 * @return
	 * @throws DrinkExchangeException
	 */
	public static double JPMDEAllShareIndex(int duration) throws DrinkExchangeException {
		double result =0d;
		try{

			Timeout timeout = new Timeout(Duration.create(1, "seconds"));
			Future<Object> future =ask(Controller.getTradeActor(), new TradeMsg.JPMDE(duration), timeout);//1 second timeout

			result = (double)Await.result(future, timeout.duration());
		}
		catch (Exception e) {
			throw new DrinkExchangeException(e.getMessage());
		}

		return result;
	}
}
