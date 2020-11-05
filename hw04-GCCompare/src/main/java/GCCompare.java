import java.util.ArrayList;
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
        List<Long> longArrayList = new ArrayList<>();
        longArrayList.add((long) (Math.random() * Long.MAX_VALUE));
        while (true){
            int currentSize = longArrayList.size();
            System.out.println("currentSize is " + currentSize);
            for (int i=currentSize-1;i>currentSize/2;i--){
                longArrayList.remove((int) (Math.random()*longArrayList.size()));
            }
            for (int i = 0; i < currentSize; i++) {
                longArrayList.add((long)(Math.random()*Long.MAX_VALUE));
            }
        }
    }
}
