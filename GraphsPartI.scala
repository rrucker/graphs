/**~/workspace/GraphFrames project   2020
 * THis is part of an assignment of graphframes
 * for week Asg13.2 of Big D class
 * NOTE: This url below is where the example is, I did not use databricks'
 * to run this program, I used IntelliJ, The code below is using IntelliJ
 *
 *
 * **NOTE: I GOT THE EXAMPLE from the Databricks, I coded it in IntelliJ
 * Below is a url of the graphframes api tutorial
 * https://docs.databricks.com/spark/latest/
 *    graph-analysis/graphframes/user-guide-scala.html //*** I did not code this in Databricks
 *                          but used IntelliJ instead
 *
 *    NOTE: Databricks has a "display" method, use show() instead
 *    graph.inDegree.show() versus display(graph.inDegree)
 *    2020-04-11/ 2020-11-10 rr
 */

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.graphframes._
// I didn't use plotly in the  code below *( was just in case of a follow on :-)
import plotly._
import element._
import layout._
import Plotly._

//import scala.io.Source
object GraphsPartI extends App{
  Logger.getLogger("org").setLevel(Level.OFF)
  val spark = SparkSession
    .builder()
    .appName("GraphFrame Part I ")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._
  import org.graphframes._
  println(s"Spark version  ${spark.version}")
// very small Sample just for entry learning
val vertexsA = Seq(
  ("a","Alice",34),
  ("b","Bob",68),
  ("c","Charlie",55)
).toDF("id", "name", "age")
 // vertexs.show(false)
val edgesA = Seq(
  ("a","b","friend"),
  ("b","c","follow"),
  ("c","b","follow")
).toDF("src","dst","relationship")
  //edges.show(false)

  val graphA = GraphFrame(vertexsA, edgesA)
//graphA.inDegrees.show()
  //graphA.edges.filter("relationship='follow'").count()
val fnVertexs= "/Users/rob/DeskTop/IFTBigData/GraphFrameVertexsAPI24.csv"
  val fnEdges= "/Users/rob/DeskTop/IFTBigData/GraphFrameEdgesAPI24.csv"
  val mySchemaVertex = StructType(Array(
    StructField("id", StringType, false),
    StructField("name",StringType, false),
    StructField("age", IntegerType, false)
  ))
  val mySchemaEdge = StructType(Array(
    StructField("src", StringType, false),
    StructField("dst",StringType, false),
    StructField("relationship", StringType, false)
  ))
  //val rawVertexs = scala.io.Source.fromFile(fn).getLines.toList
  val vertexs = spark.read
    .format("csv")
    .schema(mySchemaVertex)
    .option("sep", ",")
    .option("inferSchema", "false")
    .option("header", "false")
    .load(fnVertexs)
  vertexs.show()
  val edges = spark.read
    .format("csv")
    .schema(mySchemaEdge)
    .option("sep", ",")
    .option("inferSchema", "false")
    .option("header", "false")
    .load(fnEdges)
  edges.show()
  //rawVertexs.select("name", "age").write.format("parquet").save("namesAndAges.parquet")

  val graph =GraphFrame(vertexs, edges)
   graph.inDegrees.show()
   graph.outDegrees.show()
  graph.pageRank.resetProbability(0.01).maxIter(20).run()
  // Query: Count the number of "follow" connections in the graph.
  graph.vertices.filter($"age" > 25).show()
  graph.vertices.groupBy().min("age").show()
//  **Subgraphs
 // val v2 = graph.vertices.groupBy().min("age")
  val v2 = graph.vertices.filter($"age" >=30)
  val e2 = graph.edges.filter("relationship = 'friend'")
  val g2 = GraphFrame(v2, e2)
  g2.vertices.show()
  g2.edges.show()
//  Motifs
  println(s"  motif (x)-[e]->(y); (y)-[e2]->(z); (z)-[e3]->(w); (w) -[e4]->(s)")
  val paths = graph.find("(x)-[e]->(y); (y)-[e2]->(z); (z)-[e3]->(w); (w) -[e4]->(s)")
    //.filter("e.relationship = 'friend'")
     //.filter("x.age < y.age")
paths.show()
  /*
  // Search for pairs of vertices with edges in both directions between them.
  val paths2 = graph.find("(a)-[e]->(b); (b)-[e2]->(a)")
  println(s" find (a)-[e]->(b); (b)-[e2]->(a) ")
  paths2.show()

// More complex queries can be expressed by applying filters.
  paths2.filter("b.age > 30").show()

  val subpaths = g2.find("(x) -[e]-> (y) ; (y)-[e2]->(z)")
subpaths.show()
*/

}// end  object GraphsPartI
