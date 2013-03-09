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
public class TravelingSalesmanMimicTest {
    /** The n value */
    private static final int N = 48;
    private static final int N_TEST = 10;

    // create the random points
    private static final double[][] points = {
        {6734, 1453}, {2233,   10}, {5530, 1424}, { 401,  840}, {3082, 1644}, {7608, 4453},
        {7573, 3716}, {7265, 1268}, {6898, 1885}, { 112, 2049}, {5468, 2606}, {5989, 2873},
        {4706, 2674}, {4612, 2035}, {6347, 2683}, {6107,  669}, {7611, 5184}, {7462, 3590},
        {7732, 4732}, {5900, 3561}, {4483, 3369}, {6101, 1110}, {5199, 2182}, {1633, 2809},
        {4307, 2322}, { 675, 1006}, {7555, 4819}, {7541, 3981}, {3177,  756}, {7352, 4506},
        {7545, 2801}, {3245, 3305}, {6426, 3173}, {4608, 1198}, {  23, 2216}, {7248, 3779},
        {7762, 4595}, {7392, 2244}, {3484, 2829}, {6271, 2135}, {4985,  140}, {1916, 1569},
        {7280, 4899}, {7509, 3239}, {  10, 2676}, {6807, 2993}, {5185, 3258}, {3023, 1942}
    };

    private static final double OPTIMAL_TOUR = 33524.0;

    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random();

        // for mimic we use a sort encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanSortEvaluationFunction(points);
        int[] ranges = new int[N];
        Arrays.fill(ranges, N);
        DiscreteUniformDistribution odd = new  DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        double bestTourLength = Double.MAX_VALUE;
        double averageTourLength = 0.0;
        long totalTrainTime = 0;
        for (int t = 0; t < N_TEST; t++) {
            MIMIC mimic = new MIMIC(200, 100, pop);
            FixedIterationTrainer fit = new FixedIterationTrainer(mimic, 5000);

            long trainTime = System.currentTimeMillis();
            fit.train();
            totalTrainTime += (System.currentTimeMillis() - trainTime);

            double tourLength = 1/ef.value(mimic.getOptimal());
            averageTourLength += tourLength;
            if (tourLength < bestTourLength) {
                bestTourLength = tourLength;
            }
        }
        averageTourLength /= N_TEST;
        System.out.println("Avg Elapsed Time: " + totalTrainTime);
        System.out.println("Average Tour: " + averageTourLength);
        System.out.println("Best Tour: " + bestTourLength);
        System.out.println("Error: " + (bestTourLength - OPTIMAL_TOUR));
    }
}
