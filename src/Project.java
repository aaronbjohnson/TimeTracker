/**
 * The Project class stores information about a project, and keeps track of the total time spent working on a project.
 *
 * @author Aaron Johnson
 */
public class Project {
    private String name;
    private double totalTime;

    public Project(String name) {
        this.name = name;
    }

    public Project(String name, double totalTime) {
        this.name = name;
        this.totalTime = totalTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", totalTime=" + totalTime +
                '}';
    }
}
