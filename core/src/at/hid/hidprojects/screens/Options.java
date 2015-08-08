package at.hid.hidprojects.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import at.hid.hidprojects.HIDProjects;

/**
 * @author dunkler_engel
 *
 */
public class Options implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private Label lblHeading, lblDefaultModule;
	private SelectBox<String> selDefaultModule;
	private TextButton btnBack;
	private CheckBox cbVsync, cbDebug, cbFullscreen;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
		table.setSize(width, height);
	}

	@Override
	public void show() {
		HIDProjects.debug(this.getClass().toString(), "creating EditProfile screen");
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		Gdx.input.setInputProcessor(stage);

		// creating skin
		HIDProjects.debug(this.getClass().toString(), "creating skin");
		atlas = new TextureAtlas("ui/gui.atlas");
		skin = new Skin(Gdx.files.internal("ui/gui.json"), atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// creating heading
		HIDProjects.debug(this.getClass().toString(), "creating heading");
		lblHeading = new Label(HIDProjects.getLangBundle().format("Options.lblHeading.text"), skin);

		// creating labels
		HIDProjects.debug(this.getClass().toString(), "creating labels");
		lblDefaultModule = new Label(HIDProjects.getLangBundle().format("Options.lblDefaultModule.text"), skin);
		
		// creating selectboxes
		HIDProjects.debug(this.getClass().toString(), "creating selectboxes");
		selDefaultModule = new SelectBox<String>(skin);
		ArrayList<String> newItems = new ArrayList<>();
		newItems.add(HIDProjects.getLangBundle().format("Options.selDefaultModule.text1"));
		newItems.add(HIDProjects.getLangBundle().format("Options.selDefaultModule.text2"));
		newItems.add(HIDProjects.getLangBundle().format("Options.selDefaultModule.text3"));
		newItems.add(HIDProjects.getLangBundle().format("Options.selDefaultModule.text4"));
		newItems.add(HIDProjects.getLangBundle().format("Options.selDefaultModule.text5"));
		newItems.add(HIDProjects.getLangBundle().format("Options.selDefaultModule.text6"));
		String[] data = new String[newItems.size()];
		selDefaultModule.setItems(newItems.toArray(data));
		selDefaultModule.setSelected(Gdx.app.getPreferences(HIDProjects.TITLE).getString("defaultModule", HIDProjects.getLangBundle().format("Options.selDefaultModule.text1")));
		selDefaultModule.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.getPreferences(HIDProjects.TITLE).putString("defaultModule", selDefaultModule.getSelected());
				Gdx.app.getPreferences(HIDProjects.TITLE).flush();
			}
		});

		// creating buttons
		HIDProjects.debug(this.getClass().toString(), "creating buttons");
		btnBack = new TextButton(HIDProjects.getLangBundle().format("Options.btnBack.text"), skin);
		btnBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				HIDProjects.debug(this.getClass().toString(), "switching to MainMenu screen");
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});
		btnBack.pad(10);

		// creating checkboxes
		HIDProjects.debug(this.getClass().toString(), "creating checkboxes");
		cbDebug = new CheckBox(HIDProjects.getLangBundle().format("Options.cbDebug.text"), skin);
		cbDebug.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (cbDebug.isChecked()) {
					Gdx.app.getPreferences(HIDProjects.TITLE).putBoolean("debug", true);
					HIDProjects.DEBUG = true;
					Gdx.app.setLogLevel(Application.LOG_DEBUG);
				} else {
					Gdx.app.getPreferences(HIDProjects.TITLE).putBoolean("debug", false);
					HIDProjects.DEBUG = false;
					Gdx.app.setLogLevel(Application.LOG_INFO);
				}
				Gdx.app.getPreferences(HIDProjects.TITLE).flush();
			}
		});
		
		cbVsync = new CheckBox(HIDProjects.getLangBundle().format("Options.cbVsync.text"), skin);
		cbVsync.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (cbVsync.isChecked()) {
					Gdx.app.getPreferences(HIDProjects.TITLE).putBoolean("vsync", true);
				} else {
					Gdx.app.getPreferences(HIDProjects.TITLE).putBoolean("vsync", false);
				}
				Gdx.app.getPreferences(HIDProjects.TITLE).flush();
			}
		});
		
		cbFullscreen = new CheckBox(HIDProjects.getLangBundle().format("Options.cbFullscreen.text"), skin);
		cbFullscreen.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (cbFullscreen.isChecked()) {
					Gdx.app.getPreferences(HIDProjects.TITLE).putBoolean("fullscreen", true);
				} else {
					Gdx.app.getPreferences(HIDProjects.TITLE).putBoolean("fullscreen", false);
				}
				Gdx.app.getPreferences(HIDProjects.TITLE).flush();
			}
		});
		
		if(Gdx.app.getPreferences(HIDProjects.TITLE).getBoolean("debug")) {
			cbDebug.setChecked(true);
		}
		if(Gdx.app.getPreferences(HIDProjects.TITLE).getBoolean("vsync")) {
			cbVsync.setChecked(true);
		}
		if(Gdx.app.getPreferences(HIDProjects.TITLE).getBoolean("fullscreen")) {
			cbFullscreen.setChecked(true);
		}
		
		// building ui
		HIDProjects.debug(this.getClass().toString(), "building ui");
		table.add(lblHeading).spaceBottom(15).row();
		table.add(cbDebug).left().spaceBottom(15).row();
		table.add(cbVsync).left().spaceBottom(15).row();
		table.add(cbFullscreen).left().spaceBottom(15).row();
		table.add(lblDefaultModule).row();
		table.add(selDefaultModule).left().spaceBottom(15).row();
		table.add(btnBack).spaceBottom(15);
		if (HIDProjects.DEBUG) {
			table.debug(); // draw debug lines
		}
		stage.addActor(table);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		HIDProjects.debug(this.getClass().toString(), "cleaning up Options screen");
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

}
