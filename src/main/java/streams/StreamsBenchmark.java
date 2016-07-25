package streams;

import data.Pair;
import org.openjdk.jmh.annotations.*;
import solutions.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static data.Generator.generatePairs;

@Fork(5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 20, time = 1, timeUnit = TimeUnit.MINUTES)
//@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.MINUTES)
@State(Scope.Thread)
public class StreamsBenchmark {

    @Param({"1", "10", "100", "1000", "10000", "100000", "10000000"})
    public int pairsCount;

    @Param({"0.1", "1", "10", "100", "1000", "10000"})
    public double pairsPerUniqueId;

    public List<Pair> pairs;

    @Setup
    public void setup() {
        final int idCount = Math.max(1, (int) (pairsCount / pairsPerUniqueId));

        pairs = generatePairs(idCount, pairsCount);
    }

    @Benchmark
    public Result classic() {
        return Classic.solve(this.pairs);
    }

    @Benchmark
    public Result trice() {
        return Trice.solve(this.pairs, true);
    }

    @Benchmark
    public Result trice_seq() {
        return Trice.solve(this.pairs, false);
    }

    @Benchmark
    public Result mapPair() {
        return MapPairSolution.solve(this.pairs, true);
    }

    @Benchmark
    public Result mapPair_seq() {
        return MapPairSolution.solve(this.pairs, false);
    }

    @Benchmark
    public Result singleIteration() {
        return SingleIteration.solve(this.pairs, true);
    }

    @Benchmark
    public Result singleIteration_seq() {
        return SingleIteration.solve(this.pairs, false);
    }

}
