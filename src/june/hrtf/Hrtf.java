package june.hrtf;

/**
 * Allows a user to access the subject for the respective HRTF database.
 */
public class Hrtf
{
    /**
     * Creates a CIPIC Database Subject from the CIPIC Database.
     * @param subjectNumber the number of the subject.
     * @return
     */
    public static CipicSubject getCipicSubject(String subjectNumber)
    {
        return new CipicSubject(subjectNumber);
    }
}
