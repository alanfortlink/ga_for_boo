import java.util.*

/**
 * Created by alanfortlink on 5/20/17.
 */

class Chromosome{
    var size: Int = 0
    var problem: BPP = BPP()
    var items: MutableList<Int> = mutableListOf()
    var chromosomeFitness: Int = -1

    constructor(size: Int, problem: BPP, items: MutableList<Int>){
        this.size = size
        this.problem = problem
        this.items = items
    }

    fun getFitness(algIsNextFit: Boolean): Int{

        if(chromosomeFitness != -1){
            return chromosomeFitness;
        }

        if(algIsNextFit){
            var fitness = 0
            var space = 0

            for(item in items){
                val weight = problem.weights[item]

                if(weight <= space){
                    space -= weight
                }else{
                    fitness += 1
                    space = problem.binCapacity - weight
                }
            }

            chromosomeFitness = fitness
            return fitness
        }else{
            var spaces = mutableListOf<Int>(problem.binCapacity)

            for(i in items){
                val weight = problem.weights[i];

                var done = false;

                for(j in 0..spaces.size-1){
                    if (spaces[j] >= weight){
                        spaces[j] -= weight;

                        done = true;
                        break;
                    }
                }

                if(!done){
                    spaces.add(problem.binCapacity - weight)
                }
            }

            chromosomeFitness = spaces.size

            return spaces.size;
        }


    }
}
