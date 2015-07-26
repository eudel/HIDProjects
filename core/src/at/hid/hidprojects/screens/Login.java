package at.hid.hidprojects.screens;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import at.hid.hidprojects.HIDProjects;

/**
 * @author dunkler_engel
 *
 */
public class Login implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private Label lblHeading, lblMail, lblPass, lblInfo;
	private TextButton btnLogin, btnRegister;
	private TextField txtMail, txtPass;

	private String mail, pass;

	public Login(String mail, String pass) {
		this.mail = mail;
		this.pass = pass;
	}

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
		HIDProjects.debug(this.getClass().toString(), "creating Login screen");
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		Gdx.input.setInputProcessor(stage);

		// creating skin
		HIDProjects.debug(this.getClass().toString(), "creating skin");
		atlas = new TextureAtlas("ui/atlas.atlas");
		skin = new Skin(Gdx.files.internal("ui/atlas.json"), atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// creating heading
		HIDProjects.debug(this.getClass().toString(), "creating heading");
		lblHeading = new Label(HIDProjects.getLangBundle().format("Login.lblHeading.text"), skin);

		// creating labels
		HIDProjects.debug(this.getClass().toString(), "creating labels");
		lblMail = new Label(HIDProjects.getLangBundle().format("Login.lblMail.text"), skin);
		lblPass = new Label(HIDProjects.getLangBundle().format("Login.lblPass.text"), skin);
		lblInfo = new Label("", skin);

		// creating textfields
		txtMail = new TextField(mail, skin);
		txtPass = new TextField(pass, skin);
		txtPass.setPasswordMode(true);
		txtPass.setPasswordCharacter('*');

		// creating buttons
		HIDProjects.debug(this.getClass().toString(), "creating buttons");
		btnLogin = new TextButton(HIDProjects.getLangBundle().format("Login.btnLogin.text"), skin);
		btnLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (doLogin(txtMail.getText(), txtPass.getText())) {
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				} else {
					lblInfo.setText(HIDProjects.getLangBundle().format("Login.lblInfo.noinet"));
					lblInfo.setColor(Color.RED);
				}
			}
		});
		btnLogin.pad(10);

		btnRegister = new TextButton(HIDProjects.getLangBundle().format("Login.btnRegister.text"), skin);
		btnRegister.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				HIDProjects.debug(this.getClass().toString(), "switching to Register screen");
				//				Gdx.net.openURI("http://hidlauncher.hid-online.at/register.php");
				((Game) Gdx.app.getApplicationListener()).setScreen(new Register(txtMail.getText(), txtPass.getText()));
			}
		});
		btnRegister.pad(10);

		// building ui
		HIDProjects.debug(this.getClass().toString(), "building ui");
		table.add(lblHeading).spaceBottom(100).row();
		table.add(lblMail).spaceBottom(15).row();
		table.add(txtMail).width(500).spaceBottom(15).row();
		table.add(lblPass).spaceBottom(15).row();
		table.add(txtPass).width(500).spaceBottom(15).row();
		table.add(lblInfo).spaceBottom(15).row();
		HorizontalGroup hg1 = new HorizontalGroup();
		hg1.addActor(btnRegister);
		hg1.addActor(btnLogin);
		table.add(hg1).spaceBottom(15).row();
		if (HIDProjects.DEBUG) {
			table.debug(); // draw debug lines
		}
		stage.addActor(table);
		stage.setKeyboardFocus(txtMail);
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
		HIDProjects.debug(this.getClass().toString(), "cleaning up Login screen");
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

	public boolean doLogin(String user, String pass) {
		HashMap<String, String> otherMetaHeaders = new HashMap<String, String>();
		otherMetaHeaders.put("emailAuth", "true");
		otherMetaHeaders.put("userProfile", "true");
		HIDProjects.app42.userServiceSetOtherMetaHeaders(otherMetaHeaders);

		if (HIDProjects.inetConnection()) {
			if (HIDProjects.app42.userServiceAuthenticate(txtMail.getText(), txtPass.getText()) == false) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Register(txtMail.getText(), txtPass.getText()));
				return false;
			} else {
				String uuid = HIDProjects.app42.userGetUserName();
				if (!uuid.isEmpty()) {
					HIDProjects.app42.getUser(uuid);
					HIDProjects.profile.setSelectedUser(uuid);

					Json json = new Json();
					FileHandle fhProfile = null;
					try {
						if (HIDProjects.EXT) {
							fhProfile = Gdx.files.external(HIDProjects.PATH + "/profile.dat");
						} else {
							fhProfile = Gdx.files.local(HIDProjects.PATH + "/profile.dat");
						}
						String profileAsText = json.prettyPrint(HIDProjects.profile);
						fhProfile.writeString(profileAsText, false, "UTF-8");
					} catch (Exception e) {
						HIDProjects.error(this.getClass().getName(), "error writing profile.dat file", e);
					}

				}
			}
		} else {
			HIDProjects.error(this.getClass().toString(), "no internet connection available");
			return false;
		}
		return true;
	}
}
