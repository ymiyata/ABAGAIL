package opt.test;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class OneMaxMimicTest {
    /** The n value */
    private static final int N = 30;
    private static final int T = 3;
    private static final int N_TEST = 10;
    private static final double OPTIMAL_SOLN = 100;

    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random();

        OneMaxEvaluationFunction ef = new OneMaxEvaluationFunction();
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        DiscreteUniformDistribution odd = new  DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        double bestEstimate = 0.0;
        double averageValue = 0.0;
        long totalTrainTime = 0;
        for (int t = 0; t < N_TEST; t++) {
            MIMIC mimic = new MIMIC(200, 100, pop);
            FixedIterationTrainer fit = new FixedIterationTrainer(mimic, 5000);

            long trainTime = System.currentTimeMillis();
            fit.train();
            totalTrainTime += (System.currentTimeMillis() - trainTime);

            double estimate = ef.value(mimic.getOptimal());
            averageValue += estimate;
            if (estimate > bestEstimate) {
                bestEstimate = estimate;
            }
        }
        averageValue /= N_TEST;
        System.out.println("Avg Elapsed Time: " + totalTrainTime/N_TEST);
        System.out.println("Average Value: " + averageValue);
        System.out.println("Best Value: " + bestEstimate);
        System.out.println("Error: " + Math.abs(bestEstimate - OPTIMAL_SOLN));
    }
}
