import java.util.*;

/**
 * Some class javadoc.
 **/
public class C {

    public void example() {

        Object foo = null;
        foo.toString();

        Object bar = new Object();
        bar.toString();
    }

    public String unRelatedMethod() {

        List<String> aList = new LinkedList<String>();
        List<String> bar = aList;

        String quux = "nonNull";
        aList.add(quux);
        aList.add("quux");
        String baz = aList.get(0);
        return baz;

    }

}