package sjsu.yang.stephen.test1;

public class User {
    private long uID;
    private String uName;
    private String uGender;
    private float uWeight;

    public User(long id, String n, String g, float w) {
        uID = id;
        uName = n;
        uGender = g;
        uWeight = w;
    }

    public User() {

    }
    public long getuID() {
        return uID;
    }
    public String getuName() {
        return uName;
    }
    public String getuGender() {
        return uGender;
    }
    public float getuWeight() {
        return uWeight;
    }
    public void setuName(String n) {
        uName = n;
    }
    public void setuGender(String g) {
        uGender = g;
    }
    public void setuWeight(float f) {
        uWeight = f;
    }
    public void setuID(long l) { uID = l; }


}
