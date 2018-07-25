package com.kirtesh.downloadmanager.enums;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author kirteshdudawat
 *
 * Contains all eligible file downloading protocols as Enum Constants.
 * Support to below mentioned protocol would be present in Application.
 * 
 *
 */
public enum Protocols {
	
	HTTP("HTTP"),
	HTTPS("HTTPS"),
	FTP("FTP"),
	SFTP("SFTP");
	
	private static List<String> protocolList = new ArrayList<>();
	private String _protocol;
	
	private Protocols(String _protocol) {
		this._protocol = _protocol;
	}

	public String getProtocol() {
		return _protocol;
	}
	
	static {
		for(Protocols protocols : Protocols.values()) {
			protocolList.add(protocols._protocol);
		}
	}
	
	/**
	 * 
	 * @return Deep Copy of All protocols in list format.
	 */
	public static List<String> getPropertiesList() {
		List<String> protocolsList = new ArrayList<String>();
		protocolsList.addAll(protocolList);
		return protocolsList;
	}

	/**
	 * Validates if protocol mentioned is Valid or not.
	 * 
	 * @param protocol - protocol against whom validity needs to be tested.
	 * @return boolean value in true / false representating that is Protocol valid or not.
	 */
	public static boolean isValidProtocol(String protocol) {
		protocol = protocol.toUpperCase();
		
		if(protocolList.contains(protocol)) {
			return true;
		}
		return false;
	}
}
