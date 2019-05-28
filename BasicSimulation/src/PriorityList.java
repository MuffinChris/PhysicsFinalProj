import java.util.ArrayList;
import java.util.List;

public class PriorityList {

    private List<PhysicsObject> objects;

    public PriorityList() {
        objects = new ArrayList<PhysicsObject>();
    }

    public List<PhysicsObject> getList() {
        return objects;
    }

    public void sortByPrio() {
        List<PhysicsObject> newList = new ArrayList<PhysicsObject>();
        int min = -1;
        PhysicsObject minO = null;
        while (newList.size() != objects.size()) {
            for (PhysicsObject o : objects) {
                int priority = o.getPriority();
                if (priority > min) {
                    min = priority;
                    minO = o;
                }
            }
            if (minO instanceof PhysicsObject) {
                newList.add(minO);
                int index = objects.indexOf(minO);
                minO.setPriority(-1);
                objects.set(index, minO);
            } else {
                System.out.println("FATAL ERROR: NO OBJECTS IN OBJECT LIST");
                System.exit(1);
            }
            min = -1;
        }
        objects = newList;
    }

}
