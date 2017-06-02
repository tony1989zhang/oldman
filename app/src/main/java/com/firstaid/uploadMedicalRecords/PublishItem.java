package com.firstaid.uploadMedicalRecords;

import java.io.Serializable;

public class PublishItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3048543975955010025L;

	private boolean isVoice = false;

	private boolean isAddPhoto;

	private String picDrr;

	private String voicePath;

	private float voiceLength;

	private String voiceName;

	public String getPicDrr() {
		return picDrr;
	}

	public void setPicDrr(String picDrr) {
		this.picDrr = picDrr;
	}

	public boolean isVoice() {
		return isVoice;
	}

	public void setVoice(boolean isVoice) {
		this.isVoice = isVoice;
	}

	public boolean isAddPhoto() {
		return isAddPhoto;
	}

	public void setAddPhoto(boolean isAddPhoto) {
		this.isAddPhoto = isAddPhoto;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public float getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(float voiceLength) {
		this.voiceLength = voiceLength;
	}

	public String getVoiceName() {
		return voiceName;
	}

	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}

}
