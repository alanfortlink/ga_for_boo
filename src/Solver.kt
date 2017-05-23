import java.util.*

fun nextInt(): Int{
    return (Math.random() * 100000000000).toInt();
}

/**
 * Created by alanfortlink on 5/20/17.
 */
class Solver{

    var problem: BPP = BPP()
    var mutationRate: Double = 1.0
    var population: MutableList<Chromosome> = mutableListOf()
    var populationSize: Int = 0
    var algIsNextFit: Boolean = true

    constructor(problem: BPP, populationSize: Int, mutationRate: Double, algIsNextFit: Boolean){
        this.problem = problem
        this.populationSize = populationSize
        this.mutationRate = mutationRate
        this.algIsNextFit = algIsNextFit
    }

    fun generatePopulation(){
        for(k in 0..(populationSize)) {

            var items = mutableListOf<Int>();

            for (i in 1..problem.size) {
                items.add((i + k) % problem.size);
            }

            var item = Chromosome(problem.size, problem, items);

            this.population.add(item);
        }

        for(k in problem.size..populationSize){
            Collections.shuffle(population[k].items)
        }
   }

    fun generateOffspring(ind: Chromosome) : Chromosome{

        var min:Int = nextInt() % (ind.items.size/2)
        var max:Int = (nextInt() % (ind.items.size/2)) + (ind.items.size/2)

        val subLists: MutableList< MutableList<Int> > = mutableListOf()

        val begin = ind.items.subList(0, min)
        val mid = ind.items.subList(min, max)
        val end = ind.items.subList(max, ind.items.size)

        subLists.add(begin)
        subLists.add(mid)
        subLists.add(end)

        Collections.shuffle(subLists[nextInt() % subLists.size]);

        var offspring = Chromosome(ind.size, ind.problem, mutableListOf());

        offspring.items.addAll(begin);
        offspring.items.addAll(mid);
        offspring.items.addAll(end);


        return offspring;
    }

    fun updatePopulation(){
        var candidates = mutableListOf<Chromosome>();
        candidates.addAll(population);

        var newChromosomes = mutableListOf<Chromosome>()

        for(k in 1..populationSize) {

            val randomIndex = (nextInt() % candidates.size);

            val item = candidates[randomIndex]

            newChromosomes.add(this.generateOffspring(item));

        }

        mutatePopulation(newChromosomes)
        candidates.addAll(newChromosomes)

        candidates = getByTournament(candidates)

        this.population = candidates
    }

    fun getByTournament(items: MutableList<Chromosome>): MutableList<Chromosome>{

        var newItems: MutableList<Chromosome> = mutableListOf()

        while(newItems.size < populationSize){
            var index1 = nextInt() % items.size
            var item1 = items[index1]
            items.removeAt(index1)

            var index2 = nextInt() % items.size
            var item2 = items[index2]
            items.removeAt(index2)

            if (item1.getFitness(algIsNextFit) < item2.getFitness(algIsNextFit)) {
                newItems.add(item1)
            } else {
                newItems.add(item2)
            }
        }

        return newItems
    }

    fun mutatePopulation(items: MutableList<Chromosome>){

        val numberOfMutations = (populationSize * mutationRate).toInt()
        for(i in 1..numberOfMutations){
            val randomIndex = nextInt() % populationSize;
            items[randomIndex] = mutateChromosome(items[randomIndex]);
        }
    }

    fun mutateChromosome(ind: Chromosome): Chromosome{
        val index1 = nextInt() % (ind.items.size);
        val index2 = nextInt() % (ind.items.size);

        val aux: Int = ind.items[index1];
        ind.items[index1] = ind.items[index2]
        ind.items[index2] = aux

        return ind
    }

    fun solve(iterations: Int, timeLimit: Int): Chromosome{

        var initialTime = System.currentTimeMillis()

        generatePopulation()
        var i = 0;
        while(i < iterations){
//            println("Iteration ${i}")
            updatePopulation()

            i += 1

            var elapsedTime = System.currentTimeMillis() - initialTime

            if(elapsedTime > timeLimit){
                break
            }
        }

        var bestSolution = population[0]

        for(item in population.subList(1, populationSize)){
            if(item.getFitness(algIsNextFit) < bestSolution.getFitness(algIsNextFit)){
                bestSolution = item;
            }
        }

//        println(bestSolution.items)
        println("${bestSolution.getFitness(algIsNextFit)} ${(System.currentTimeMillis() - initialTime).toDouble() / 1000}")
//        println("Iterations: ${i-1}")
//        println(bestSolution.items.map { value -> problem.weights[value] })
        return bestSolution
    }
}

fun main(args: Array<String>) {

    val i = args[0]
    val c = args[1].toInt()
    var m = args[2].toDouble()
    var a = args[3].toBoolean()


    var problem = BPP()
    problem.loadFromFile(i)

    var solver = Solver(problem, c * problem.size, m/problem.size, a)

    var result = solver.solve(1000000, 10 * 60 * 1000)

//    println("result found: ${result.getFitness(a)}")

//    for(i in 1..8){
//        println("+++++++++++++++++++++++++++++++++++++++++ Instance ${i} ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
//        for(j in 1..5){
//            println("---------------------------------------------------------------------------------------------------------------------")
//            var problem = BPP()
//            problem.loadFromFile("/Users/alanfortlink/IdeaProjects/POAtividade7/bpp_instances/instance${i}.bpp")
//
//            var solver = Solver(problem, Math.log(problem.size.toDouble()).toInt() * problem.size, 1.0/problem.size)
//
//            var result = solver.solve(1000000, 10 * 60 * 1000)
//
//            println("result found: ${result.getFitness()}")
//        }
//    }
}
