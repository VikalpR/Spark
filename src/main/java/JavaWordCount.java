import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public final class JavaWordCount {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "C:/Users/rangarev792/spark-2.4.0-bin-hadoop2.7");

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .config("spark.master", "local")
                .getOrCreate();
       // runJdbcDatasetExample(spark);

        String file = "C:/Users/rangarev792/README.md"; // Should be some file on your system

        JavaRDD<String> lines = spark.read().textFile(file).javaRDD();

        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?,?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }
        spark.stop();
    }

    private static void runJdbcDatasetExample(SparkSession spark) {
        // $example on:jdbc_dataset$
        // Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
        // Loading data from a JDBC source
        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:ukdc1-a1-sbx.worldpaytd.local:1521/Cdb5_rmb_app.worldpaytd.local")
                .option("dbtable", "CISADM.CI_ACCT")
                .option("user", "CISADM")
                .option("password", "CISADM")
                .load();

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "CISADM");
        connectionProperties.put("password", "CISADM");
        Dataset<Row> jdbcDF2 = spark.read()
                .jdbc("jdbc:ukdc1-a1-sbx.worldpaytd.local:1521/Cdb5_rmb_app.worldpaytd.local", "CISADM.CI_ACCT", connectionProperties);

        // Saving data to a JDBC source
        jdbcDF.write()
                .format("jdbc")
                .option("url", "jdbc:postgresql:dbserver")
                .option("dbtable", "schema.tablename")
                .option("user", "username")
                .option("password", "password")
                .save();

        jdbcDF2.write()
                .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);

        // Specifying create table column data types on write
        jdbcDF.write()
                .option("createTableColumnTypes", "name CHAR(64), comments VARCHAR(1024)")
                .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);
        // $example off:jdbc_dataset$
    }
}