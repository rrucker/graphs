name := "GraphFrames - rr"
version := "0.1"
scalaVersion := "2.12.10"
resolvers += "graphframes repo" at "https://dl.bintray.com/spark-packages/maven/"
resolvers += "Typesafe Releases" at
  "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "log4j" % "log4j" % "1.2.17"
//libraryDependencies += "com.couchbase.client" %% "scala-client" % "1.0.2"
libraryDependencies += "graphframes" % "graphframes" % "0.8.0-spark3.0-s_2.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.5"

libraryDependencies += "org.plotly-scala" %% "plotly-core" % "0.7.3"
libraryDependencies += "org.plotly-scala" %% "plotly-render" % "0.7.3"