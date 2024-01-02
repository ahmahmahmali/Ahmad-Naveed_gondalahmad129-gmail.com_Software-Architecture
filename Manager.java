// Manager.java
package application;

public class Manager {
    public static void main(String[] args) {
        Competition competition = new Competition();
        competition.startCompetition();
        generateReports(competition);
    }

    private static void generateReports(Competition competition) {
        CompetitorList competitorList = competition.getCompetitorList();

        // Table of Competitors
        System.out.println("Table of Competitors:");
        System.out.println("Name\tMarks");
        for (Competitor competitor : competitorList.getCompetitors()) {
            System.out.println(competitor.getName() + "\t" + competitor.getMarks());
        }
        System.out.println();

        // Competitor with the Highest Overall Score
        Competitor highestScoreCompetitor = competition.getOverallHighestScorer();
        System.out.println("Competitor with the Highest Overall Score:");
        System.out.println("Name: " + highestScoreCompetitor.getName());
        System.out.println("Marks: " + highestScoreCompetitor.getMarks());
        System.out.println();

        // Other Summary Statistics
        System.out.println("Summary Statistics:");
        System.out.println("Total Competitors: " + competitorList.getCompetitors().size());
        System.out.println("Total Marks: " + competition.getTotalMarks());
        System.out.println("Average Marks: " + competition.getAverageMarks());
        System.out.println("Lowest Score: " + competition.getLowestScore());
        System.out.println("Highest Score: " + competition.getHighestScore());
        System.out.println();

        // Frequency Report
        System.out.println("Frequency Report:");
        competition.displayFrequencyReport();
    }
}

