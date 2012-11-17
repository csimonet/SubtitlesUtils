package com.simonetti.extractzip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractZIP {

	public ExtractZIP() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Dimensione Buffer - sonar tips(Magic Number)
	 */	
	private static int dimBuffer = 1024;
	
	
	/**
	 * Extracts the contents of the zip file passed to it.
	 * <li>If the zip entry in zip file is a directory, will be skipped</li>
	 * 
	 * @param File
	 */
	public void extractZip(File f){	
		
		 try
       {
               /*
                * STEP 1 : Create directory with the name of the zip file
                *
                * For e.g. if we are going to extract c:/demo.zip create c:/demo
                * directory where we can extract all the zip entries
                *
                */                
               String zipPath = f.getParent();//strZipFile.substring(0, strZipFile.length()-4);
//               File temp = new File(zipPath);
//               temp.mkdir();
               System.out.println(zipPath + " created");
              
               /*
                * STEP 2 : Extract entries while creating required
                * sub-directories
                *
                */
               ZipFile zipFile = new ZipFile(f);
               Enumeration<? extends ZipEntry> e = zipFile.entries();
              
               while(e.hasMoreElements())
               {
                       ZipEntry entry = (ZipEntry)e.nextElement();
                       File destinationFilePath = new File(zipPath+"\\"+ entry.getName());


//                       System.out.println(destinationFilePath.getParentFile());
//                       System.out.println(destinationFilePath.getParent());
//                       System.out.println(destinationFilePath.getAbsolutePath());
//                       System.out.println(destinationFilePath.getCanonicalPath());
//                       System.out.println(destinationFilePath.getName());
//                       System.out.println(destinationFilePath.getPath());
                       
                       
                       //create directories if required.
                      // destinationFilePath.getParentFile().mkdirs();
                      
                       //if the entry is directory, leave it. Otherwise extract it.
                       if(entry.isDirectory())
                       {
                           //TODO La entry è una directory:entra nella directory ed estraine il contenuto     
                       	continue;
                       }
                       else
                       {
                               System.out.println("Extracting " + destinationFilePath);
                              
                               /*
                                * Get the InputStream for current entry
                                * of the zip file using
                                *
                                * InputStream getInputStream(Entry entry) method.
                                */
                               BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                               
                                                                                                              
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
                       }
               }
       }
       catch(IOException ioe)
       {
               System.out.println("IOError :" + ioe);
       }	
	}
	
	
	/**
	 * Extracts the contents of the zip file passed to it.
	 * <li>If the zip entry in zip file is a directory, will be skipped</li>
	 * <li>If the zip entry name contains or equals to {@code String skipFile}, will be skipped</li>
	 * 
	 * @param File, String
	 * 
	 */
	public void extractZip(File f,String skipFile){	
		
		 try
        {
                /*
                 * STEP 1 : Create directory with the name of the zip file
                 *
                 * For e.g. if we are going to extract c:/demo.zip create c:/demo
                 * directory where we can extract all the zip entries
                 *
                 */                
                String zipPath = f.getParent();//strZipFile.substring(0, strZipFile.length()-4);
//                File temp = new File(zipPath);
//                temp.mkdir();
                System.out.println(zipPath + " created");
               
                /*
                 * STEP 2 : Extract entries while creating required
                 * sub-directories
                 *
                 */
                ZipFile zipFile = new ZipFile(f);
                Enumeration<? extends ZipEntry> e = zipFile.entries();
               
                while(e.hasMoreElements())
                {
                        ZipEntry entry = (ZipEntry)e.nextElement();
                        File destinationFilePath = new File(zipPath+"\\"+ entry.getName());


//                        System.out.println(destinationFilePath.getParentFile());
//                        System.out.println(destinationFilePath.getParent());
//                        System.out.println(destinationFilePath.getAbsolutePath());
//                        System.out.println(destinationFilePath.getCanonicalPath());
//                        System.out.println(destinationFilePath.getName());
//                        System.out.println(destinationFilePath.getPath());
                        
                        
                        //create directories if required.
                       // destinationFilePath.getParentFile().mkdirs();
                       
                        //if the entry is directory, leave it. Otherwise extract it.
                        if(entry.isDirectory() || entry.getName().contains(skipFile) || entry.getName().equals(skipFile))
                        {
                            //TODO La entry è una directory:entra nella directory ed estraine il contenuto     
                        	continue;
                        }
                        else
                        {
                                System.out.println("Extracting " + destinationFilePath);
                               
                                /*
                                 * Get the InputStream for current entry
                                 * of the zip file using
                                 *
                                 * InputStream getInputStream(Entry entry) method.
                                 */
                                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                                
                                                                                                               
                                int b;
                                byte buffer[] = new byte[dimBuffer];

                                /*
                                 * read the current entry from the zip file, extract it
                                 * and write the extracted file.
                                 */
                                FileOutputStream fos = new FileOutputStream(destinationFilePath);
                                BufferedOutputStream bos = new BufferedOutputStream(fos, dimBuffer);

                                while ((b = bis.read(buffer, 0, dimBuffer)) != -1) {
                                	bos.write(buffer, 0, b);
                                }
                               
                                //flush the output stream and close it.
                                bos.flush();
                                bos.close();
                               
                                //close the input stream.
                                bis.close();
                        }
                }
        }
        catch(IOException ioe)
        {
                System.out.println("IOError :" + ioe);
        }	
	}

}
