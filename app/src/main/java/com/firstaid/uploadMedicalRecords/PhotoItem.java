package com.firstaid.uploadMedicalRecords;

import java.io.Serializable;

public class PhotoItem implements Serializable {
	private static final long serialVersionUID = 8682674788506891598L;
	public boolean select = false;
	private boolean isVideo = false;
	private String path;
	private String thumbnailPath;
	private int width; // 宽
	private int height; // 高
	private int duration; // 视频长度
	private long size; // 文件大小
	private long createTime; // 视频创建时间 时间戳

	public PhotoItem(String path) {
		select = false;
		this.path = path;
	}

	public PhotoItem(boolean flag) {
		select = flag;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean isVideo() {
		return isVideo;
	}

	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "PhotoItem [select=" + select + ", isVideo=" + isVideo + ", path=" + path
				+ ", thumbnailPath=" + thumbnailPath + ", width=" + width + ", height="
				+ height + ", duration=" + duration + ", size=" + size + ", createTime="
				+ createTime + "]";
	}
	
//	
}
