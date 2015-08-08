package at.hid.hidprojects.screens;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JRootPane;

import org.json.JSONObject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import at.hid.hidprojects.HIDProjects;

/**
 * @author dunkler_engel
 *
 */
public class Project implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table, table0, table1, table2, table3;
	private TextButton btnTabProject, btnTabStatistic, btnTabTodo, btnTabNotes, btnHome, btnTabCalendar, btnLogout, btnOptions, btnTabTimecard, btnNewProject, btnSaveProject;
	private Label lblBasicDataTitle, lblTitle, lblNotes, lblParent, lblStartDate, lblEndDate, lblStatus, lblContact, lblTags, lblAccessTitle, lblUser, lblRead, lblWrite, lblAccess, lblCreate, lblCopy, lblDelete, lblDownload, lblAdmin, lblAction, lblModuleTitle, lblModule, lblActive;
	private TextField txtTitle, txtTags, txtStartDate, txtEndDate;
	private TextArea txtNotes;
	private SelectBox<String> selParent, selStatus, selContact, selUser;
	private CheckBox cbRead, cbWrite, cbAccess, cbCreate, cbCopy, cbDelete, cbDownload, cbAdmin, cbNote, cbStatistic, cbTodo;
	private Tree projectTree;
	
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
		table0.setWidth(table.getWidth() - (15 * HIDProjects.scale));
		table1.setWidth(table0.getWidth());
		table2.setWidth(table1.getWidth() / 3);
		table3.setWidth((table1.getWidth() / 3) * 2);
	}

	@Override
	public void show() {
		HIDProjects.debug(this.getClass().toString(), "creating MainMenu screen");
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		Gdx.input.setInputProcessor(stage);

		// creating skin
		HIDProjects.debug(this.getClass().toString(), "creating skin");
		atlas = new TextureAtlas("ui/gui.atlas");
		skin = new Skin(Gdx.files.internal("ui/gui.json"), atlas);

		table = new Table(skin);
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table0 = new Table(skin);
		table1 = new Table(skin);
		table2 = new Table(skin);
		table3 = new Table(skin);

		// creating labels
		HIDProjects.debug(this.getClass().toString(), "creating labels");
		lblBasicDataTitle = new Label(HIDProjects.getLangBundle().format("Project.lblBasicDataTitle.text"), skin, "title");
		lblTitle = new Label(HIDProjects.getLangBundle().format("Project.lblTitle.text"), skin);
		lblTitle.setAlignment(Align.right);
		lblNotes = new Label(HIDProjects.getLangBundle().format("Project.lblNotes.text"), skin);
		lblNotes.setAlignment(Align.right);
		lblParent = new Label(HIDProjects.getLangBundle().format("Project.lblParent.text"), skin);
		lblParent.setAlignment(Align.right);
		lblStartDate = new Label(HIDProjects.getLangBundle().format("Project.lblStartDate.text"), skin);
		lblStartDate.setAlignment(Align.right);
		lblEndDate = new Label(HIDProjects.getLangBundle().format("Project.lblEndDate.text"), skin);
		lblEndDate.setAlignment(Align.right);
		lblStatus = new Label(HIDProjects.getLangBundle().format("Project.lblStatus.text"), skin);
		lblStatus.setAlignment(Align.right);
		lblContact = new Label(HIDProjects.getLangBundle().format("Project.lblContact.text"), skin);
		lblContact.setAlignment(Align.right);
		lblTags = new Label(HIDProjects.getLangBundle().format("Project.lblTags.text"), skin);
		lblTags.setAlignment(Align.right);
		lblAccessTitle = new Label(HIDProjects.getLangBundle().format("Project.lblAccessTitle.text"), skin, "title");
		
		// creating textfields
		HIDProjects.debug(this.getClass().toString(), "creating textfields");
		txtTitle = new TextField("", skin);
		txtTags = new TextField("", skin);
		txtStartDate = new TextField("", skin);
		txtEndDate = new TextField("", skin);
		
		// creating textareas
		HIDProjects.debug(this.getClass().toString(), "creating textareas");
		txtNotes = new TextArea("", skin);
		txtNotes.setPrefRows(5);
		
		// creating selectboxes
		HIDProjects.debug(this.getClass().toString(), "creating selectboxes");
		selStatus = new SelectBox<String>(skin);
		ArrayList<String> newItems = new ArrayList<String>();
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.offered"));
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.ordered"));
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.working"));
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.ended"));
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.stopped"));
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.re-opened"));
		newItems.add(HIDProjects.getLangBundle().format("Project.selStatus.waiting"));
		String[] data = new String[newItems.size()];
		selStatus.setItems(newItems.toArray(data));
		
		selParent = new SelectBox<String>(skin);
		newItems = new ArrayList<String>();
		newItems.add(HIDProjects.profile.getCompany());
		data = new String[newItems.size()];
		selParent.setItems(newItems.toArray(data));
		
		// creating tabs
		HIDProjects.debug(this.getClass().toString(), "creating tabs");
		btnTabCalendar = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabCalendar.text"), skin, "tab");
		btnTabCalendar.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Calendar());
			}
		});
		
		btnTabProject = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabProject.text"), skin, "tab");
		btnTabProject.setDisabled(true);

		btnTabStatistic = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabStatistic.text"), skin, "tab");
		btnTabStatistic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Statistic());
			}
		});

		btnTabTodo = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabTodo.text"), skin, "tab");
		btnTabTodo.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Todo());
			}
		});

		btnTabNotes = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabNotes.text"), skin, "tab");
		btnTabNotes.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Notes());
			}
		});
		
		btnTabTimecard = new TextButton(HIDProjects.getLangBundle().format("MainMenu.btnTabTimecard.text"), skin, "tab");
		btnTabTimecard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Timecard());
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
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
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
		
		btnSaveProject = new TextButton(HIDProjects.getLangBundle().format("Project.btnSaveProject.text"), skin);
		btnSaveProject.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				JSONObject json = new JSONObject();
				try {
					json.put("title", txtTitle.getText());
					json.put("notes", txtNotes.getText());
					json.put("parent", selParent.getSelected());
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy");
					json.put("startDate", sdf.parse(txtStartDate.getText()).getTime());
					json.put("endDate", sdf.parse(txtEndDate.getText()).getTime());
					json.put("status", selStatus.getSelected());
					json.put("tags", txtTags.getText());
					json.put("company", HIDProjects.profile.getCompany());
				} catch (Exception e) {
					HIDProjects.error(this.getClass().toString(), "error creating project jsonObject", e);
				}
				HIDProjects.app42.storageServiceInsertJSONDocument(HIDProjects.TITLE, "projectsList", json);
				
				table2.clear();
				table3.clear();
				
				btnSaveProject.setVisible(false);
			}
		});
		btnSaveProject.pad(10);
		btnSaveProject.setVisible(false);
		
		btnNewProject = new TextButton(HIDProjects.getLangBundle().format("Project.btnNewProject.text"), skin);
		btnNewProject.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				table2.clear();
				table3.clear();
				
				table2.add(lblBasicDataTitle).top().row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add("").row();
				table2.add(lblAccessTitle).row();
				
				table3.add().width(table3.getWidth() / 5);
				table3.add().width((table3.getWidth() / 5) * 2);
				table3.add().width((table3.getWidth() / 5) * 2).row();
				table3.add(lblTitle).fillX().space(5);
				table3.add(txtTitle).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblNotes).fillX().space(5);
				table3.add(txtNotes).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblParent).fillX().space(5);
				table3.add(selParent).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblStartDate).fillX().space(5);
				table3.add(txtStartDate).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblEndDate).fillX().space(5);
				table3.add(txtEndDate).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblStatus).fillX().space(5);
				table3.add(selStatus).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblContact).fillX().space(5);
				table3.add(selContact).fillX().space(5);
				table3.add().space(5).row();
				table3.add(lblTags).fillX().space(5);
				table3.add(txtTags).fillX().space(5);
				table3.add().space(5).row();
				
				btnSaveProject.setVisible(true);
			}
		});
		btnNewProject.pad(10);

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

		// building projectTree
		HIDProjects.debug(this.getClass().toString(), "building projectTree");
		projectTree = new Tree(skin);
		HIDProjects.app42.storageServiceFindDocumentByKeyValue(HIDProjects.TITLE, "projectsList", "company", HIDProjects.profile.getCompany());
		ArrayList<JSONObject> jsonProjectsList = HIDProjects.app42.storageServiceGetJsonDocList();
		ArrayList<String> projectNames = new ArrayList<String>();
		ArrayList<Node> treeNodes = new ArrayList<Node>();
		for (int i = 0; i < jsonProjectsList.size(); i++) {
			JSONObject jsonProject = jsonProjectsList.get(i);
			try {
				if (jsonProject.getString("parent").equals(HIDProjects.profile.getCompany())) {
					projectNames.add(jsonProject.getString("title"));
					treeNodes.add(new Node(new Label(jsonProject.getString("title"), skin)));
				}
			} catch (Exception e) {
				HIDProjects.error(this.getClass().toString(), "error reading projects JSONObject", e);
			}
		}
		
		for (int i = 0; i < treeNodes.size(); i++) {
			projectTree.add(treeNodes.get(i));
		}
		
		table2.add(projectTree);
		
		// building ui
		HIDProjects.debug(this.getClass().toString(), "building ui");
		table.add(table0).spaceBottom(15).width(table.getWidth() - (15 * HIDProjects.scale));
		table0.setWidth(table.getWidth() - (15 * HIDProjects.scale));
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
		table.add(table1).expandY().top();
		table1.setWidth(table0.getWidth());
		table1.add(btnNewProject).spaceBottom(15).width(table1.getWidth() / 3);
		table1.add(btnSaveProject).width((table1.getWidth() / 3) * 2).row();
		table1.add(table2);
		table2.setWidth(table1.getWidth() / 3);
		table1.add(table3).fill().spaceBottom(15).row();
		table3.setWidth((table1.getWidth() / 3) * 2);
		
		if (HIDProjects.DEBUG) {
			table.debug(); // draw debug lines 
			table0.debug(); 
			table1.debug(); 
			table2.debug(); 
			table3.debug(); 
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
