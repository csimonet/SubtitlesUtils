package com.simonetti.subtitlesutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.simonetti.extractzip.ExtractZIP;
import com.simonetti.subtitlesutils.obj.SubtitleObj;
import com.simonetti.subtitlesutils.obj.VideoObj;

public class SubtitlesUtils {
	
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
		
//		String nameOfFile;
		File folder = new File("D:\\automatizzazione subs\\");
		//File folderDest = new File(pathDest);		
		
		if (folder.exists()){
			
			if(debug) print("Path existence: "+folder.exists());
			
			if(debug) print("searchZIPFile - START");
			search_extractZIPFile(folder);
			if(debug) print("searchZIPFile - END");
		
			if(debug) print("searchSRTFile - START");
			searchSRTFile(folder);
			if(debug) print("searchSRTFile - END");
			
			if(debug) print("searchVideoFile - START");
			searchVIDEOFile(folder);
			if(debug) print("searchVideoFile - END");
			
			if(debug) print("matchVIDEOandSUB - START");
			matchVIDEOandSUB(folder);
			if(debug) print("matchVIDEOandSUB - END");
			
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
		
		if(debug) print("Scorrimento subContainer");
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
		
		if(debug) print("Scorrimento videoContainer");
		for (VideoObj obj: videoContainer){			
			print("Titolo:"+obj.getTitle()+" - Episodio:"+obj.getEpisode()+" - Path:"+obj.getVideo().getPath().substring(0,obj.getVideo().getPath().lastIndexOf(".")));			
		}
		
		
	}
	
	public static void matchVIDEOandSUB(File folder) throws IOException {

		for (Iterator<VideoObj> itVideo = videoContainer.iterator(); itVideo.hasNext(); ){
			VideoObj objVideo = itVideo.next();
			for (Iterator<SubtitleObj> itSub = subContainer.iterator(); itSub.hasNext();){
				
				SubtitleObj objSub = itSub.next();
				
				if(objVideo.getTitle().equals(objSub.getTitle())){
					if(objVideo.getEpisode().equals(objSub.getEpisode())){
						
						print("Video:"+objVideo.getVideo().getName());
						print("Subtitle:"+objSub.getSub().getName());						
						

						String destinationFilePath = objVideo.getVideo().getPath().substring(0,objVideo.getVideo().getPath().lastIndexOf("."))+".srt";
						//objSub.getSub().renameTo(arg0)
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(objSub.getSub()));
                        
                        
                        int b;
                        byte buffer[] = new byte[2048];

                        /*
                         * read the current entry from the zip file, extract it
                         * and write the extracted file.
                         */
                        FileOutputStream fos = new FileOutputStream(destinationFilePath);
                        BufferedOutputStream bos = new BufferedOutputStream(fos,1024);

                        while ((b = bis.read(buffer, 0, 1024)) != -1) {
                     	   bos.write(buffer, 0, b);
                        }
                       
                        //flush the output stream and close it.
                        bos.flush();
                        bos.close();
                       
                        //close the input stream.
                        bis.close();
                        
        				print("Rimozione:"+objSub.getSub().getName());
        				objSub.getSub().delete();   
        				itSub.remove();
        				continue;
					}					
				}				
				
			}
			//TODO flush dei container e rimozione srt non matchati e zip matchati
//			videoContainer.remove(objVideo);
		}
		
	}
	
	
//	public static void matchVIDEOandSUB(File folder) throws IOException {
//
//		for (VideoObj objVideo: videoContainer){	
//			for (SubtitleObj objSub: subContainer){	
//				
//				if(objVideo.getTitle().equals(objSub.getTitle())){
//					if(objVideo.getEpisode().equals(objSub.getEpisode())){
//						
//						print("Video:"+objVideo.getVideo().getName());
//						print("Subtitle:"+objSub.getSub().getName());						
//						
//
//						String destinationFilePath = objVideo.getVideo().getPath().substring(0,objVideo.getVideo().getPath().lastIndexOf("."))+".srt";
//						//objSub.getSub().renameTo(arg0)
//                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(objSub.getSub()));
//                        
//                        
//                        int b;
//                        byte buffer[] = new byte[2048];
//
//                        /*
//                         * read the current entry from the zip file, extract it
//                         * and write the extracted file.
//                         */
//                        FileOutputStream fos = new FileOutputStream(destinationFilePath);
//                        BufferedOutputStream bos = new BufferedOutputStream(fos,1024);
//
//                        while ((b = bis.read(buffer, 0, 1024)) != -1) {
//                     	   bos.write(buffer, 0, b);
//                        }
//                       
//                        //flush the output stream and close it.
//                        bos.flush();
//                        bos.close();
//                       
//                        //close the input stream.
//                        bis.close();
//                        
//        				print("Rimozione:"+objSub.getSub().getName());
//        				objSub.getSub().delete();   
//        				subContainer.remove(objSub);
//        				continue;
//					}					
//				}				
//				
//			}
////			videoContainer.remove(objVideo);
//		}
//		
//	}
	
	

	
	public static void print(String msg) {		
		System.out.println(msg);	
	}

}


//private static void searchSRTFile(File fold) {
//
//	File[] listOfFiles = fold.listFiles(); 
//	List <SubtitleObj> contenitoreItasa = new ArrayList<SubtitleObj>();
//	List <SubtitleObj> contenitoreSubsFactory = new ArrayList<SubtitleObj>();
//	
//	for (int i = 0; i < listOfFiles.length; i++) {			
//		
//		if(listOfFiles[i].isFile()) {
//			
//			if(listOfFiles[i].getName().endsWith("itasa.srt")){					
////				print(listOfFiles[i].getName());
//				SubtitleObj subIS = new SubtitleObj(listOfFiles[i]);
//				contenitoreItasa.add(subIS);					
//				
//			}else if (listOfFiles[i].getName().endsWith("subsfactory.srt")){// && !(listOfFiles[i].getName().contains("720p"))){					
////				print(listOfFiles[i].getName());	
//				SubtitleObj subSF = new SubtitleObj(listOfFiles[i]);
//				contenitoreSubsFactory.add(subSF);
//			}				
//		}
//	}		
//	
//	if(debug) print("Scorrimento contenitoreItasa");
//	for (SubtitleObj is: contenitoreItasa){			
//		print("Titolo:"+is.getTitolo()+" - Episodio:"+is.getEpisodio());			
//	}
//	
//	
//	if(debug) print("Scorrimento contenitoreSubsfactory");		
//	for (SubtitleObj sf: contenitoreSubsFactory){			
//		print("Titolo:"+sf.getTitolo()+" - Episodio:"+sf.getEpisodio());		
//	}		
//	
//}


