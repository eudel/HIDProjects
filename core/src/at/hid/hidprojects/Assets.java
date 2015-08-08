package at.hid.hidprojects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public static void load() {
		HIDProjects.assets.load("ui/gui.json", Skin.class);
	}
	
	public static void playSound(Sound sound) {
		if (Gdx.app.getPreferences(HIDProjects.TITLE).getFloat("sound") != 0) sound.play(Gdx.app.getPreferences(HIDProjects.TITLE).getFloat("sound"));
	}
	
	public static void playMusic(Music music) {
		music.setVolume(Gdx.app.getPreferences(HIDProjects.TITLE).getFloat("music"));
		if (Gdx.app.getPreferences(HIDProjects.TITLE).getFloat("music") != 0) music.play();
	}
}
