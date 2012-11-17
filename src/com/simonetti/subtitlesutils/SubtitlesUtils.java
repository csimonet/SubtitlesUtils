package com.simonetti.subtitlesutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.simonetti.extractzip.ExtractZIP;
import com.simonetti.subtitlesutils.obj.SubtitleObj;
import com.simonetti.subtitlesutils.obj.VideoObj;

public final class SubtitlesUtils {
	
	/**
	 * Private constructor - sonar tips (Utility class must have private constructor)
	 */
	private SubtitlesUtils (){		
		
	}
	
	/**
	 * Enables or disables debug mode
	 */
	private static boolean debug = false;
	
	
	/**
	 * Video container
	 */
	private static List <VideoObj> videoContainer;

	/**
	 * Video container
	 */	
	private static List <SubtitleObj> subContainer;
	
	/**
	 * Dimensione Buffer - sonar tips(Magic Number)
	 */	
	private static int dimBuffer = 1024;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main (String[] args) throws IOException {
				
		for(int numArg = 0; numArg < args.length; numArg++){
			if(args[numArg].compareTo("-debug") == 0){
				debug = true;
				continue;
			}
		}		
		
		
		//=Path del config
		String pathConfig = "E:\\DOWNLOAD TORRENT COMPLETI\\MovingFile SCRIPT\\configScriptJava.ini";
		String pathSrc= null;
		                    
        //creazione BufferedReader per leggere il csv
        BufferedReader br = new BufferedReader( new FileReader(pathConfig));
        
        //Stringa in cui verrà caricato volta per volta il contenuto del rigo
        String strLine = "";
 
                   
        //lettura del file riga per riga
         while( (strLine = br.readLine()) != null)
             {
         		if (strLine.startsWith("SRC_EXTRACT:")){         			
         			pathSrc = strLine.substring((strLine.indexOf(':')+1));
         			if (debug){print(pathSrc);}
         			break;         			
         			}
         		}
         br.close();
         		
		File folder = new File(pathSrc);
		
		
		if (folder.exists()){
			
			if(debug){ print("Path existence: "+folder.exists());}
			
//		  	long start=System.nanoTime();  //currentTimeMillis();			
			if(debug){ print("searchZIPFile - START");}
			search_extractZIPFile(folder);
			if(debug){ print("searchZIPFile - END");}
//			long end=System.nanoTime();
//		    System.out.println("Tempo di esecuzione search_extractZIPFile() "+((end-start)/1000000)+" millisec.");
		
//		    start=System.nanoTime();  //currentTimeMillis();
		    if(debug){ print("searchSRTFile - START");}
			searchSRTFile(folder);
			if(debug){ print("searchSRTFile - END");}
//			end=System.nanoTime();
//		    System.out.println("Tempo di esecuzione searchSRTFile() "+((end-start)/1000000)+" millisec.");
			
//		    start=System.nanoTime();  //currentTimeMillis();
		    if(debug){ print("searchVideoFile - START");}
			searchVIDEOFile(folder);
			if(debug){ print("searchVideoFile - END");}
//			end=System.nanoTime();
//		    System.out.println("Tempo di esecuzione searchVIDEOFile() "+((end-start)/1000000)+" millisec.");
			
//		    start=System.nanoTime();  //currentTimeMillis();
			if(debug){ print("matchVIDEOandSUB - START");}
			matchVIDEOandSUB(folder);
			if(debug){ print("matchVIDEOandSUB - END");}
//			end=System.nanoTime();
//		    System.out.println("Tempo di esecuzione matchVIDEOandSUB() "+((end-start)/1000000)+" millisec.");
			
		}
		
	}	


