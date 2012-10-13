package com.simonetti.subtitlesutils;

import java.io.File;
import java.io.IOException;

import com.simonetti.extractzip.ExtractZIP;

public class SubtitlesUtils {
	
	private static boolean debug = false;	

	/**
	 * @param args
	 */
	public static void main (String[] args) throws IOException
	{
				
		for(int numArg = 0; numArg < args.length; numArg++){
			if(args[numArg].compareTo("-debug") == 0){
				debug = true;
				continue;
			}
		}		

		
//		String nameOfFile;
		File folder = new File("D:\\automatizzazione subs\\");
		//File folderDest = new File(pathDest);		
		System.out.println("Esistenza folderSrc: "+folder.exists());
		//System.out.println("Esistenza folderDest: "+folderDest.exists());
		
		
		if(debug) print("searchZIPFile - START");
		searchZIPFile(folder);
		if(debug) print("searchZIPFile - END");
		
	}	
	
	public static void searchZIPFile(File dir) {
		
		File[] listOfFiles = dir.listFiles();
		ExtractZIP extZIP = new ExtractZIP();
		
		for (int i = 0; i < listOfFiles.length; i++) {			
			
			if(listOfFiles[i].isFile()) {
				
				if(listOfFiles[i].getName().endsWith("itasa.zip")){
					extZIP.extractZip(listOfFiles[i]);					
				}else if (listOfFiles[i].getName().endsWith("subsfactory.zip")){
					extZIP.extractZip(listOfFiles[i]);
				}				
			}
		}
	}
	
	
	
	public static void print(String msg) {		
		System.out.println(msg);		
	}

}

