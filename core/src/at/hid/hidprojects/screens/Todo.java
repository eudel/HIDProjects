package at.hid.hidprojects.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import at.hid.hidprojects.HIDProjects;

/**
 * @author dunkler_engel
 *
 */
public class Todo implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table, table0;
	private TextButton btnTabProject, btnTabStatistic, btnTabTodo, btnTabNotes, btnHome, btnTabCalendar, btnLogout, btnOptions, btnTabTimecard;
	private ScrollPane spLog;

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
		HIDProjects.debug(this.getClass().toString(), "creating MainMenu screen");
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		Gdx.input.setInputProcessor(stage);

		// creating skin
		HIDProjects.debug(this.getClass().toString(), "creating skin");
		atlas = new TextureAtlas("ui/atlas.atlas");
		skin = new Skin(Gdx.files.internal("ui/atlas.json"), atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table0 = new Table(skin);

		// creating tabs
		HIDProjects.debug(this.getClass().toString(), "creating tabs");
		btnTabCalendar = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnCalendar.text"), skin, "tab");
		btnTabCalendar.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		btnTabProject = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabProject.text"), skin, "tab");
		btnTabProject.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});

		btnTabStatistic = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabStatistic.text"), skin, "tab");
		btnTabStatistic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});

		btnTabTodo = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabTodo.text"), skin, "tab");
		btnTabTodo.setDisabled(true);

		btnTabNotes = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabNotes.text"), skin, "tab");
		btnTabNotes.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		btnTabTimecard = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTimecard.text"), skin, "tab");
		btnTabTimecard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		// creating buttons
		HIDProjects.debug(this.getClass().toString(), "creating buttons");
		btnHome = new TextButton(HIDProjects.TITLE, skin);
		btnHome.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				HIDProjects.debug(this.getClass().toString(), "switching to MainMenu screen");
				dispose();
				((Game) Gdx.app.getApplicationListener()).setScreen(new Todo());
			}
		});
		btnHome.pad(10);

		btnOptions = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnOptions.text"), skin);
		btnOptions.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				HIDProjects.debug(this.getClass().toString(), "switching to Options screen");
				dispose();
				((Game) Gdx.app.getApplicationListener()).setScreen(new Options());
			}
		});
		btnOptions.pad(10);

		btnLogout = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnLogout.text"), skin);
		btnLogout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				HIDProjects.debug(this.getClass().toString(), "switching to Login screen");
				HIDProjects.app42.userServiceLogout(HIDProjects.app42.userGetSessionId());
				HIDProjects.profile.setSelectedUser(null);
				HIDProjects.profile.setClientToken(null);
				HIDProjects.profile.saveProfile();
				((Game) Gdx.app.getApplicationListener()).setScreen(new Login("", ""));
				dispose();
			}
		});
		btnLogout.pad(10);

		// building ui
		HIDProjects.debug(this.getClass().toString(), "building ui");
		table.add(table0).width(table.getWidth() - (15 * HIDProjects.scale));
		table0.add(btnHome);
		table0.add(btnTabCalendar);
		table0.add(btnTabProject);
		table0.add(btnTabStatistic);
		table0.add(btnTabTodo);
		table0.add(btnTabNotes);
		table0.add(btnTabTimecard);
		table0.add(btnOptions);
		table0.add(btnLogout);
		table.row();
		
		table.add(spLog).expandY().top().row();

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
		HIDProjects.debug(this.getClass().toString(), "cleaning up MainMenu screen");
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

}
