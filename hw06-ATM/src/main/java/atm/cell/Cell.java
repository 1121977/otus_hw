package atm.cell;
import atm.Nominal;

public interface Cell {
    public static final Integer MAX_CAPACITY = 10;
    public static final Integer MAX_COUNT = 100;

    public void put( Integer count );
    public Integer get( Integer count );
    public Integer getCount();
    public Nominal getNominal();
    public Integer getBalance();
}
