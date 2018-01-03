package pe.cpebs.procesos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	
	public static Calendar stringToCalendar(String stringDate, String datePattern) {	    
	    if (stringDate == null) {
	      return null;
	    }
	 
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
	    try {
	    	calendar.setTime(simpleDateFormat.parse(stringDate));
	    }
	    catch (ParseException e) {	      	      
	        calendar = null;
	        LOGGER.error("Error parseando fecha", e);
	    }	
	    return calendar;
	}	
}
