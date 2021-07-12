import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class payments {
    /*public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "C:/Users/rangarev792/spark-2.4.0-bin-hadoop2.7");
        String logFile = "C:/Users/rangarev792/README.md"; // Should be some file on your system
        SparkSession spark = SparkSession.builder().appName("Payments").config("spark.master", "local").getOrCreate();
        Dataset<String> logData = spark.read().textFile(logFile).cache();


        long numAs = logData.filter(s -> s.contains("a")).count();
        long numBs = logData.filter(s -> s.contains("b")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

        spark.stop();
    }*/
}
