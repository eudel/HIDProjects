package at.hid.hidprojects;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Profile {
	private String clientToken, selectedProfile, selectedUser, displayName;

	/**
	 * @return the clientToken
	 */
	public String getClientToken() {
		return clientToken;
	}
	/**
	 * @return the selectedProfile
	 */
	public String getSelectedProfile() {
		return selectedProfile;
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
	
	/**
	 * @param clientToken the clientToken to set
	 */
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
	/**
	 * @param selectedProfile the selectedProfile to set
	 */
	public void setSelectedProfile(String selectedProfile) {
		this.selectedProfile = selectedProfile;
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

	/**
	 * saves the profile file to disk
	 * @param profile the profile to save
	 */
	public boolean saveProfile() {
		FileHandle fhSettings = null;
		if (Gdx.files.isExternalStorageAvailable()) {
			fhSettings = Gdx.files.external(HIDProjects.PATH + "/profile.dat");
		} else {
			fhSettings = Gdx.files.local(HIDProjects.PATH + "/profile.dat");
		}
		JSONObject json = new JSONObject();
		try {
			json.put("clientToken", getClientToken());
			json.put("selectedProfile", getSelectedProfile());
			json.put("selectedUser", getSelectedUser());
			String profileAsText = json.toString();
			
			profileAsText = profileAsText.replaceAll("\\{", "{\n");
			profileAsText = profileAsText.replaceAll(",", ",\n");
			profileAsText = profileAsText.replaceAll("\\[", "\n[\n");
			profileAsText = profileAsText.replaceAll("\\}", "}\n");
			profileAsText = profileAsText.replaceAll("\"\\}", "\"\n}");
			profileAsText = profileAsText.replaceAll("\\]", "]\n");
			
			fhSettings.writeString(profileAsText, false, "UTF-8");
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
		FileHandle fhSettings = null;
		if (Gdx.files.isExternalStorageAvailable()) {
			fhSettings = Gdx.files.external(HIDProjects.PATH + "/profile.dat");
		} else {
			fhSettings = Gdx.files.local(HIDProjects.PATH + "/profile.dat");
		}
		try {
			String profileAsText = fhSettings.readString();
			jsonProfile = new JSONObject(profileAsText);
			if (jsonProfile.has("clientToken")) {
				setClientToken(jsonProfile.getString("clientToken"));
			}
			if (jsonProfile.has("selectedUser")) {
				setSelectedUser(jsonProfile.getString("selectedUser"));
			}
			if (jsonProfile.has("selectedProfile")) {
				setSelectedProfile(jsonProfile.getString("selectedProfile"));
			}
		} catch (Exception e) {
			HIDProjects.error(this.getClass().toString(), "error reading settings file", e);
		}
	}
}
