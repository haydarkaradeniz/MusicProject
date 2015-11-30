
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
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import com.common.bo.MidiNote;
import com.common.util.CommonConstants;

public class Test {
	private static void put(Map<Long, List<MidiNote>> noteMap, int channel, long tick, MidiNote note) {
		if(noteMap.get(tick) == null) {
			noteMap.put(tick, new ArrayList<MidiNote>());
		}
		noteMap.get(tick).add(note);
	}
	
	public static void test2() throws Exception {
		Sequence sequence = MidiSystem.getSequence(new File("C:\\Users\\is96092\\Desktop\\elise.mid"));
		Map<Long, List<MidiNote>> noteMap = new HashMap<>();        
        for (Track track :  sequence.getTracks()) {
        	for (int i=0; i < track.size(); i++) { 
        		MidiEvent event = track.get(i);
        		MidiMessage message = event.getMessage();
        		if (message instanceof ShortMessage) {
        			ShortMessage sm = (ShortMessage) message;
        			if (sm.getCommand() == CommonConstants.NOTE_ON) {
        				if(sm.getData2()>0) {
        					put(noteMap, sm.getChannel(), event.getTick(), new MidiNote(sm.getData1(), sm.getData2()));        					
        				}
        			}
        		}       		
        	}          
        } 
        
        System.out.println("Track");
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
        for(Long key:keySet) {        	
			List<MidiNote> notes = noteMap.get(key);
			String noteLine = "@"+String.format("%04d", key);
			for(MidiNote note:notes) {
				noteLine = noteLine +  "     " + note.getNoteName() + "(" + note.getNote() + ") - " + note.getVelocity(); 
			}
			System.out.println(noteLine);
        }
        play(keySet, noteMap);
	}
	
	private static void play(List<Long> keySet, Map<Long, List<MidiNote>> noteMap ) {
		try {
			Synthesizer syn = MidiSystem.getSynthesizer();
			syn.open();  
			final MidiChannel[] mc = syn.getChannels();
			Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
			syn.loadInstrument(instr[90]);
			
			for(Long key:keySet) {
				List<MidiNote> notes = noteMap.get(key);
				int channel = 1;
				for(MidiNote note:notes) {
					mc[channel++].noteOn(note.getKey(),note.getVelocity());
				}
				Thread.sleep(200);
				channel = 1;
				for(MidiNote note:notes) {
					mc[channel++].noteOff(note.getKey(),note.getVelocity());
				}		
//				Thread.sleep(200);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) throws Exception {
    	test1();
    }
    
    
    public static void test1() throws InvalidMidiDataException, IOException {
    	 Sequence sequence = MidiSystem.getSequence(new File("C:\\Users\\is96092\\Desktop\\furelise.mid"));

         int trackNumber = 0;
         for (Track track :  sequence.getTracks()) {
             trackNumber++;
             System.out.println("Track " + trackNumber + ": size = " + track.size());
             System.out.println();
             for (int i=0; i < track.size(); i++) { 
                 MidiEvent event = track.get(i);
                 System.out.print("@" + event.getTick() + " ");
                 MidiMessage message = event.getMessage();
                 if (message instanceof ShortMessage) {
                     ShortMessage sm = (ShortMessage) message;
                     System.out.print("Channel: " + sm.getChannel() + " ");
                     if (sm.getCommand() == CommonConstants.NOTE_ON) {
                         int key = sm.getData1();
                         int octave = (key / 12)-1;
                         int note = key % 12;
                         String noteName = CommonConstants.NOTE_NAMES[note];
                         int velocity = sm.getData2();
                         System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                     } else if (sm.getCommand() == CommonConstants.NOTE_OFF) {
                         int key = sm.getData1();
                         int octave = (key / 12)-1;
                         int note = key % 12;
                         String noteName = CommonConstants.NOTE_NAMES[note];
                         int velocity = sm.getData2();
                         System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                     } else {
                         System.out.println("Command:" + sm.getCommand());
                     }
                 } else {
                     System.out.println("Other message: " + message.getClass());
                 }
             }

             System.out.println();
         }

    }
}