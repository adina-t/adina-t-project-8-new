package lineChart;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * An object of class RecursionInspect tests the run time associated with various array sizes and
 * recursion limits. The data will the be write to a text file in the format of [limit, run-time]
 *
 * @author Adina T.
 */
public class RecursionInspect
{
    /**
     * Produce an array of random ints.
     *
     * @param size size of array
     * @return the array created
     */
    public static Integer[] generateRandIntArray(int size)
    {
        Random rand = new Random();
        Integer[] randIntArray = new Integer[size];
        int count = 0;
        while (count < size)
            randIntArray[count++] = rand.nextInt();

        return randIntArray;
    }

    /**
     * Write the list of pairs of data to a CSV file
     * @param dataPairs a series of xy pairs as an array
     * @param arrSize the array size the xy-pairs belong to
     * @param outputFile the file to write to
     */
    public static void writeToFile(long[][] dataPairs, int arrSize, FileWriter outputFile)
    {

        try
        {
            outputFile.write("size," + arrSize + "\n");
            for(int i = 0; i < dataPairs.length; i++)
                outputFile.write(dataPairs[i][0] + ","
                        + dataPairs[i][1] + "\n");

        }
        catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }



    // constants for testing quicksort with various cases
    final static int RECUR_LIM_MIN = 2;
    final static int RECUR_LIM_MAX = 300;
    final static int ARRAY_SIZE = 100000;
    final static int TEST_CYCLES = 4;  //times to test quicksort to avoid outliers

    /**
     * Test sorting the the array using quicksort several times with different recursion limits,
     * then write the limit and the run time as pairs into the text file
     * @param outputFile the file to write the data pairs to
     */
    public RecursionInspect(FileWriter outputFile) {

        int dataSetSize = (RECUR_LIM_MAX - RECUR_LIM_MIN) / 2 + 1;
        long[][] xyDataSet = new long[dataSetSize][2];
        Integer[] unsortedInts = RecursionInspect.generateRandIntArray(ARRAY_SIZE);

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
        RecursionInspect.writeToFile(xyDataSet, ARRAY_SIZE, outputFile);
        System.out.println("writing done for size " + ARRAY_SIZE);
    }
}
