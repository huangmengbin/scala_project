//
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.rdd.RDD
//object mainOb {
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setMaster("local[4]").setAppName("wordcount")
//    val context: SparkContext = new SparkContext(conf)
//    val dataRDD: RDD[String] = context.textFile("test.txt")
//    val wordRDD: RDD[String] = dataRDD flatMap (_.split(" "))
//    val wordAdd: RDD[(String, Int)] = wordRDD.map((_,1))
//    val resultRDD: RDD[(String, Int)] = wordAdd.reduceByKey(_+_)
//    val sortResultRDD: RDD[(String, Int)] = resultRDD.sortBy(_._2)
//    val finalData: Array[(String, Int)] = sortResultRDD.collect()
//    finalData foreach println
//    context stop()
//  }
//}