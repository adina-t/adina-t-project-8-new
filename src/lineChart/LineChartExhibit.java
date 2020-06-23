package lineChart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * An object of LineChartExhibit creates line charts by reading in (pseudo-) CSV file and
 * populate the line.
 *
 * @author Adina T.
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
        Scanner input = null;
        try
        {
            input = new Scanner(file);
            drawLineFromCSV(input, lineChart);
            System.out.println("drawn");
        }
        catch(FileNotFoundException e) {}

        input.close();
        Scene scene = new Scene(lineChart,800,600);
        stage.setScene(scene);

        // make the stage visible
        stage.show();

    }

    /**
     * Represent the data as line in a line chart based on the data passed.
     * @param input the file with the data pairs
     * @param lineChart the chart to draw on
     */
    public static void drawLineFromCSV(Scanner input, LineChart<Number, Number> lineChart)
    {
        if(input == null)
            return;

        //defining a series and a pair
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        XYChart.Data<Number, Number> dataPair;


        // Text file format
        // size: [array size]
        // [ x1 ] [ y1 ]                          x: recursion limit, y: time used
        // [ x2 ] [ y2 ]
        // ...
        int line = 0;
        if(input.hasNextLine())
        {
            String[] header = input.nextLine().split(",");
            series.setName(header[1]);
        }

        while(input.hasNextLine())
        {
            line++;
            String[] tokens = input.nextLine().split(",");

            // parse the int values and add them as pairs
            int xVal = Integer.parseInt(tokens[0]);
            int yVal = Integer.parseInt(tokens[1]);
            dataPair = new XYChart.Data<>(xVal, yVal);
            series.getData().add(dataPair);

        }

        lineChart.getData().add(series);
    }


    /**
     * Launch the javafx application.
     * @param args used by the machine
     */
    public static void main(String[] args) {
        launch(args);
    }
}
