package atm.atm.impl;

import atm.Nominal;
import atm.atm.ATM;
import atm.cell.Cell;
import atm.cell.impl.CellImpl;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class ATMImpl implements ATM {
    private List<Cell> atmStorage;

    private BufferedReader bufferedReader;

    private ATMImpl() {

        this.atmStorage = new ArrayList<>();
        for (Nominal nominal : Nominal.values()) {
            this.atmStorage.add(new CellImpl(nominal, 5));
        }
    }

    @Override
    public boolean putCash(Nominal cash) {
        Cell curCell = null;
        for (Cell cellNominalEntry : atmStorage) {
            if (cellNominalEntry.getNominal() == cash && cellNominalEntry.getCount() < CellImpl.MAX_COUNT) {
                cellNominalEntry.put(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Nominal> getCash(Integer sum) {
        if (sum % 100 != 0) {
            throw new IllegalArgumentException("Введена некорректная сумма. Минимальная купюра - 100р.");
        }
        if (sum > this.getBalance()) {
            throw new IllegalArgumentException("Запрашиваемая сумма превышает остаток денег в банкомате.");
        }
        List<Nominal> outList = new ArrayList<>();
        List<Nominal> nominalList = new ArrayList<>(this.atmStorage.stream().map(Cell::getNominal).collect(Collectors.toList()));
        nominalList.sort(Comparator.reverseOrder());

        Map<Nominal, Integer> checkMap = new HashMap<>();
        for (Nominal nominal : nominalList) {
            Integer mustGive = sum / nominal.getNominal();
            sum = sum % nominal.getNominal();
            Integer canGive = 0;
            for (Cell cell : this.atmStorage)
                if (cell.getNominal() == nominal)
                    canGive += cell.getCount();
            if (canGive < mustGive) {
                sum += (mustGive - canGive) * nominal.getNominal();
                checkMap.put(nominal, canGive);
            } else {
                if (!checkMap.containsKey(nominal))
                    checkMap.put(nominal, mustGive);
            }
        }
        if (sum != 0) {
            int iHave = 0;
            for (Nominal key : checkMap.keySet()) {
                iHave += key.getNominal() * checkMap.get(key);
            }
            throw new IllegalArgumentException("Невозможно выдать запрашиваемую сумму имеющимися купюрами, максимально возможная сумма: " + iHave);
        } else {
            for (Nominal key : checkMap.keySet())
                addBanknotes(checkMap.get(key), key, outList);
        }
        return outList;
    }

    private void addBanknotes(Integer number, Nominal nominal, List<Nominal> outList) {
        for (int i = 0; i < number; i++) {
            Cell minCountCell = null;
            int minCount = Cell.MAX_COUNT;
            outList.add(nominal);
            for (Cell cell : this.atmStorage)
                if (cell.getNominal() == nominal && cell.getCount() > 0 && cell.getCount() <= minCount) {
                    minCount = cell.getCount();
                    minCountCell = cell;
                }
            minCountCell.get(1);
        }
    }

    @Override
    public Integer getBalance() {

        Integer balance = 0;
        for (Cell cell : this.atmStorage) {
            balance += cell.getCount() * cell.getNominal().getNominal();
        }
        return balance;
    }

    public static class ATMImplBuilder {
        public static ATMImpl build() {
            ATMImpl atm = new ATMImpl();
            return atm;
        }
    }
}