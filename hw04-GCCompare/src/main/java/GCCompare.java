import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
-Xms48m -Xmx48m -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level -XX:+UseSerialGC
-XX:+UseSerialGC (<default>)
-XX:+UseParallelGC
-XX:+UseG1GC
-XX:+UnlockExperimentalVMOptions -XX:+UseZGC
 */

public class GCCompare {
    public static void main(String ... args){
        List<Date> dateArray = new ArrayList<>();
        dateArray.add(new Date());
        while (true){
            int currentSize = dateArray.size();
            System.out.println("currentSize is " + currentSize);
            for (int i=currentSize-1;i>=currentSize-(currentSize>>1);i--){
                dateArray.remove((int) (Math.random()*dateArray.size()));
            }
            for (int i = 0; i < currentSize; i++) {
                dateArray.add(new Date());
            }
        }
    }
}
