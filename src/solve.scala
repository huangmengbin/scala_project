/**
  * 历史代码
  * */
object solve {

  import scala.collection.mutable
  def main(hmb666:Array[String]): Unit ={
    val hashMap = new mutable.HashMap[String, Int]
    read("test.txt").foreach(s=> hashMap.put(s, hashMap.getOrElse(s,0) + 1) )

    val wordCountArray: Array[(String, Int)] = hashMap.toArray.sortBy(-_._2)
    write(wordCountArray, "result.txt")
  }

  import scala.io.Source
  def read(filePlace:String):Array[String] = {
    val stringBuilder = new StringBuilder
    Source.fromFile(filePlace, "UTF-8").foreach(stringBuilder.append)
    stringBuilder.toString().toLowerCase().split("\\s+")
  }

  import java.io.{File, PrintWriter}
  def write(wordCountArray:Array[(String, Int)], filePlace:String):Unit={
    val printer = new PrintWriter(new File(filePlace))
      wordCountArray.foreach(t=>{
        println(t)
        printer.write(t.toString()+'\n')
      })
    printer.close()
  }
}