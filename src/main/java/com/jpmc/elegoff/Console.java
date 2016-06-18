package com.jpmc.elegoff;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.budhash.cliche.ShellFactory;

/**
 * @author elegoff
 *  Main class of the console application
 */
public class Console {

	private static Logger logger = LoggerFactory.getLogger(Console.class);
	public final static String PROMPT_TEXT ="DrinkExchange";
	
	public static void main(String[] args) {
		if (logger.isDebugEnabled()){
			logger.debug("Application is started with log level DEBUG");
		}

				try {
			ShellFactory.createConsoleShell(PROMPT_TEXT, "Type ?list (or ?l) to get a list of commands\nCTRL+C to quit", new Commands())
			.commandLoop();
		} catch (IOException e) {
			logger.error("An exception occurred" + e.getMessage());
		}
		
	}
	

	
	

}
