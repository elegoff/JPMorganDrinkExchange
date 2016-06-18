package com.jpmc.elegoff.actors;

import java.io.Serializable;


import com.jpmc.elegoff.model.Stock;
import com.jpmc.elegoff.model.TradeType;

/**
 * @author elegoff
 * Class which encapsulates message types handled by a TradeActor
 */
public class TradeMsg {
	
	
	/**
	 * @author elegoff
	 * Base class for Buy or Sell operations
	 */
	static abstract class BuyOrSell {
		protected Stock stock;
		protected double price;
		protected TradeType tradeType;
		
		protected  long quantity;
	}
	
	
	/**
	 * @author elegoff
	 * Message sent to TradeActor when recording a Buy trade
	 */
	public static class Buy extends BuyOrSell implements Serializable{
		
		private static final long serialVersionUID = -4532063710410940715L;
			
		public Buy(Stock stock, long quantity, double price){
			
			this.stock =stock;
			this.price=price;
			this.tradeType=TradeType.BUY;
			
			this.quantity = quantity;
			
		}
		
		
	}
	
	/**
	 * @author elegoff
	 * Message sent to TradeActor when recording a sell Trade
	 */
	public static class Sell extends BuyOrSell implements Serializable{
		
		private static final long serialVersionUID = -4532063710410940715L;
			
		public Sell(Stock stock, long quantity, double price){
			
			this.stock =stock;
			this.price=price;
			this.tradeType=TradeType.SELL;
			this.quantity = quantity;
			
		}
		
		

		
	}
	
	/**
	 * @author elegoff
	 * Message sent to TradeActor when requesting the current list of Trades
	 */
	public static class ListTrades implements Serializable{
		private static final long serialVersionUID = 6322615833687517274L;		
	}

	/**
	 * @author elegoff
	 * Message sent to TradeActor when requesting a Volume Weight calculation
	 */
	public static class VolumeWeighted implements Serializable {
		
		private static final long serialVersionUID = -782491393977362104L;
		private Stock stock;
		private int duration;
		
		
		
		public VolumeWeighted(Stock stock, int duration) {
			super();
			this.stock = stock;
			this.duration = duration;
		}
		public Stock getStock() {
			return stock;
		}
		public int getDuration() {
			return duration;
		}
		
		
		
	}
	
	/**
	 * @author elegoff
	 * Message sent to TradeActor when requesting a JPMDE calculation
	 */
	public static class JPMDE implements Serializable {

		
		private static final long serialVersionUID = -6601594088788503249L;
		
		private int duration;

		public JPMDE(int duration) {
			super();
			this.duration = duration;
		}

		public int getDuration() {
			return duration;
		}
		
		
		
		
	}


}
