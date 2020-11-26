import java.io.{File, PrintWriter}
import scala.io.Source
/**
  * 历史代码
  * */
object teachersAnswer {
  def main(args: Array[String]): Unit = {
    var linesList :List[String] = readFileContent("test.txt")
    var wc1 = linesList.flatMap(_.split(" ")).map(_.toLowerCase()).map((_,0)).groupBy(_._1).map(t=>(t._1,t._2.size)).toList.sortBy(_._2).reverse;
    print(wc1)
    write2File("result.txt",wc1)
  }

  /**
    * save wordcount result to file
    *
    * */
  def write2File(file:String,data :List[(String,Int)]):Unit = {
    var fw = new PrintWriter(new File(file));
    for(x <- data){
      fw.write(x._1 + "\t" + x._2)
      fw.write("\n")
    }
    fw.close()
  }

  /**
    * read raw file to do wordcount from local
    *
    * */
  def readFileContent(file :String) :List[String] = {
    var src = Source.fromFile(file,"UTF-8");
    var lines = src.getLines().toList;
    return lines
  }
}
