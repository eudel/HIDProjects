package at.hid.hidprojects.screens;

import java.util.ArrayList;

import org.json.JSONObject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import at.hid.hidprojects.Assets;
import at.hid.hidprojects.HIDProjects;


public class Splash implements Screen {
	private SpriteBatch batch;
	private Sprite splash;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		splash.draw(batch);
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		splash.setSize(width, height);
	}

	@Override
	public void show() {
		// apply preferences
				HIDProjects.debug(this.getClass().toString(), "apply preferences");
				Gdx.graphics.setVSync(Gdx.app.getPreferences(HIDProjects.TITLE).getBoolean("vsync", false));

				HIDProjects.debug(this.getClass().toString(), "creating Splash screen");

				Assets.load();

				batch = new SpriteBatch();

				Texture splashTexture = HIDProjects.assets.get("img/splash.jpg", Texture.class);
				splash = new Sprite(splashTexture);
				splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				HIDProjects.assets.finishLoading();
				
				FileHandle fhProfile;
				if (HIDProjects.EXT) {
					fhProfile = Gdx.files.external(HIDProjects.PATH + "/profile.dat");
				} else {
					fhProfile = Gdx.files.local(HIDProjects.PATH + "/profile.dat");
				}
				if (!fhProfile.exists()) {
					HIDProjects.profile.saveProfile();
				} else {
					HIDProjects.profile.loadProfile();
				}
				
				if ((HIDProjects.profile.getSelectedUser() != null) && (!HIDProjects.profile.getSelectedUser().equals(""))) {
					HIDProjects.app42.userServiceSetQuery("companyList", "user", HIDProjects.profile.getSelectedUser());
					HIDProjects.app42.getUser(HIDProjects.profile.getSelectedUser());
					ArrayList<JSONObject> jsonDocList = HIDProjects.app42.userGetJsonDocList();
					try {
						HIDProjects.profile.setAdmin(jsonDocList.get(0).getInt("admin"));
						HIDProjects.profile.setCompany(jsonDocList.get(0).getString("company"));
					} catch (Exception e) {
						HIDProjects.error(this.getClass().toString(), "error reading additional user data", e);
					}
					HIDProjects.app42.setSessionId(HIDProjects.profile.getClientToken());
					
					
					HIDProjects.debug(this.getClass().toString(), "switching to MainMenu Screen");
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				} else {
					HIDProjects.debug(this.getClass().toString(), "switching to Login Screen");
					((Game) Gdx.app.getApplicationListener()).setScreen(new Login("", ""));
				}
				
	}
	
	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
