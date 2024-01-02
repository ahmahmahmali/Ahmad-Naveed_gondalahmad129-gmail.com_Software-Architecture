// CompetitorList.java
package application;

import java.util.ArrayList;
import java.util.List;

public class CompetitorList {
    private List<Competitor> competitors = new ArrayList<>();

    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public Competitor findCompetitorByName(String name) {
        for (Competitor competitor : competitors) {
            if (competitor.getName().equals(name)) {
                return competitor;
            }
        }
        return null;
    }

    // Other methods for generating reports, statistics, etc.
}

class Competitor {
    private String name;
    private int marks;

    public Competitor(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }

    public void addMarks(int additionalMarks) {
        this.marks += additionalMarks;
    }
    // You may need additional methods or fields based on your requirements
}