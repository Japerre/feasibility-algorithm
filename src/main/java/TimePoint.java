import java.util.ArrayList;
import java.util.List;

public class TimePoint {

    private int id;
    private int domainSize;
    private List<Domain> domainList;
    private int currentDomainIndex;

    TimePoint(int id, int domainSize, List<Domain> domainList){
        this.id = id;
        this.domainSize = domainSize;
        this.domainList = domainList;
        currentDomainIndex = 0;
    }

    TimePoint(int id, Domain domain){
        this.id = id;
        this.domainSize = 1;
        this.domainList = new ArrayList<>();
        domainList.add(domain);
        currentDomainIndex = 0;
    }

    public int getId() {
        return id;
    }

    public int getDomainSize() {
        return domainSize;
    }

    public Domain getCurrentDomain(){
        return domainList.get(currentDomainIndex);
    }

    public Domain getNextDomain() {
        currentDomainIndex++;
        return domainList.get(currentDomainIndex);
    }

    public boolean isLastDomain(){
        return currentDomainIndex == domainSize-1;
    }

    @Override
    public String toString() {
        return "TimePoint{" +
                "id=" + id +
                ", domainSize=" + domainSize +
                ", domainList=" + domainList +
                '}';
    }
}
