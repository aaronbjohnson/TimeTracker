public class Project {
    private String name;
    private double totalTime;

    public Project(String name) {
        this.name = name;
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
