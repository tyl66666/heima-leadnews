package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerQuickStart {
    public static void main(String[] args) {
        //1 kafka链接配置信息
        Properties prop = new Properties();
        //kafka链接地址
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        // key和value的序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //重试
        prop.put(ProducerConfig.RETRIES_CONFIG,10);
        //压缩
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"gzip");
        //2 创建kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);

        //3 发送消息
        ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<>("topic-first", "key-001", "hello kafka");
        producer.send(kvProducerRecord);

        //4 关闭消息通道  必须关闭 不然消息发送失败
        producer.close();
    }
}
