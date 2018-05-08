package june.hrtf;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Interface that should be implemented for different databases.
 */
interface HrtfSubject
{

    public INDArray getHrirL();
    public INDArray getHrirR();
    public INDArray getItd();
    public String getName();
    public INDArray getOnL();
    public INDArray getOnR();
    public double[] getPossibleElevations();
    public double[] getPossibleAzimuths();
}
