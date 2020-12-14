package atm.atm;

import atm.Nominal;
import java.util.List;

public interface ATM {
    boolean putCash( Nominal cash);
    List<Nominal> getCash(Integer sum);
    Integer getBalance();
}

