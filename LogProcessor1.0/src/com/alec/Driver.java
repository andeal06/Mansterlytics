package com.alec;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.alec.db.utilities.DBBuilder;

public class Driver {
	
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	
	public static void main(String[] args) throws Exception {
		// Takes a directory and sends all .json files to be processed
		LOGGER.info("Starting Directory Processing");
		
		String path = args[0];
		Path myDir = Paths.get(path);
		
		LOGGER.info(String.format("Processing %d files.", myDir.toFile().listFiles().length));
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(myDir, "*.{json}")) {
			for (Path entry : stream) {
				DBBuilder.buildTables(entry.toFile());
			}
		} catch (IOException x) {
			LOGGER.severe(x.toString());
		}
		LOGGER.info("Completed Directory Scraping");
	
		
	}
}
