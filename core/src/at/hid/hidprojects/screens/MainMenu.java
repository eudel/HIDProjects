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
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

//		Gdx.input.setInputProcessor(stage);

		String defaultModule = Gdx.app.getPreferences(HIDProjects.TITLE).getString("defaultModule", HIDProjects.getLangBundle().format("Options.selDefaultModule.text1")); 
		if (defaultModule.equals(HIDProjects.getLangBundle().format("Options.selDefaultModule.text2")))
			((Game) Gdx.app.getApplicationListener()).setScreen(new Calendar());
		else if (defaultModule.equals(HIDProjects.getLangBundle().format("Options.selDefaultModule.text1")))
			((Game) Gdx.app.getApplicationListener()).setScreen(new Project());
		else if (defaultModule.equals(HIDProjects.getLangBundle().format("Options.selDefaultModule.text3")))
			((Game) Gdx.app.getApplicationListener()).setScreen(new Statistic());
		else if (defaultModule.equals(HIDProjects.getLangBundle().format("Options.selDefaultModule.text4")))
			((Game) Gdx.app.getApplicationListener()).setScreen(new Todo());
		else if (defaultModule.equals(HIDProjects.getLangBundle().format("Options.selDefaultModule.text5")))
			((Game) Gdx.app.getApplicationListener()).setScreen(new Notes());
		else if (defaultModule.equals(HIDProjects.getLangBundle().format("Options.selDefaultModule.text6")))
			((Game) Gdx.app.getApplicationListener()).setScreen(new Timecard());
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
