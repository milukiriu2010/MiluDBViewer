package db;

//import java.util.Map;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.KsDef;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

// http://d.hatena.ne.jp/akishin999/20100430/1272642657
// https://gist.github.com/amigoavena/4092068#file-keyspaces-java-L43
public class CassandraExample {
    public static void main(String[] args) {
        try {

            // Thrift を使用して Cassandra に接続
            TTransport port = new TSocket("localhost", 9160);
            TProtocol protocol = new TBinaryProtocol(port);
            Cassandra.Client client = new Cassandra.Client(protocol);
            port.open();

            // クラスタ名の表示
            System.out.printf("Cluster name : [%s]\n", client.describe_cluster_name());
 
            // バージョンの表示
            System.out.printf("Version : [%s]\n", client.describe_version());
            
            // 接続中クラスタの全キースペースを取得
            //for (String keyspace : client.describe_keyspaces()) {
            for (KsDef keyspace : client.describe_keyspaces()) {
                // キースペース名の表示
                System.out.printf("Keyspace : [%s]\n", keyspace);
                /*
                // キースペース内の全 Column Family の取得
                //for (Map.Entry<String, Map<String, String>> entry : client.describe_keyspace(keyspace).entrySet()) {
                for (Map.Entry<String, Map<String, String>> entry : client.describe_keyspace(keyspace).entrySet()) {
                    System.out.printf("\tColumn Family : [%s]\n", entry.getKey());
                    for (Map.Entry<String, String> innerEntry : entry.getValue().entrySet()) {
                        System.out.printf("\t\t[%s]:[%s]\n", innerEntry.getKey(), innerEntry.getValue());
                    }
                }
                */
            }

            port.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }                
}
