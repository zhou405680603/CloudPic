package com.zzy.cloudpic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoverInfo implements Serializable {
	private List<ImageInfo> imageList = null; // 相册中的图片
	private List<ThumbInfo> thumbList = null; // 缩略图
	private String name; // 相册的名字
	private String data; // 相册路径
	private int size; // 相册中的图片数量
	private int coverSize;// 封面张数
	private boolean isHide; // 是否排除
	private int scrollY = 0;

	public CoverInfo(String name) {
		this.name = name;
		imageList = new ArrayList<ImageInfo>();
		size = 1;
		coverSize = 1;
		isHide = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCoverSize() {
		return coverSize;
	}

	public void setCoverSize(int coverSize) {
		this.coverSize = coverSize;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}

	public List<ImageInfo> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageInfo> imageList) {
		this.imageList = imageList;
		size = imageList.size();
	}

	public int getSize() {
		return size;
	}

	public void addImage(ImageInfo image) {
		imageList.add(image);
		size = imageList.size();
	}

	public ImageInfo getImage(int id) {
		return imageList.get(id);
	}

	public ImageInfo removeImage(int id) {
		size = imageList.size() - 1;
		return imageList.remove(id);
	}

	public List<ThumbInfo> getThumbList() {
		return thumbList;
	}

	public void setThumbList(List<ThumbInfo> thumbList) {
		this.thumbList = thumbList;
	}

	public void addThumb(ThumbInfo thumb) {
		thumbList.add(thumb);
	}

	public ThumbInfo getThumb(int id) {
		return thumbList.get(id);
	}

	public void setScrollY(int scrollY) {
		System.out.println("coverInfo=======" + scrollY);
		this.scrollY = scrollY;
	}

	public int getScrollY() {
		return scrollY;
	}

	@Override
	public String toString() {
		return "CoverInfo [thumbList=" + imageList + ", name=" + name + ", size=" + size
				+ ", coverSize=" + coverSize + ", isHide=" + isHide + "]";
	}

}
