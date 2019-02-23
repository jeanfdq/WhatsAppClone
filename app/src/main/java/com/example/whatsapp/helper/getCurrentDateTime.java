package com.example.whatsapp.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class getCurrentDateTime {

	public static String getDateTime() {

		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");
		return dateformat.format(c.getTime());
	}


}
