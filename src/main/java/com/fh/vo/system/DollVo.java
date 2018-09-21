package com.fh.vo.system;

import java.util.List;
/**
 * 娃娃机实体类
 */

public class DollVo {

    private String dollId;
    private String dollName;
    private String dollState;
    private String roomId;
    private String dollSn;
    private Integer dollGold;
    private String dollUrl;
    private String cameraName01;
    private String cameraName02;
    private String dollConversiongold;
    private List<CameraVo> cameras;
    private String dollTag;
    private String prob;//娃娃机概率
    private String dollType; //娃娃显示渠道类型
	private String reward;//奖金
	private String deviceType;//网关类型
	private String machine_type;

	public String getMachine_type() {
		return machine_type;
	}

	public void setMachine_type(String machine_type) {
		this.machine_type = machine_type;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getDollConversiongold() {
		return dollConversiongold;
	}

	public void setDollConversiongold(String dollConversiongold) {
		this.dollConversiongold = dollConversiongold;
	}

	public String getProb() {
		return prob;
	}

	public void setProb(String prob) {
		this.prob = prob;
	}

	public String getDollTag() {
		return dollTag;
	}

	public void setDollTag(String dollTag) {
		this.dollTag = dollTag;
	}

	public String getDollId() {
		return dollId;
	}

	public void setDollId(String dollId) {
		this.dollId = dollId;
	}

	public String getDollName() {
		return dollName;
	}

	public void setDollName(String dollName) {
		this.dollName = dollName;
	}

	public String getDollState() {
		return dollState;
	}

	public void setDollState(String dollState) {
		this.dollState = dollState;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getDollSn() {
		return dollSn;
	}

	public void setDollSn(String dollSn) {
		this.dollSn = dollSn;
	}

	public Integer getDollGold() {
		return dollGold;
	}

	public void setDollGold(Integer dollGold) {
		this.dollGold = dollGold;
	}

	public String getDollUrl() {
		return dollUrl;
	}

	public void setDollUrl(String dollUrl) {
		this.dollUrl = dollUrl;
	}

	public String getCameraName01() {
		return cameraName01;
	}

	public void setCameraName01(String cameraName01) {
		this.cameraName01 = cameraName01;
	}

	public String getCameraName02() {
		return cameraName02;
	}

	public void setCameraName02(String cameraName02) {
		this.cameraName02 = cameraName02;
	}

	public String getDollCnversiongold() {
		return dollConversiongold;
	}

	public void setDollCnversiongold(String dollCnversiongold) {
		this.dollConversiongold = dollCnversiongold;
	}

	public List<CameraVo> getCameras() {
		return cameras;
	}

	public void setCameras(List<CameraVo> cameras) {
		this.cameras = cameras;
	}

	public String getDollType() {
		return dollType;
	}

	public void setDollType(String dollType) {
		this.dollType = dollType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
