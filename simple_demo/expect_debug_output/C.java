import java.util.*;
public class C {
    public void example() {
        Object foo = null;
        foo.toString();
    }
    public String unRelatedMethod() {
        List<String> aList = new LinkedList<String>();
        String baz = aList.get(0);
        return baz;
    }
}