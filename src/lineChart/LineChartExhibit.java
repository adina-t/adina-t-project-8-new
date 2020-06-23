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

public class LineChartExhibit extends Application {

    final static int RECUR_LIM_MIN = 2;
    final static int RECUR_LIM_MAX = 300;
    final static int ARRAY_SIZE_MIN = 20000;
    final static int ARRAY_SIZE_MAX = 10000000;
    final static int DATA_SETS = 20;
    final static int TEST_CYCLES = 4;

    /**
     * Produce the line chart (invoke by the Program automatically).
     * @param stage the stage to show the chart
     * @throws Exception exception thrown when not working as intended
     */
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("lineChartExhibit.fxml"));

        stage.setScene(new Scene(root, 300, 275));
        stage.show();

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

        int interval = (ARRAY_SIZE_MAX - ARRAY_SIZE_MIN) / (DATA_SETS - 1);
        for(int arrSize = ARRAY_SIZE_MIN; arrSize <= ARRAY_SIZE_MIN + interval; arrSize += interval) {

            int dataSetSize = (RECUR_LIM_MAX - RECUR_LIM_MIN) / 2 + 1;
            long[][] xyDataSet = new long[dataSetSize][2];
            Integer[] unsortedInts = RecursionInspect.generateRandIntArray(arrSize);

            for (int limit = RECUR_LIM_MIN, index = 0; limit <= RECUR_LIM_MAX; limit += 2) {

                //System.out.println("in recursion for loop");
                long startTime, estimatedTime = 0;
                FHsort.setRecursionLimit(limit);

                // Test several times to avoid non-significant differences
                for (int i = 0; i < TEST_CYCLES; i++) {
                    // start timer - sort cloned array - stop time
                    startTime = System.nanoTime();
                    FHsort.quickSort(unsortedInts.clone());
                    long newEstimatedTime = System.nanoTime() - startTime;

                    // take the least time as the most accurate value
                    if (estimatedTime == 0 || estimatedTime > newEstimatedTime)
                        estimatedTime = newEstimatedTime;
                }

                // capture rescursion limit as x value, runtime as y value
                long[] xyPair = {limit, estimatedTime};
                xyDataSet[index++] = xyPair;
            }

            // write the xy-pairs to the text file
            RecursionInspect.writeToFile(xyDataSet, arrSize, outputFile);
            System.out.println("writing done for size " + arrSize);

        }
        // close the output file
        outputFile.close();

        // flush the error stream
        System.err.flush();

        // read the output file and create the line on the chart
        //RecursionInspect.drawLineFromCSV(file, lineChart);
        System.out.println("drawn");

        Scene scene  = new Scene(lineChart,800,600);
        stage.setScene(scene);

        // make the stage visible
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
