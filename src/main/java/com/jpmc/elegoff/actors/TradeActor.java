package com.jpmc.elegoff.actors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import akka.actor.*;
import akka.japi.Creator;
import com.jpmc.elegoff.Calculator;
import com.jpmc.elegoff.model.Stock;
import com.jpmc.elegoff.model.Trade;
import com.jpmc.elegoff.model.TradeType;


/**
 * @author elegoff
 * Actor classes handling Trade related actions and keeping track of list of recorded Trade objects 
 */
public class TradeActor extends UntypedActor{
	
	//keep track of all trades	
	private List<Trade> trades;

	/* (non-Javadoc)
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof TradeMsg.ListTrades){
			getSender().tell(trades, getSelf());
		}
		else if (msg instanceof TradeMsg.BuyOrSell){
			TradeMsg.BuyOrSell buyOrSell =(TradeMsg.BuyOrSell) msg;
			getSender().tell(recordTrade(buyOrSell.stock,buyOrSell.quantity, buyOrSell.tradeType, buyOrSell.price ), getSelf());
		}
		else if (msg instanceof TradeMsg.VolumeWeighted){
			TradeMsg.VolumeWeighted vw= (TradeMsg.VolumeWeighted) msg;
			getSender().tell(calcVolumeWeighted(vw.getStock(), vw.getDuration() ), getSelf());
		}
		else if (msg instanceof TradeMsg.JPMDE){
			TradeMsg.JPMDE m =(TradeMsg.JPMDE)msg;
			getSender().tell(calJPMDE(m.getDuration()), getSelf());
		}
			
	}
		
	/**
	 * @author elegoff
	 * Actor creator
	 */
	public static class TradeActorC implements Creator<TradeActor> {		
		private static final long serialVersionUID = 1L;

		@Override public TradeActor create() {
		    return new TradeActor(initTrades());
		  }
		}
	
	/**
	 * Public constructor
	 * @param trades : initial trade list
	 */
	public TradeActor(List<Trade> trades) {
		super();
		this.trades = trades;
	}



	//instanciate a Trade object and add to list
	private Trade recordTrade(Stock stock, long quantity, TradeType buyOrSell, double price){
	
		Trade trade = new Trade(stock, quantity, buyOrSell, price) ;
		this.trades.add(trade);
		
		return trade;
	}
	
	
	
	//in case we want to start with a non empty list of Trades, we may add them here later
	private static List<Trade> initTrades(){
		return new ArrayList<Trade>();
	}
	
	
	//implement the Volume Weight calculation
	private double calcVolumeWeighted(Stock stock, int duration){
		
		//current time in ms
		long now = Calendar.getInstance().getTimeInMillis();
	
		//List of stock for the last 'duration' minutes
		List<Trade> li = trades.stream()
				.filter(t -> t.getStock().getSymbol() == stock.getSymbol())
				.filter(t -> t.getTimestamp() > now - (duration * 1000 * 60 ))//duration min ago
				.collect(Collectors.toList());
		
		return Calculator.calcVolumeWeightedStockPrice(li);
	}
	
	
	//JPMDE calculation
	private double calJPMDE(int duration){
		Map<Stock, List<Trade>> groupByStocks = trades.stream()
				.collect(Collectors.groupingBy(Trade::getStock));
		List<Double> vws = groupByStocks.keySet().stream().map(s-> calcVolumeWeighted(s, duration))
				.collect(Collectors.toList());
		return Calculator.geometricMean(vws);
		
	}
	

}
