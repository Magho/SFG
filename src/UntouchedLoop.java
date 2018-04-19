public class UntouchedLoop {

    UntouchedLoop(Loop loop1, Loop loop2) {

        this.loop1 = loop1;
        this.loop2 = loop2;
    }

    private Loop loop1;
    private Loop loop2;

    public int getGain (){
        return loop1.getGain() + loop2.getGain();
    }
}
