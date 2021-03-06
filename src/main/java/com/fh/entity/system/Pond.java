package com.fh.entity.system;

/**
 * 奖池实体类
 */
public class Pond {
    private Integer POND_ID;
    private String GUESS_ID;
    private Integer GUESS_Y;
    private Integer GUESS_N;
    private Integer GUESS_GOLD;
    private Integer GOLD_Y;
    private Integer GOLD_N;
    private String CREATE_DATE;
    private String UPDATE_DATE;
    private String POND_FLAG;
    private String DOLL_ID;
    private String GUESS_STATE;//实际抓取结果
    private Integer ALLPEOPLE;//竞猜总人数
    private String GUESSER_NAME;//获胜者昵称


    public Pond() {

    }

    public Pond(String GUESS_ID, String DOLLID, String CREATE_DATE) {
        this.GUESS_ID = GUESS_ID;
        this.CREATE_DATE = CREATE_DATE;
        this.DOLL_ID = DOLLID;
    }

    public String getGUESSER_NAME() {
        return GUESSER_NAME;
    }

    public void setGUESSER_NAME(String GUESSER_NAME) {
        this.GUESSER_NAME = GUESSER_NAME;
    }

    public Integer getALLPEOPLE() {
        return ALLPEOPLE;
    }

    public void setALLPEOPLE(Integer ALLPEOPLE) {
        this.ALLPEOPLE = ALLPEOPLE;
    }

    public Integer getGOLD_Y() {
        return GOLD_Y;
    }

    public void setGOLD_Y(Integer GOLD_Y) {
        this.GOLD_Y = GOLD_Y;
    }

    public Integer getGOLD_N() {
        return GOLD_N;
    }

    public void setGOLD_N(Integer GOLD_N) {
        this.GOLD_N = GOLD_N;
    }

    public String getDOLL_ID() {
        return DOLL_ID;
    }

    public void setDOLL_ID(String DOLL_ID) {
        this.DOLL_ID = DOLL_ID;
    }

    public String getGUESS_STATE() {
        return GUESS_STATE;
    }

    public void setGUESS_STATE(String GUESS_STATE) {
        this.GUESS_STATE = GUESS_STATE;
    }

    public String getPOND_FLAG() {
        return POND_FLAG;
    }


    public void setPOND_FLAG(String POND_FLAG) {
        this.POND_FLAG = POND_FLAG;
    }

    public Integer getPOND_ID() {
        return POND_ID;
    }

    public void setPOND_ID(Integer POND_ID) {
        this.POND_ID = POND_ID;
    }

    public String getGUESS_ID() {
        return GUESS_ID;
    }

    public void setGUESS_ID(String GUESS_ID) {
        this.GUESS_ID = GUESS_ID;
    }

    public Integer getGUESS_Y() {
        return GUESS_Y;
    }

    public void setGUESS_Y(Integer GUESS_Y) {
        this.GUESS_Y = GUESS_Y;
    }

    public Integer getGUESS_N() {
        return GUESS_N;
    }

    public void setGUESS_N(Integer GUESS_N) {
        this.GUESS_N = GUESS_N;
    }

    public Integer getGUESS_GOLD() {
        return GUESS_GOLD;
    }

    public void setGUESS_GOLD(Integer GUESS_GOLD) {
        this.GUESS_GOLD = GUESS_GOLD;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(String UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }


}
