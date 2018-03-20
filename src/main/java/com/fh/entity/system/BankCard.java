package com.fh.entity.system;

import lombok.Data;

/**
 * 银行卡信息类
 */
@Data
public class BankCard {
    private String BANKCARD_ID;
    private String USER_ID;
    private String BANK_ADDRESS;
    private String BANK_NAME;
    private String BANK_BRANCH;
    private String BANK_CARD_NO;
    private String ID_NUMBER;
    private String USER_REA_NAME;
    private String IS_DEFAULT;
    private String BANK_PHONE;
}
