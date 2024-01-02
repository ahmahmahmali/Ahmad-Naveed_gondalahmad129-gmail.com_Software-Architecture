// Competition.java
package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Competition extends Application {

    private CompetitorList competitorList = new CompetitorList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadDataFromFile();

        primaryStage.setTitle("Quiz System");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createQuizPane());

        Button addMarksButton = new Button("Add Marks");
        addMarksButton.setOnAction(event -> showAddMarksDialog());

        Button addCompetitorButton = new Button("Add Competitor");
        addCompetitorButton.setOnAction(event -> showAddCompetitorDialog());

        GridPane gridPane = createQuizPane();
        gridPane.add(addMarksButton, 0, 3);
        gridPane.add(addCompetitorButton, 1, 3);

        borderPane.setCenter(gridPane);

        primaryStage.setScene(new Scene(borderPane, 600, 400));
        primaryStage.show();
    }

    private void addMarksForCompetitor(String name, int additionalMarks) {
        Competitor competitor = findCompetitorByName(name);
        if (competitor != null) {
            competitor.addMarks(additionalMarks);
            showAlert("Marks Added", additionalMarks + " marks added for " + name);
            saveDataToFile(); // Save the updated data to the file
        } else {
            showAlert("Error", "Competitor not found");
        }
    }

    private void showAddMarksDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Marks");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter competitor name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            TextInputDialog marksDialog = new TextInputDialog();
            marksDialog.setTitle("Add Marks");
            marksDialog.setHeaderText(null);
            marksDialog.setContentText("Enter additional marks:");

            Optional<String> marksResult = marksDialog.showAndWait();
            marksResult.ifPresent(marks -> {
                try {
                    int additionalMarks = Integer.parseInt(marks);
                    addMarksForCompetitor(name, additionalMarks);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input for marks. Please enter a valid number.");
                }
            });
        });
    }

    private void addCompetitor(String name, int initialMarks) {
        competitorList.addCompetitor(new Competitor(name, initialMarks));
        showAlert("Competitor Added", "Competitor " + name + " added with initial marks: " + initialMarks);
        saveDataToFile(); // Save the updated data to the file
    }

    private void showAddCompetitorDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Competitor");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter competitor name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            TextInputDialog marksDialog = new TextInputDialog();
            marksDialog.setTitle("Add Competitor");
            marksDialog.setHeaderText(null);
            marksDialog.setContentText("Enter initial marks:");

            Optional<String> marksResult = marksDialog.showAndWait();
            marksResult.ifPresent(marks -> {
                try {
                    int initialMarks = Integer.parseInt(marks);
                    addCompetitor(name, initialMarks);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input for marks. Please enter a valid number.");
                }
            });
        });
    }

    private GridPane createQuizPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label marksLabel = new Label("Marks:");
        TextField marksField = new TextField();

        Button viewMarksButton = new Button("View Marks");
        viewMarksButton.setOnAction(event -> {
            String name = nameField.getText();
            Competitor competitor = findCompetitorByName(name);
            if (competitor != null) {
                showAlert("Marks for " + name, "Marks: " + competitor.getMarks());
            } else {
                showAlert("Error", "Competitor not found");
            }
        });

        Button staffLoginButton = new Button("Staff Login");
        staffLoginButton.setOnAction(event -> showStaffLogin());

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(marksLabel, 0, 1);
        gridPane.add(marksField, 1, 1);
        gridPane.add(viewMarksButton, 0, 2);
        gridPane.add(staffLoginButton, 1, 2);

        return gridPane;
    }

    private void showStaffLogin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Staff Login");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter password:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(password -> {
            if (password.equals("your_staff_password")) {
                showStaffOptions();
            } else {
                showAlert("Error", "Incorrect password");
            }
        });
    }

    private void showStaffOptions() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("View Individual Scores", "View Statistical Reports");
        dialog.setTitle("Staff Options");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose an option:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(option -> {
            if (option.equals("View Individual Scores")) {
                getFullDetails();
            } else if (option.equals("View Statistical Reports")) {
                getOverallScore();
            }
        });
    }

    private void getFullDetails() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Individual Scores");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter competitor name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Competitor competitor = findCompetitorByName(name);
            if (competitor != null) {
                showAlert("Scores for " + name, "Marks: " + competitor.getMarks());
            } else {
                showAlert("Error", "Competitor not found");
            }
        });
    }

    private void getOverallScore() {
        int totalMarks = 0;
        int highestScore = Integer.MIN_VALUE;
        int lowestScore = Integer.MAX_VALUE;
        int numberOfCompetitors = competitorList.getCompetitors().size();

        for (Competitor competitor : competitorList.getCompetitors()) {
            int marks = competitor.getMarks();
            totalMarks += marks;
            if (marks > highestScore) {
                highestScore = marks;
            }
            if (marks < lowestScore) {
                lowestScore = marks;
            }
        }

        double averageMarks = (double) totalMarks / numberOfCompetitors;

        StringBuilder report = new StringBuilder();
        report.append("Statistical Report:\n");
        report.append("Total Competitors: ").append(numberOfCompetitors).append("\n");
        report.append("Total Marks: ").append(totalMarks).append("\n");
        report.append("Average Marks: ").append(averageMarks).append("\n");
        report.append("Highest Score: ").append(highestScore).append("\n");
        report.append("Lowest Score: ").append(lowestScore);

        showAlert("Statistical Reports", report.toString());
    }

    private Competitor findCompetitorByName(String name) {
        for (Competitor competitor : competitorList.getCompetitors()) {
            if (competitor.getName().equals(name)) {
                return competitor;
            }
        }
        return null;
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("competitors.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int marks = Integer.parseInt(parts[1]);
                competitorList.addCompetitor(new Competitor(name, marks));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("competitors.txt"))) {
            for (Competitor competitor : competitorList.getCompetitors()) {
                writer.write(competitor.getName() + "," + competitor.getMarks());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        saveDataToFile();
    }

    // Add these methods to the Competition class

    public Competitor getOverallHighestScorer() {
        Competitor highestScorer = null;
        int highestScore = Integer.MIN_VALUE;

        for (Competitor competitor : competitorList.getCompetitors()) {
            if (competitor.getMarks() > highestScore) {
                highestScore = competitor.getMarks();
                highestScorer = competitor;
            }
        }

        return highestScorer;
    }

    public int getTotalMarks() {
        int totalMarks = 0;
        for (Competitor competitor : competitorList.getCompetitors()) {
            totalMarks += competitor.getMarks();
        }
        return totalMarks;
    }

    public double getAverageMarks() {
        int totalMarks = getTotalMarks();
        int numberOfCompetitors = competitorList.getCompetitors().size();
        return numberOfCompetitors > 0 ? (double) totalMarks / numberOfCompetitors : 0;
    }

    public int getLowestScore() {
        int lowestScore = Integer.MAX_VALUE;
        for (Competitor competitor : competitorList.getCompetitors()) {
            int marks = competitor.getMarks();
            if (marks < lowestScore) {
                lowestScore = marks;
            }
        }
        return lowestScore;
    }

    public int getHighestScore() {
        int highestScore = Integer.MIN_VALUE;
        for (Competitor competitor : competitorList.getCompetitors()) {
            int marks = competitor.getMarks();
            if (marks > highestScore) {
                highestScore = marks;
            }
        }
        return highestScore;
    }

    public void displayFrequencyReport() {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (Competitor competitor : competitorList.getCompetitors()) {
            int marks = competitor.getMarks();
            frequencyMap.put(marks, frequencyMap.getOrDefault(marks, 0) + 1);
        }

        System.out.println("Marks\tFrequency");
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

	public CompetitorList getCompetitorList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void startCompetition() {
		// TODO Auto-generated method stub
		
	}
}
