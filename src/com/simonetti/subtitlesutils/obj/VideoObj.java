package com.simonetti.subtitlesutils.obj;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoObj {
	
	File video;
	String title;
	String episode;
	
	
	/**
	 * Constructor method
	 */
	public VideoObj(File f) {
		
		this.video=f;
		extractInfo();		
	}


	private void extractInfo() {
		
		String tmp = null;
		String tmpTitle= null;
		String tmpEpisode= null;
		
		tmp = this.video.getName();//Example: "Fringe.S05E02.HDTV.x264-LOL.mp4"
		
		String [] splitArray = tmp.split("\\.");//Splits the string by using the dot as a regex
		
		tmpTitle = splitArray[0].toLowerCase();//the first element of array is always the title
		
		String pattern = "(s[0-9]{2}e[0-9]{2}|e[0-9]*)" ;//search pattern like this "s05e02" or like this "e366"
		
		
		Pattern MY_PATTERN = Pattern.compile(pattern);
		for (int i = 1; i< splitArray.length; i++){
		Matcher m = MY_PATTERN.matcher(splitArray[i].toLowerCase());//toLowerCase() it's necessary because the matcher 
																	//searches only lowercase occurrences  of pattern
		      while (m.find()) {
		          String s = m.group();			     
				  tmpEpisode = s;
		      }		
		}
		
		setTitle(tmpTitle);
		setEpisode(tmpEpisode);
	}


	/**
	 * @return the sub
	 */
	public File getVideo() {
		return video;
	}


	/**
	 * @param sub the sub to set
	 */
	public void setVideo(File video) {
		this.video = video;
	}


	/**
	 * @return the titolo
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param titolo the titolo to set
	 */
	public void setTitle(String titolo) {
		this.title = titolo;
	}


	/**
	 * @return the episodio
	 */
	public String getEpisode() {
		return episode;
	}


	/**
	 * @param episodio the episodio to set
	 */
	public void setEpisode(String episodio) {
		this.episode = episodio;
	}


}
