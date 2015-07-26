package at.hid.hidprojects.desktop;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import at.hid.hidprojects.HIDProjects;
import at.hid.hidprojects.desktop.api.DesktopApp42;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = HIDProjects.TITLE + " " + HIDProjects.VERSION;
		config.vSyncEnabled = true;
		config.x = 0;
		config.y = 0;
		
		GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Dimension screenResolution = new Dimension(monitor.getDisplayMode().getWidth(), monitor.getDisplayMode().getHeight());
		
		int scale = 0;
		if (screenResolution.width >= 1920 && screenResolution.height >= 1080) {
			config.width = 1920;
			config.height = 1000;
			scale = 6;
		} else if (screenResolution.width >= 1600 && screenResolution.height >= 900) {
			config.width = 1600;
			config.height = 833;
			scale = 5;
		} else if (screenResolution.width >= 1280 && screenResolution.height >= 720) {
			config.width = 1280;
			config.height = 667;
			scale = 4;
		} else if (screenResolution.width >= 960 && screenResolution.height >= 540) {
			config.width = 960;
			config.height = 500;
			scale = 3;
		} else if (screenResolution.width >= 640 && screenResolution.height >= 360) {
			config.width = 640;
			config.height = 333;
			scale = 2;
		} else {
			config.width = 320;
			config.height = 167;
			scale = 1;
		}
		
		new LwjglApplication(new HIDProjects(new DesktopApp42(), scale), config);
	}
}
