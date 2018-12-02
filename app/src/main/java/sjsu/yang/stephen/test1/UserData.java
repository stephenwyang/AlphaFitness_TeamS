package sjsu.yang.stephen.test1;

public class UserData {
    private float wDist;
    private float wTime;
    private float wCal;
    private long wDate;

    public UserData(){

    }
    public float getwDist() {
        return wDist;
    }
    public float getwTime() {
        return wTime;
    }
    public float getwCal() {
        return wCal;
    }
    public long getwDate() {
        return wDate;
    }
    public void setwDist(float f) {
        wDist = f;
    }
    public void setwTime(float f) {
        wTime = f;
    }
    public void setwCal(float f) {
        wCal = f;
    }
    public void setwDate(long l) {
        wDate = l;
    }

}
