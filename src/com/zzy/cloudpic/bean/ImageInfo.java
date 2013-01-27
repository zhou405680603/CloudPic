package com.zzy.cloudpic.bean;

import java.io.Serializable;

public class ImageInfo implements Serializable {
	private int id; // id _id
	private int orientation;// 旋转的角度
	private long date_add; // 插入时间 date_add
	private long date_modified; // 最后修改时间 date_modified
	private long size; // 大小 _size
	private long date_taken; // 图片生成时间，为空则等于datae_modified date_taken
	private double latitude; // 纬度 latitude
	private double longitude; // 经度 longitude
	private String data; // 路径 _data
	private String mime_type; // 类型 mime_type
	private String title; // 标题 title
	private String parent_name; // 所在文件夹 bucket_dispaly_name
	private String display_name; // 名称 _display_name

	public int getId() {

		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public long getDate_add() {
		return date_add;
	}

	public void setDate_add(long date_add) {
		this.date_add = date_add;
	}

	public long getDate_modified() {
		return date_modified;
	}

	public void setDate_modified(long date_modified) {
		this.date_modified = date_modified;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getDate_taken() {
		return date_taken;
	}

	public void setDate_taken(long date_taken) {
		this.date_taken = date_taken;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageInfo other = (ImageInfo) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		}
		else if (!data.equals(other.data))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageInfo [id=" + id + ", date_add=" + date_add + ", date_modified="
				+ date_modified + ", size=" + size + ", data=" + data + ", mime_type=" + mime_type
				+ ", title=" + title + ", parent_name=" + parent_name + ", _display_name="
				+ display_name + "]";
	}

}
