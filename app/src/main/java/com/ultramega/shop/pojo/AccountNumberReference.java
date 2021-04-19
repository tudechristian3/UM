package com.ultramega.shop.pojo;

/**
 * Created by User-PC on 7/11/2017.
 */

public class AccountNumberReference {

    private String BankAccountName;
    private String BankAccountNumber;

    public AccountNumberReference(String bankAccountName, String bankAccountNumber) {
        BankAccountName = bankAccountName;
        BankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountName() {
        return BankAccountName;
    }

    public String getBankAccountNumber() {
        return BankAccountNumber;
    }

    @Override
    public String toString() {
        return BankAccountName + " ("+BankAccountNumber+")";
    }
}
