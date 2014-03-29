package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioImpl;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class AudioGame extends AudioImpl {

	public static String filePlaying = ""; 

	public static boolean playing = false; 
	public static HashMap<String, Audio> map = new HashMap<>(10);

	public static String []filesPlaying; 
	public static final AudioGame audioGame  = new AudioGame();

	public static AudioGame getInstance() {
		return audioGame;
	}

	private AudioGame(){
		File soundsFolder= new File("res/Sounds");

		File[] files = soundsFolder.listFiles();

		try{
			for (File file: files){
				String fileName = file.getName();
				Audio oggStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource(String.format("res/Sounds/%s", fileName)));
				map.put(fileName, oggStream);
			}
		}
		catch (Exception exc){
			exc.printStackTrace();
		}
	}

	public final static void play(String path){

		Audio oggStream = map.get(path);
		if (!playing)
		{
			oggStream.playAsSoundEffect(1.0f, 0.8f, false);
			playing = true; 
			new javax.swing.Timer(3000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AudioGame.playing = false; 
				}
			}).start();
		}

		
	}


}