	/**
	 * Search and extract zipFile in directory {@code File dir}
	 * @param 
	 */
	public static void search_extractZIPFile(File dir) {
		
		File[] listOfFiles = dir.listFiles();
		ExtractZIP extZIP = new ExtractZIP();
		
		for (int i = 0; i < listOfFiles.length; i++) {			
			
			if(listOfFiles[i].isFile()) {
				
				if(listOfFiles[i].getName().endsWith("itasa.zip")){
					extZIP.extractZip(listOfFiles[i],"tag");					
				}else if (listOfFiles[i].getName().endsWith("subsfactory.zip")){
					extZIP.extractZip(listOfFiles[i],"720p");
				}				
			}
		}
	}
	
	
	/**
	 * Search srtFile in directory {@code File dir}
	 * @param 
	 */
	public static void searchSRTFile(File dir) {
	
		File[] listOfFiles = dir.listFiles(); 
		subContainer = new ArrayList<SubtitleObj>();	
		String nameOfFile = null;
		
		for (int i = 0; i < listOfFiles.length; i++) {			
			
			if(listOfFiles[i].isFile()) 
			{				
				nameOfFile = listOfFiles[i].getName();
				if(nameOfFile.endsWith("itasa.srt") ||(nameOfFile.endsWith("subsfactory.srt"))){					
					SubtitleObj sub = new SubtitleObj(listOfFiles[i]);
					subContainer.add(sub);					
				}
			}			
		}
		
		if(debug){ print("Scorrimento subContainer");}
		for (SubtitleObj obj: subContainer){			
			print("Titolo:"+obj.getTitle()+" - Episodio:"+obj.getEpisode());//+" - Path:"+obj.getSub().getPath());			
		}		
	}
	
	/**
	 * Search videoFile in directory {@code File dir}
	 * @param 
	 */
	public static void searchVIDEOFile(File dir) {	
	
		File[] listOfFiles = dir.listFiles(); 
		videoContainer = new ArrayList<VideoObj>();	
		String nameOfFile = null;
		for (int i = 0; i < listOfFiles.length; i++) 
		{		
			if (listOfFiles[i].isFile()) 
			{
				nameOfFile = listOfFiles[i].getName();
				if (nameOfFile.endsWith(".mp4")|| nameOfFile.endsWith(".avi")|| nameOfFile.endsWith(".mkv")){
					VideoObj video = new VideoObj(listOfFiles[i]);
					videoContainer.add(video);					
				}
			}
		}
		
		if(debug){ print("Scorrimento videoContainer");}
		for (VideoObj obj: videoContainer){			
			print("Titolo:"+obj.getTitle()+" - Episodio:"+obj.getEpisode());//+" - Path:"+obj.getVideo().getPath().substring(0,obj.getVideo().getPath().lastIndexOf(".")));			
		}
		
		
	}
	
	public static void matchVIDEOandSUB(File folder) throws IOException {

		for (Iterator<VideoObj> itVideo = videoContainer.iterator(); itVideo.hasNext(); ){
			VideoObj objVideo = itVideo.next();
			for (Iterator<SubtitleObj> itSub = subContainer.iterator(); itSub.hasNext();){
				
				SubtitleObj objSub = itSub.next();
				
				if(objVideo.getTitle().equals(objSub.getTitle())){
					if(objVideo.getEpisode().equals(objSub.getEpisode())){
						
						if(debug){print(new StringBuilder("Video:").append(objVideo.getVideo().getName()).append("Subtitle:").append(objSub.getSub().getName()).toString());}
						

						String destinationFilePath = objVideo.getVideo().getPath().substring(0,objVideo.getVideo().getPath().lastIndexOf("."))+".srt";
						//objSub.getSub().renameTo(arg0)
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(objSub.getSub()));
                        
                        
                        int b;
                        byte buffer[] = new byte[dimBuffer];

                        /*
                         * read the current entry from the zip file, extract it
                         * and write the extracted file.
                         */
                        FileOutputStream fos = new FileOutputStream(destinationFilePath);
                        BufferedOutputStream bos = new BufferedOutputStream(fos,dimBuffer);

                        while ((b = bis.read(buffer, 0, dimBuffer)) != -1) {
                     	   bos.write(buffer, 0, b);
                        }
                       
                        //flush the output stream and close it.
                        bos.flush();
                        bos.close();
                       
                        //close the input stream.
                        bis.close();
                        
                        if(debug){print("Rimozione:"+objSub.getSub().getName());}
        				
                        //delete subtitle from hardisk
                        objSub.getSub().delete();   
                        
                        //remove "objSub" from subContainer using iterator.remove()
                        itSub.remove();
        				
                        // stop cycle; go to the next element of videoContainer
                        break;        				
					}					
				}			
			}
			//TODO flush dei container e rimozione srt non matchati e zip matchati
			itVideo.remove();
		}
		
	}
	
	
	public static void print(String msg) {		
		System.out.println(msg);	
	}

}


