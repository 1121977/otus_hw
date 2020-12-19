package atm.atm.impl;

import atm.atm.ATM;
import atm.atm.ATMService;
import atm.cell.Cell;

public class ATMServiceImpl implements ATMService {

    private ATM atm;

    public ATMServiceImpl(ATM atm){
        this.atm = atm;
    }
    @Override
    public Integer getBalance() {
        Integer balance = 0;
        for (Cell cell : atm.getAtmStorage()) {
            balance += cell.getCount() * cell.getNominal().getNominal();
        }
        return balance;
    }
}
