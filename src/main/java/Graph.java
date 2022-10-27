import java.util.ArrayList;
import java.util.List;

public class Graph {

    private int nTimePoints;
    private int nArcs;
    private List<TimePoint> timePoints;
    private List<List<Arc>> adjList;

    public Graph(int nTimePoints, int nArcs, List<TimePoint> timePoints, List<List<Arc>> adjList) {
        this.nTimePoints = nTimePoints;
        this.nArcs = nArcs;
        this.timePoints = timePoints;
        this.adjList = adjList;
    }

    public List<Arc> getOutgoingArcsTAlfa(){
        List<Arc> arcsFromAlfa = new ArrayList<>();
        for(TimePoint t : timePoints){
            Domain d = t.getCurrentDomain();
            arcsFromAlfa.add(new Arc(0, t.getId(), -d.getL()));
        }
        return arcsFromAlfa;
    }

    public List<Arc> getOutgoingArcs(int id){
        TimePoint timePoint = getTimePoint(id);
//        System.out.println(timePoint);
        Domain domain = timePoint.getCurrentDomain();
//        adjList.get(id).add(new Arc(timePoint.getId(), 0, domain.getU()));
        return adjList.get(id);
    }

    public TimePoint getTimePoint(int id){
        return timePoints.get(id-1);
    }

    public int getnTimePoints() {
        return nTimePoints;
    }

    public int getnArcs() {
        return nArcs;
    }

    public List<TimePoint> getTimePoints() {
        return timePoints;
    }


    public void updateGraphArcs(int t_iID) {
        TimePoint t_i = timePoints.get(t_iID);
        Domain d_i = t_i.getCurrentDomain();
    }
}
