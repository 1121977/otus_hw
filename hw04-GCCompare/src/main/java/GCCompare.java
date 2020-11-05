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
    static final int ARRAY_SIZE = 64;
    public static void main(String ... args){
        List<int[]> intArrayList = new ArrayList<>();
        intArrayList.add(new int[ARRAY_SIZE]);
        while (true){
            int currentSize = intArrayList.size();
            System.out.println("currentSize is " + currentSize);
            for (int i=currentSize-1;i>currentSize/2;i--){
                intArrayList.remove((int) (Math.random()*intArrayList.size()));
            }
            for (int i = 0; i < currentSize; i++) {
                intArrayList.add(new int[ARRAY_SIZE]);
            }
        }
    }
}
