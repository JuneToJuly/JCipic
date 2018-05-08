package june.csv;

/**
 * As a general way of organizing different CSVReaders for different types of HRTF Databases.
 */
public class CSVReader
{
    /*
        Don't need to instantiate this class.
     */
    private CSVReader(){}

    /**
     * Gets instance of the Cipic CSV Parser.
     * @param name the name of the HRTF subject
     * @return instance of CSVCipic
     */
    public static CSVCipic getCipic(String name)
    {
        return new CSVCipic(name);
    }
}
