package june.csv;

import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.buffer.DoubleBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.io.*;

/**
 * Parses CIPIC CSV Files.
 */
public class CSVCipic
{
    private String subjectName;
    private BufferedReader bufferedReader;
    private String cipicDirectory;

    CSVCipic(String name)
    {
        subjectName = name;
        if((cipicDirectory = System.getenv("CIPIC") + "/csv/") == null)
        {
            System.out.println("CIPIC environment available.\n +" +
                                       "please set the variable to the location of database");
        }
    }

    /**
     * Reads the hrir_l values from the respective CSV and stores in a Rank 3 INDArray
     * @return INDArray Rank 3 filled with hrir_l data
     */
    public INDArray readHrir_l()
    {
        String fileName = cipicDirectory + "subject_" + String.format("%03d", Integer.valueOf(subjectName)) + "/" + "hrir_l.csv";
        openReader(fileName);

        INDArray hrir_l = read25x50x200();

        closeReader();

        return hrir_l;
    }

    /**
     * Reads the hrir_r values from the respective CSV and stores in a Rank 3 INDArray
     * @return INDArray Rank 3 filled with hrir_r data
     */
    public INDArray readHrir_r()
    {

        String fileName = cipicDirectory + "subject_" + String.format("%03d", Integer.valueOf(subjectName)) + "/" + "hrir_r.csv";
        openReader(fileName);

        INDArray hrir_r = read25x50x200();

        closeReader();

        return hrir_r;
    }

    /**
     * Reads the ITD from the respective CSV file.
     * @return INDArray containing the ITD data
     */
    public INDArray readITD()
    {
        String fileName = cipicDirectory + "subject_" + String.format("%03d", Integer.valueOf(subjectName)) + "/" + "itd.csv";
        openReader(fileName);
        INDArray itdArray = read25x50();
        closeReader();
        return itdArray;
    }


    /**
     * Reads the OnL data from the respective CSV file.
     * @return INDArray containing OnL data
     */
    public INDArray readOnL()
    {
        String fileName = cipicDirectory + "subject_"  + String.format("%03d", Integer.valueOf(subjectName)) + "/" + "on_l.csv";
        openReader(fileName);
        INDArray OnL = read25x50();
        closeReader();
        return OnL;
    }

    /**
     * Reads the OnR data from the respective CSV file.
     * @return INDArray containg OnR data
     */
    public INDArray readOnR()
    {
        String fileName = cipicDirectory + "subject_" + String.format("%03d", Integer.valueOf(subjectName)) + "/" + "on_r.csv";
        openReader(fileName);
        INDArray OnR = read25x50();
        closeReader();
        return OnR;
    }

    /*
        Reads a 25x50x200 CSV file.
     */
    private INDArray read25x50x200()
    {
        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
        INDArray outputArray = Nd4j.create(25, 50, 200);
        INDArray tempRow = Nd4j.create(50);
        DoubleBuffer buffer = new DoubleBuffer(50);
        String line = "";
        int row = 0, column = 0, channel = 0;

        try
        {
            while((line = bufferedReader.readLine()) != null)
            {
                String[] dataPoints = line.split(",");
                for (String hrtf_data: dataPoints)
                {
                    // We have finished all channels for a given row
                    if(channel == 200)
                    {
                        if(++row == 25)
                        {
                            break;
                        }
                        channel = 0;
                    }

                    // We have finished the last column for a given row in a given channel
                    // Assign the new row, move to next channel, reset the column
                    if (column == 49)
                    {
                        buffer.put(column, Double.valueOf(hrtf_data));
                        tempRow = Nd4j.create(buffer);

                        // Grab the data at this row for all the columns on that given channel
                        // Assign that to our new read in data.
                        outputArray.get(NDArrayIndex.point(row), NDArrayIndex.all(), NDArrayIndex.point(channel)).assign(tempRow);

                        buffer.flush();

                        column = 0;
                        channel++;
                    }
                    else
                    {
                        buffer.put(column, Double.valueOf(hrtf_data));
                        column++;
                    }
                }

            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return outputArray;
    }

    /*
        Reads a 25x50 CSV file.
     */
    private INDArray read25x50()
    {
        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
        INDArray outputArray = Nd4j.create(25, 50);
        INDArray tempRow = Nd4j.create(50);
        DoubleBuffer buffer = new DoubleBuffer(50);
        int row = 0, column = 0;
        try
        {
            String line = "";
            while((line = bufferedReader.readLine()) != null)
            {
                String[] values = line.split(",");
                for (String delayValue: values)
                {
                    if (column == 49)
                    {
                        buffer.put(column, Double.valueOf(delayValue));
                        tempRow = Nd4j.create(buffer);

                        outputArray.get(NDArrayIndex.point(row), NDArrayIndex.all()).assign(tempRow);
                        buffer.flush();

                        column = 0;
                        row++;
                    }
                    else
                    {
                        buffer.put(column, Double.valueOf(delayValue));
                        column++;
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return outputArray;
    }

    /*
        Close reader after using.
     */
    private void closeReader()
    {
        try
        {
            bufferedReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
        Gets rid of some of the clutter with multiple try catches.
     */
    private void openReader(String fileName)
    {
        try
        {
            bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
