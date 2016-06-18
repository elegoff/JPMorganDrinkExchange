package com.jpmc.elegoff;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.jpmc.elegoff.model.Stock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jpmc.elegoff.model.StockType;

public class StockHelper {

	

	
	/**
	 * @param symbol : Stock symbol
	 * @param stocks : a List of Stock objects
	 * @return the Stock instance (if any) for the given symbol
	 * @throws NoSuchElementException
	 */
	public static Stock getStockBySymbol(String symbol, List<Stock> stocks) throws NoSuchElementException{
		if (symbol == null || symbol.isEmpty()){
			return null;
		}
		
		Optional<Stock> li = stocks.stream().filter(s ->s.getSymbol().equalsIgnoreCase(symbol)).findFirst();
		return li.get();
	}
	
	/**
	 * @return A list of Stock objects as configured in a Json file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static List<Stock> loadJson() throws FileNotFoundException, IOException, ParseException{
		List<Stock> stocks = new ArrayList<Stock>();
		
		JSONParser parser = new JSONParser();
		
		JSONObject json=(JSONObject)parser.parse(new FileReader("Stocks.json"));
		JSONArray stockList = (JSONArray)json.get("Stock List");
		@SuppressWarnings("rawtypes")
		Iterator it = stockList.iterator();
		while(it.hasNext()){
			JSONObject jsonStock = (JSONObject) it.next();
			String symbol = (String) jsonStock.get("symbol");
			String st = (String)jsonStock.get("stockType");
			StockType stockType = StockType.valueOf(st);
			double lastDividend = ((Number)jsonStock.get("lastDividend")).doubleValue();
			int fixedDividend =  ((Number)jsonStock.get("fixedDividend")).intValue();
			double parValue =  ((Number)jsonStock.get("parValue")).doubleValue();
			stocks.add(new Stock(symbol, stockType, lastDividend, fixedDividend, parValue));
		}
		
		return stocks;
	}
	
}
