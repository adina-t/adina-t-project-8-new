package lineChart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;

/**
 * An object of LineChartExhibit creates
 */
public class LineChartExhibit extends Application {

    /**
     * Produce the line chart (invoke by the Program automatically).
     * @param stage the stage to show the chart
     * @throws Exception exception thrown when not working as intended
     */
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("lineChartExhibit.fxml"));

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Correlation of Recursion Limit and Run Time");

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Recursion Limit");
        yAxis.setLabel("Run Time (in nanoseconds)");

        //create the chart
        LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        // set up the chart
        lineChart.setTitle("The Effect of Recursion Limit of Quick sort on Run Time");
        lineChart.setCreateSymbols(false);

        // create the output file and the writer
        File file = new File("resources/recursion_limit_data_pairs.txt");
        FileWriter outputFile = new FileWriter(file);


        // create the instance to write the data needed into the text file first
        RecursionInspect newLineToDraw = new RecursionInspect(outputFile);

        // close the output file
        outputFile.close();

        // flush the error stream
        System.err.flush();

        // read the output file and create the line on the chart
        RecursionInspect.drawLineFromCSV(file, lineChart);
        System.out.println("drawn");

        Scene scene  = new Scene(lineChart,800,600);
        stage.setScene(scene);

        // make the stage visible
        stage.show();

    }

    /**
     * Launch the javafx application.
     * @param args used by the machine
     */
    public static void main(String[] args) {
        launch(args);
    }
}
