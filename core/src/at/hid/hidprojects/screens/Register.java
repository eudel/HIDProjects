package at.hid.hidprojects.screens;

import java.util.UUID;

import org.json.JSONObject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import at.hid.hidprojects.HIDProjects;

/**
 * @author dunkler_engel
 *
 */
public class Register implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private Label lblHeading, lblMail, lblPass, lblDisplayName, lblCompany;
	private TextButton btnLogin, btnRegister;
	private TextField txtMail, txtPass, txtDisplayName, txtCompany;

	private String mail, pass;

	public Register(String mail, String pass) {
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
		HIDProjects.debug(this.getClass().toString(), "creating Register screen");
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
		lblHeading = new Label(HIDProjects.getLangBundle().format("Register.lblHeading.text"), skin);

		// creating labels
		HIDProjects.debug(this.getClass().toString(), "creating labels");
		lblMail = new Label(HIDProjects.getLangBundle().format("Register.lblMail.text"), skin);
		lblPass = new Label(HIDProjects.getLangBundle().format("Register.lblPass.text"), skin);
		lblDisplayName = new Label(HIDProjects.getLangBundle().format("Register.lblDisplayName.text"), skin);
		lblCompany = new Label(HIDProjects.getLangBundle().format("Register.lblCompany.text"), skin);

		// creating textfields
		txtMail = new TextField(mail, skin);
		txtPass = new TextField(pass, skin);
		txtPass.setPasswordMode(true);
		txtPass.setPasswordCharacter('*');
		txtDisplayName = new TextField("", skin);
		txtCompany = new TextField("", skin);

		// creating buttons
		HIDProjects.debug(this.getClass().toString(), "creating buttons");
		btnLogin = new TextButton(HIDProjects.getLangBundle().format("Register.btnLogin.text"), skin);
		btnLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Login(txtMail.getText(), txtPass.getText()));
			}
		});
		btnLogin.pad(10);

		btnRegister = new TextButton(HIDProjects.getLangBundle().format("Register.btnRegister.text"), skin);
		btnRegister.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				HIDProjects.debug(this.getClass().toString(), "registering user " + txtDisplayName.getText());
				if (HIDProjects.inetConnection()) {
					doRegister(txtMail.getText(), txtPass.getText(), txtDisplayName.getText(), txtCompany.getText());
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
		});
		btnRegister.pad(10);

		HIDProjects.app42.userServiceGetAllUsers();
		for (int i = 0; i < HIDProjects.app42.userServiceGetUserCount(); i++) {
			if (HIDProjects.app42.userlistGetUserMail(i).equals(txtMail.getText())) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Login(mail, pass));
			}
		}

		// building ui
		HIDProjects.debug(this.getClass().toString(), "building ui");
		table.add(lblHeading).spaceBottom(100).row();
		table.add(lblMail).spaceBottom(15).row();
		table.add(txtMail).width(500).spaceBottom(15).row();
		table.add(lblPass).spaceBottom(15).row();
		table.add(txtPass).width(500).spaceBottom(15).row();
		table.add(lblDisplayName).spaceBottom(15).row();
		table.add(txtDisplayName).width(500).spaceBottom(15).row();
		table.add(lblCompany).spaceBottom(15).row();
		table.add(txtCompany).width(500).spaceBottom(15).row();
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
		HIDProjects.debug(this.getClass().toString(), "cleaning up Register screen");
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

	public void doRegister(String mail, String pass, String displayName, String company) {
		String user = UUID.randomUUID().toString();
		JSONObject jsonDoc = new JSONObject();
		try {
			jsonDoc.put("user", user);
			jsonDoc.put("company", company);
		} catch (Exception e) {
			HIDProjects.error(this.getClass().toString(), "error creating JSONObject for user " + displayName, e);
		}

		HIDProjects.app42.setDbName(HIDProjects.TITLE);
		HIDProjects.app42.userServiceAddJSONObject("companyList", jsonDoc);
		HIDProjects.app42.createUser(user, pass, mail);
		HIDProjects.app42.userServiceAuthenticate(user, pass);
		HIDProjects.app42.setSessionId(HIDProjects.app42.userGetSessionId());
		HIDProjects.app42.userSetFirstName(displayName);
		HIDProjects.app42.userServiceCreateOrUpdateProfile();

		HIDProjects.profile.setSelectedUser(user);
		HIDProjects.profile.setClientToken(HIDProjects.app42.userGetSessionId());
		HIDProjects.profile.setDisplayName(displayName);

		HIDProjects.profile.saveProfile();
	}
}
