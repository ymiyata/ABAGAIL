package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * A one max evaluation function
 * @author Yoshiya Miyata
 * @version 1.0
 */
public class OneMaxEvaluationFunction implements EvaluationFunction {
    /**
     * Make a new four peaks function
     * @param n the n value
     */
    public OneMaxEvaluationFunction() {}

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        int ones = 0;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == 1) {
                ones++;
            }
        }
        return ones;
    }
}
