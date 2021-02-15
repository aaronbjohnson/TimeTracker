import java.util.HashMap;
import java.util.Map;

public class TimeSheet {
    private double grandTotal;
    private int id = 0;


    private Map<Integer, Project> projectMap = new HashMap<>();

    public TimeSheet(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    /**
     * This method adds a new project the to TimeSheet. The id accumulator is first increased so that the project gets
     * the proper id assignment.
     * @param project A Project to be added to the TimeSheet.
     */
    public void addProject(Project project) {
        id++;
        projectMap.put(id, project);
    }

    /**
     * This method deletes a project from the TimeSheet
     * @param key An int representing the id of the project to be deleted.
     */
    public void deleteProject(int key) {
        projectMap.remove(key);
    }

    /**
     * This method shows how many projects have been stored in the TimeSheet
     * @return An int representing the number of projects in a TimeSheet.
     */
    public int getNumberProjects() {
        return projectMap.size();
    }

    /**
     * This method is used to display the total of hours worked across all projects in a given Time Sheet.
     * @return A double representing the total hours of all Projects' totalTime variable.
     */
    public double getGrandTotal() {
        double total = 0.0;
        for (Map.Entry<Integer, Project> entry : projectMap.entrySet()) {
            total += entry.getValue().getTotalTime();
        }

        return total;
    }


    /**
     * This method returns a reference to the Map of stored Projects in projectMap.
     * @return A Map of Projects along with their id's.
     */
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
