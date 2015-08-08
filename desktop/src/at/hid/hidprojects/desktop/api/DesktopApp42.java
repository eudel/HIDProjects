package at.hid.hidprojects.desktop.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import com.shephertz.app42.paas.sdk.java.App42API;
import com.shephertz.app42.paas.sdk.java.App42Exception;
import com.shephertz.app42.paas.sdk.java.storage.Query;
import com.shephertz.app42.paas.sdk.java.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.java.storage.Storage;
import com.shephertz.app42.paas.sdk.java.storage.StorageService;
import com.shephertz.app42.paas.sdk.java.storage.QueryBuilder.Operator;
import com.shephertz.app42.paas.sdk.java.user.User;
import com.shephertz.app42.paas.sdk.java.user.UserService;

import at.hid.hidprojects.HIDProjects;
import at.hid.hidprojects.api.App42;


public class DesktopApp42 implements App42 {
	private UserService userService;
	private StorageService storageService;
	private ArrayList<User> userlist = null;
	private User user;
	private Storage storage = null;
	private ArrayList<Storage.JSONDocument> jsonDocList = null;

	public DesktopApp42() {
	}

	@Override
	public void initialize(String apiKey, String secretKey) {
		App42API.initialize(apiKey, secretKey);
	}

	@Override
	public void buildUserService() {
		userService = App42API.buildUserService();
	}
	
	@Override
	public void buildStorageService() {
		 storageService = App42API.buildStorageService();
	}
	
	@Override
	public void setDbName(String dbName) {
		App42API.setDbName(dbName);
	}
	
	@Override
	public void getUser(String username) {
		user = userService.getUser(username);
	}
	
	@Override
	public void getUserByMail(String mail) {
		user = userService.getUserByEmailId(mail);
	}
	
	@Override
	public void createUser(String uName, String pwd, String emailAddress) {
		user = userService.createUser(uName, pwd, emailAddress);
	}
	
	@Override
	public void setSessionId(String sid) {
		user.setSessionId(sid);
		userService.setSessionId(sid);
		storageService.setSessionId(sid);
	}
	
	@Override
	public void userServiceSetQuery(String collectionName, String key, String value) {
		Query query = QueryBuilder.build(key, value, Operator.EQUALS);
		userService.setQuery(collectionName, query);
	}
	
	@Override
	public void userServiceSetOtherMetaHeaders(HashMap<String, String> otherMetaHeaders) {
		userService.setOtherMetaHeaders(otherMetaHeaders);
	}
	
	@Override
	public boolean userServiceAuthenticate(String uName, String pwd) {
		try {
			HashMap<String, String> otherMetaHeaders = new HashMap<String, String>();
			otherMetaHeaders.put("emailAuth", "true");
			otherMetaHeaders.put("userProfile", "true");
			userService.setOtherMetaHeaders(otherMetaHeaders);
			
			user = userService.authenticate(uName, pwd);
			HIDProjects.profile.setClientToken(user.getSessionId());
			HIDProjects.profile.setDisplayName(user.getProfile().getFirstName());
		} catch (App42Exception e) {
			return false;
		}
		if (user != null && user.getEmail() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void userServiceLogout(String sessionId) {
		userService.logout(sessionId);
	}
	
	@Override
	public void userServiceGetAllUsers() {
		try {
			userlist = userService.getAllUsers();
		} catch (App42Exception e) {
			int errorCode = e.getAppErrorCode();
			switch (errorCode) {
				case 2006:

					break;
			}
		}
	}
	
	@Override
	public int userServiceGetUserCount() {
		if (userlist != null) {
			return userlist.size();
		} else {
			return 0;
		}
	}
	
	@Override
	public void userServiceCreateOrUpdateProfile() {
		userService.createOrUpdateProfile(user);
	}
	
	@Override
	public void userServiceAddJSONObject(String collectionName, JSONObject jsonDoc) {
		userService.addJSONObject(collectionName, jsonDoc);
	}
	
	@Override
	public String userGetSessionId() {
		return user.getSessionId();
	}
	
	@Override
	public String userGetUserName() {
		return user.getUserName();
	}
	
	@Override
	public String userGetEmail() {
		return user.getEmail();
	}
	
	@Override
	public String userGetFirstName() {
		return user.getProfile().getFirstName();
	}
	
	@Override
	public void userSetFirstName(String displayName) {
		user.getProfile().setFirstName(displayName);
	}
	
	@Override
	public ArrayList<JSONObject> userGetJsonDocList() {
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		for (int i = 0; i < user.getJsonDocList().size(); i++) {
			try {
				result.add(new JSONObject(user.getJsonDocList().get(i).getJsonDoc()));
			} catch (Exception e) {
				HIDProjects.error(this.getClass().toString(), "error creating JsonDoc", e);
			}
		}
		
		return result;
	}
	
	@Override
	public String userlistGetUserName(int i) {
		return userlist.get(i).getUserName();
	}
	
	@Override
	public String userlistGetUserMail(int i) {
		return userlist.get(i).getEmail();
	}
	
	@Override
	public String userlistGetUserDisplayName(int i) {
		return userlist.get(i).getProfile().getFirstName();
	}
	
	@Override
	public void storageServiceFindDocumentByKeyValue(String dbName, String collectionName, String key, String value) {
		storage = storageService.findDocumentByKeyValue(dbName, collectionName, key, value);
	}
	
	@Override
	public void storageServiceDeleteDocumentsByKeyValue(String dbName, String collectionName, String key, String value) {
		storageService.deleteDocumentsByKeyValue(dbName, collectionName, key, value);
	}
	
	@Override
	public void storageServiceFindAllDocuments(String dbName, String collectionName) {
		storage = storageService.findAllDocuments(dbName, collectionName);
	}
	
	@Override
	public void storageGetJsonDocList() {
		if (storage != null) {
			jsonDocList = storage.getJsonDocList();
		}
	}
	
	@Override
	public ArrayList<JSONObject> storageServiceGetJsonDocList() {
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		for (int i = 0; i < storage.getJsonDocList().size(); i++) {
			try {
				result.add(new JSONObject(storage.getJsonDocList().get(i).getJsonDoc()));
			} catch (Exception e) {
				HIDProjects.error(this.getClass().toString(), "error creating JsonDoc", e);
			}
		}
		
		return result;
	}
	
	@Override
	public ArrayList<String> storageGetSaveValues(String key) {
		ArrayList<String> saveValues = new ArrayList<String>();
		for (int i = 0; i < jsonDocList.size(); i++) {
			JSONObject json;
			try {
				json = new JSONObject(jsonDocList.get(i).getJsonDoc());
				saveValues.add(json.getString(key));
			} catch (JSONException e) {
				HIDProjects.error(this.getClass().toString(), "error reading JSONObject", e);
			}
		}
		return saveValues;
	}
	
	@Override
	public void storageServiceInsertJSONDocument(String dbName, String collectionName, JSONObject json) {
		storageService.insertJSONDocument(dbName, collectionName, json);
	}
	
	@Override
	public void storageServiceUpdateDocumentByKeyValue(String dbName, String collectionName, String key, String value, JSONObject newJsonDoc) {
		storageService.updateDocumentByKeyValue(dbName, collectionName, key, value, newJsonDoc);
	}
	
	@Override
	public String storageGetJsonDoc(int i) {
		return storage.getJsonDocList().get(i).getJsonDoc();
	}
}
