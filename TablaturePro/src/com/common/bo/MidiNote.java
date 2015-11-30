package com.common.bo;

import com.common.util.CommonConstants;

public class MidiNote {
	
	 private int key;
     private int octave;
     private int note;
     private String noteName;
     private int velocity;
     
     public MidiNote(int key, int velocity) {
    	this.key = key;
    	this.velocity = velocity;    	 
    	this.octave = (key / 12)-1;
        this.note = key % 12;
        this.noteName = CommonConstants.NOTE_NAMES[note];
     }

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
     
     
}
