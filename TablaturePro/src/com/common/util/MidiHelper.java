package com.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import com.common.bo.MidiNote;

public class MidiHelper {
	
	
	public static Map<Long, List<MidiNote>> getMidiNote(File midiFie) throws InvalidMidiDataException, IOException {		
		Sequence sequence = MidiSystem.getSequence(midiFie);
		Map<Long, List<MidiNote>> noteMap = new HashMap<>();		
		for (Track track :  sequence.getTracks()) {
        	for (int i=0; i < track.size(); i++) { 
        		MidiEvent event = track.get(i);       		
        		MidiMessage message = event.getMessage();
        		if (message instanceof ShortMessage) {
        			ShortMessage sm = (ShortMessage) message;
        			if (sm.getCommand() == CommonConstants.NOTE_ON) {
        				if(sm.getData2()>0) {  
        					long tick = event.getTick();
        					if(noteMap.get(tick) == null) {
        						noteMap.put(tick, new ArrayList<MidiNote>());
        					}
        					noteMap.get(tick).add(new MidiNote(sm.getData1(), sm.getData2()));      					
        				}
        			}
        		}       		
        	}          
        } 		
		return noteMap;	
	}
	
	
	public static void playNoteMap(Map<Long, List<MidiNote>> noteMap,  List<Long> sortedKey, int instr, long sleep1, long sleep2) throws InterruptedException, MidiUnavailableException {
		Synthesizer syn = MidiSystem.getSynthesizer();
		syn.open();  
		final MidiChannel[] mc = syn.getChannels();
		Instrument[] instruments = syn.getDefaultSoundbank().getInstruments();
		syn.loadInstrument(instruments[instr]);		
		for(Long key:sortedKey) {
			String noteLine = "@"+String.format("%04d", key);
			List<MidiNote> notes = noteMap.get(key);
			int channel = 1;
			for(MidiNote note:notes) {
				mc[channel++].noteOn(note.getKey(),note.getVelocity());
				noteLine = noteLine +  "     " + note.getNoteName() + "(" + note.getNote() + ") - " + note.getVelocity(); 
			}
			System.out.println(noteLine);
			if(sleep1>0) {
				Thread.sleep(sleep1);
			}			
			channel = 1;
			for(MidiNote note:notes) {
				mc[channel++].noteOff(note.getKey(),note.getVelocity());
			}	
			if(sleep2>0){
				Thread.sleep(sleep2);
			}
		}		
	}
	
	
	public static List<Long> getSortedKey(Map<Long, List<MidiNote>> noteMap) {
		List<Long> keySet = new ArrayList<>(noteMap.keySet());
        Collections.sort(keySet, new Comparator<Long>()
		{
			public int compare(Long o1, Long o2)
			{
				if(o1!=null && o2!=null) {
					if(o1.longValue()>o2.longValue()) {
						return 1;
					}
					else if(o1.longValue()<o2.longValue())	{				
						return -1;
					}
					else {
						return 0;
					}
				}
				else {
					return 0;
				}
			}
		});
        return keySet;
	}
	
}
