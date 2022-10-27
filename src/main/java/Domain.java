public class Domain {

    private int l;
    private int u;
    private int cost;

    public Domain(int l, int u, int cost) {
        this.l = l;
        this.u = u;
        this.cost = cost;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "l=" + l +
                ", u=" + u +
                '}';
    }
}
