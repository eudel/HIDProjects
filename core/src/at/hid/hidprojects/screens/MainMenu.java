package at.hid.hidprojects.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import at.hid.hidprojects.HIDProjects;

/**
 * @author dunkler_engel
 *
 */
public class MainMenu implements Screen {

	private Stage stage;
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
	}

	@Override
	public void show() {
		HIDProjects.debug(this.getClass().toString(), "creating MainMenu screen");
		switch (Gdx.app.getPreferences(HIDProjects.TITLE).getString("defaultModule", "Project")) {
			case "Calendar":
				((Game) Gdx.app.getApplicationListener()).setScreen(new Calendar());
				break;
			case "Project":
				((Game) Gdx.app.getApplicationListener()).setScreen(new Project());
				break;
			case "Statistic":
				((Game) Gdx.app.getApplicationListener()).setScreen(new Statistic());
				break;
			case "Todo":
				((Game) Gdx.app.getApplicationListener()).setScreen(new Todo());
				break;
			case "Notes":
				((Game) Gdx.app.getApplicationListener()).setScreen(new Notes());
				break;
			case "Timecard":
				((Game) Gdx.app.getApplicationListener()).setScreen(new Timecard());
				break;
		}
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
	}

}
