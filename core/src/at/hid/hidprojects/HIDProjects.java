package at.hid.hidprojects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import at.hid.hidprojects.api.App42;
import at.hid.hidprojects.screens.Splash;


public class HIDProjects extends Game {
	public static final String TITLE = "HIDProjects", VERSION = "0.0.1-alpha", PATH = ".hidprojects";
	public static int scale;
	public static boolean DEBUG, EXT;
	public static Profile profile = new Profile();
	public static I18NBundle langBundle;
	public static final AssetManager assets = new AssetManager();
	public static App42 app42 = null;
	
	public HIDProjects(App42 app42, int scale) {
		HIDProjects.app42 = app42;
		HIDProjects.scale = scale;
	}
	
	/**
	 * creates the language bundle
	 */
	public static void createLangBundle(String lang) {
		FileHandle fhLang = Gdx.files.internal("lang/Language");
		Locale locale = new Locale(lang);
		langBundle = I18NBundle.createBundle(fhLang, locale);
	}

	/**
	 * @return the langBundle
	 */
	public static I18NBundle getLangBundle() {
		return langBundle;
	}

	public static void log(String tag, String message) {
		FileHandle fhLog = null;
		if (EXT) {
			fhLog = Gdx.files.external(PATH + "/logs/latest.log");
		} else {
			fhLog = Gdx.files.local(PATH + "/logs/latest.log");
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss: ");
		Gdx.app.log(tag, message);
		fhLog.writeString(sdf.format(date) + tag + ": " + message + "\n", true, "UTF-8");
	}

	public static void debug(String tag, String message) {
		FileHandle fhLog = null;
		if (EXT) {
			fhLog = Gdx.files.external(PATH + "/logs/latest.log");
		} else {
			fhLog = Gdx.files.local(PATH + "/logs/latest.log");
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss: ");
		Gdx.app.debug(tag, message);
		if (HIDProjects.DEBUG) {
			fhLog.writeString(sdf.format(date) + tag + ": Debug: " + message + "\n", true, "UTF-8");
		}
	}

	public static void error(String tag, String message, Throwable t) {
		FileHandle fhLog = null;
		if (EXT) {
			fhLog = Gdx.files.external(PATH + "/logs/latest.log");
		} else {
			fhLog = Gdx.files.local(PATH + "/logs/latest.log");
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss: ");
		Gdx.app.error(tag, message, t);
		fhLog.writeString(sdf.format(date) + tag + ": ERROR: " + message + "\n", true, "UTF-8");
		fhLog.writeString(t + "\n", true, "UTF-8");
	}
	
	public static void error(String tag, String message) {
		FileHandle fhLog = null;
		if (EXT) {
			fhLog = Gdx.files.external(PATH + "/logs/latest.log");
		} else {
			fhLog = Gdx.files.local(PATH + "/logs/latest.log");
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss: ");
		Gdx.app.error(tag, message);
		fhLog.writeString(sdf.format(date) + tag + ": ERROR: " + message + "\n", true, "UTF-8");
	}

	public void logrotate() {
		FileHandle fhLog = null;
		if (EXT) {
			fhLog = Gdx.files.external(PATH + "/logs/latest.log");
		} else {
			fhLog = Gdx.files.local(PATH + "/logs/latest.log");
		}
		fhLog.parent().mkdirs();
		if (fhLog.exists()) {
			byte[] buffer = new byte[1024];
			try {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
				FileOutputStream fos = null;
				if (EXT) {
					fos = new FileOutputStream(Gdx.files.external(PATH + "/logs/" + sdf.format(date) + ".zip").file());
				} else {
					fos = new FileOutputStream(Gdx.files.local(PATH + "/logs/" + sdf.format(date) + ".zip").file());
				}
				ZipOutputStream zos = new ZipOutputStream(fos);
				ZipEntry ze = new ZipEntry("latest.log");
				zos.putNextEntry(ze);
				FileInputStream in = null;
				if (EXT) {
					in = new FileInputStream(Gdx.files.external(PATH + "/logs/latest.log").file());
				} else {
					in = new FileInputStream(Gdx.files.local(PATH + "/logs/latest.log").file());
				}

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
				zos.closeEntry();
				zos.close();
				fhLog.delete();
			} catch (Exception e) {
				error(this.getClass().getName(), "error rotating log file", e);
			}
		}
	}
	
	@Override
	public void create () {
		EXT = Gdx.files.isExternalStorageAvailable();
		app42.initialize("0b5477705bcffc713ee4739170cbea834e9747722638d93bd1544aa1649f03fb", "14ed304d09f0c918fe4a0006dfc23e46678358786d3db56e4dcc9acd64e896dd");
		app42.buildUserService();
		app42.buildStorageService();

		DEBUG = Gdx.app.getPreferences(TITLE).getBoolean("debug");
		logrotate();
		if (DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG); // show debug logs 
		} else {
			Gdx.app.setLogLevel(Application.LOG_INFO);
		}
		if (Gdx.app.getPreferences(TITLE).contains("lang")) { // load saved language preferences
			String[] data = Gdx.app.getPreferences(TITLE).getString("lang").split("_");
			Locale.setDefault(new Locale(data[0], data[1]));
		} else {
			Gdx.app.getPreferences(TITLE).putString("lang", Locale.getDefault().toString());
			Gdx.app.getPreferences(TITLE).putBoolean("debug", false);
			Gdx.app.getPreferences(TITLE).putBoolean("fullscreen", false);
			Gdx.app.getPreferences(TITLE).putBoolean("vsync", true);
			Gdx.app.getPreferences(TITLE).flush();
		}

		createLangBundle(Locale.getDefault().getLanguage());
		
		Texture.setAssetManager(assets);
		assets.load("img/splash.jpg", Texture.class);
		assets.finishLoading();		
		
		debug(this.getClass().toString(), "switching to Splash Screen");
		setScreen(new Splash());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
		
	}
	
	public static boolean inetConnection() {
		try {
			final URLConnection connection = new URL("http://api.shephertz.com/").openConnection();
			connection.connect();
			log(TITLE, "Internet connection available.");
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
}
