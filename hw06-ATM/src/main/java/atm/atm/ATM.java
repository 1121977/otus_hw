package atm.atm;

import atm.Nominal;
import atm.cell.Cell;

import java.util.List;

public interface ATM {
    boolean putCash( Nominal cash);
    List<Nominal> getCash(Integer sum);
    List<Cell> getAtmStorage();
}

