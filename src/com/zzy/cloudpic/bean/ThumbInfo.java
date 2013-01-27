package com.zzy.cloudpic.bean;

import java.io.Serializable;

public class ThumbInfo implements Serializable {

	private int id; // id
	private String data; // 路径
	private int height; // 高
	private int width; // 宽
	private int kind; // 缩略图类型
	private int image_id; // 对应图片id

	public ThumbInfo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getImage_id() {
		return image_id;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	@Override
	public String toString() {
		return "ThumbInfo [id=" + id + ", data=" + data + ", height=" + height + ", width=" + width
				+ ", kind=" + kind + ", image_id=" + image_id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + image_id;
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
		ThumbInfo other = (ThumbInfo) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		}
		else if (!data.equals(other.data))
			return false;
		if (image_id != other.image_id)
			return false;
		return true;
	}

}
