package com.alec;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.alec.db.utilities.BuildContainer;
import com.alec.db.utilities.BuildHelper02;
import com.alec.db.utilities.CharBoonsDBUtil;
import com.alec.db.utilities.CharDBUtil;
import com.alec.db.utilities.FightDBUtil;
import com.alec.db.utilities.MainDBUtil;
import com.alec.game.model.LogInfoRaw;
import com.alec.game.model.Players;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDriver {
	
	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		
		ObjectMapper mapper = new ObjectMapper();
		LogInfoRaw info = mapper.readValue(file, LogInfoRaw.class);
		
		System.out.println(info.getTimeStartStd() + " Raw json info");
		String stringDate = info.getTimeStartStd();
		String cutStringDate = stringDate.substring(0, 10);
		
		DateFormat myDate = new SimpleDateFormat("yyyy-MM-dd");
		
		java.util.Date utilDate = myDate.parse(cutStringDate);
		java.sql.Date timestamp = new java.sql.Date(utilDate.getTime());
		
		System.out.println(timestamp + " Current model");
		
		
		DateFormat fullParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss X");
		for (int i = 0 ; i < args.length ; i++) {
			if (i > 0) {
				stringDate = args[i];
			}
			java.util.Date fullTest = fullParse.parse(stringDate);
			
			System.out.println(fullTest + " util.Date object");
			
			java.sql.Timestamp timeTest2 = new java.sql.Timestamp(fullTest.getTime());
			System.out.println(timeTest2 + " sql.Timestamp ");
			System.out.println(timeTest2.getTime());
			System.out.println(fullParse.format(timeTest2));
		}
		
		
		
	}
}
