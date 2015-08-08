package at.hid.hidprojects;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class Profile {
	private String clientToken, selectedUser, displayName, company;
	private int admin;

	/**
	 * @return the clientToken
	 */
	public String getClientToken() {
		return clientToken;
	}
	/**
	 * @return the selectedUser
	 */
	public String getSelectedUser() {
		return selectedUser;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getCompany() {
		return company;
	}
	
	public int getAdmin() {
		return admin;
	}
	
	/**
	 * @param clientToken the clientToken to set
	 */
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
	/**
	 * @param selectedUser the selectedUser to set
	 */
	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public void setAdmin(int admin) {
		this.admin = admin;
	}

	/**
	 * saves the profile file to disk
	 * @param profile the profile to save
	 */
	public boolean saveProfile() {
		FileHandle fhProfile = null;
		if (Gdx.files.isExternalStorageAvailable()) {
			fhProfile = Gdx.files.external(HIDProjects.PATH + "/profile.dat");
		} else {
			fhProfile = Gdx.files.local(HIDProjects.PATH + "/profile.dat");
		}
		JSONObject json = new JSONObject();
		try {
			json.put("clientToken", getClientToken());
			json.put("selectedUser", getSelectedUser());
			json.put("displayName", getDisplayName());
			json.put("company", getCompany());
			json.put("admin", getAdmin());
			String profileAsText = json.toString();
//			String encodedProfile = Base64Coder.encodeString(profileAsText);
			String encodedProfile = profileAsText;
			fhProfile.writeString(encodedProfile, false, "UTF-8");
		} catch(Exception e) {
			HIDProjects.error(this.getClass().toString(), "error creating Profile JSONObject", e);
			return false;
		}
		return true;
	}
	
	/**
	 * loads the profile from disk
	 * @return the Profile object
	 */
	public void loadProfile() {
		JSONObject jsonProfile = null;
		FileHandle fhProfile = null;
		if (Gdx.files.isExternalStorageAvailable()) {
			fhProfile = Gdx.files.external(HIDProjects.PATH + "/profile.dat");
		} else {
			fhProfile = Gdx.files.local(HIDProjects.PATH + "/profile.dat");
		}
		try {
			String encodedProfile = fhProfile.readString();
//			String profileAsText = Base64Coder.decodeString(encodedProfile);
			String profileAsText = encodedProfile;
			jsonProfile = new JSONObject(profileAsText);
			if (jsonProfile.has("clientToken")) {
				setClientToken(jsonProfile.getString("clientToken"));
			}
			if (jsonProfile.has("selectedUser")) {
				setSelectedUser(jsonProfile.getString("selectedUser"));
			}
			if (jsonProfile.has("displayName")) {
				setDisplayName(jsonProfile.getString("displayName"));
			}
			if (jsonProfile.has("company")) {
				setCompany(jsonProfile.getString("company"));
			}
			if (jsonProfile.has("admin")) {
				setAdmin(jsonProfile.getInt("admin"));
			}
		} catch (Exception e) {
			HIDProjects.error(this.getClass().toString(), "error reading settings file", e);
		}
	}
}
