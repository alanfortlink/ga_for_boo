import java.io.File

/**
 * Created by alanfortlink on 5/20/17.
 */

class BPP {

    constructor() {
        this.weights = mutableListOf()
    }

    var size: Int = 0
    var binCapacity: Int = 0
    var weights: List<Int>

    fun loadFromFile(filename: String){
        val file = File(filename)
        val lines = file.readLines()

        this.size = lines[0].toInt()
        this.binCapacity = lines[1].toInt()
        this.weights = lines.subList(2, lines.size).map { value -> value.toInt() };

//        println(this.weights)
    }
}