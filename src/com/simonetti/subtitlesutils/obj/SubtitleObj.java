package com.simonetti.subtitlesutils.obj;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitleObj {
	
	File sub;
	String title;
	String episode;
	
	
	/**
	 * Constructor method
	 */
	public SubtitleObj(File f) {
		
		this.sub=f;
		extractInfo();		
	}


	private void extractInfo() {
		
		String tmp = null;
		String tmpTitle= null;
		String tmpEpisode= null;
		
		tmp = this.sub.getName();//Example: "Fringe.s05e02.sub.itasa.srt"
		
		String [] splitArray = tmp.split("\\.");//Splits the string by using the dot as a regex
		
		tmpTitle = splitArray[0].toLowerCase();//the first element of array is always the title
		if(tmpTitle.startsWith("the")||tmpTitle.startsWith("2")){
			//This is necessary if name of file is like this The.Big.Bang.Theory.S06E02.HDTV.x264-LOL.mp4
			tmpTitle = new StringBuilder(tmpTitle).append(".").append(splitArray[1]).toString();
			
		}
		
		String pattern = "(s[0-9]{2}e[0-9]{2}|e[0-9]+)" ;//search pattern like this "s05e02" or like this "e366"
		//e[0-9]*: prendeva la "e" seguita da 0 o più occorrenze numeriche e quindi se gli altri elementi 
		//dell'array contenevano una "e" veniva presa.
		//e[0-9]+:prende la "e" seguita da almeno 1 o più occorrenze numeriche. SCELTA GIUSTA
		
		
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
	public File getSub() {
		return sub;
	}


	/**
	 * @param sub the sub to set
	 */
	public void setSub(File sub) {
		this.sub = sub;
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
