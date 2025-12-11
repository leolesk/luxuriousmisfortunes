package luxuriousmisfortunes.util;

public class MutationEntry {
    long lastUse;
    int usedTimes;

    public MutationEntry(long lastUse, int count) {
        this.lastUse = lastUse;
        this.usedTimes = count;
    }

    public long getLastUse() {
        return this.lastUse;
    }

    public int getUsedTimes() {
        return this.usedTimes;
    }

}
