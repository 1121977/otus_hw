package atm.cell.impl;

import atm.Nominal;
import atm.exceptions.CellIsFullException;

public class CellImpl implements atm.cell.Cell {
    private final Nominal nominal;
    private Integer count;

    public CellImpl( Nominal cellNominal, Integer cellCount ) {
        this.nominal = cellNominal;
        this.count = cellCount; //Initial load
    }

    @Override
    public void put( Integer count ) {
        if (this.count + count > MAX_CAPACITY){
            throw new CellIsFullException();
        }
        this.count += count;
    }

    @Override
    public Integer get( Integer count ) {
        Integer toReturn = ( this.count >= count ) ? count : this.count;
        this.count -= toReturn;
        return toReturn;
    }

    @Override
    public Integer getCount() {
        return this.count;
    }

    @Override
    public Nominal getNominal() {
        return this.nominal;
    }


    @Override
    public Integer getBalance() {
        return nominal.getNominal()*count;
    }
}

