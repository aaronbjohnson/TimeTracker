import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeSheet {
    private double grandTotal;
    private static int id = 0;


    private Map<Integer, Project> projectMap = new HashMap<Integer, Project>();

    // todo: delete this I think
    private ArrayList<Project> projects;

    public TimeSheet(double grandTotal) {
        this.grandTotal = grandTotal;
        this.projects  = new ArrayList<Project>();
    }

    public void addProject(Project project) {
        id++;
        projectMap.put(id, project);
    }

    public void deleteProject(int key) {
        projectMap.remove(key);
    }

   /* public void addProject(Project newProject) {
        projects.add(newProject);
    }*/

    public int getNumberProjects() {
        return projectMap.size();
    }

    /*public int getNumberProjects() {
        if (!(projects == null)) {
            return projects.size();
        } else {
            return 0;
        }
    }*/

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public Map<Integer, Project> getProjectMap() {
        return projectMap;
    }

    @Override
    public String toString() {
        return "TimeSheet{" +
                "grandTotal=" + grandTotal +
                ", projectMap=" + projectMap +
                '}';
    }
}
