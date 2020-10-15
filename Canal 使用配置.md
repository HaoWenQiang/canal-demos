## Canal 使用配置

建议先看一下官网 https://github.com/alibaba/canal/wiki/QuickStart ，里面说到了mysql 如何配置

###一、组件准备

涉及到

​	管理端(即canal admin)

​	服务端端(即canal developer，server)

​	mysql(用于server的挂载库—库A 和 被监听的库—库B)

​	kafka(由server端将binlog发送到kafka)

​	zookeeper(kafka依赖，canal server的HA)

### 二、组件配置

这里主要说 canal admin 和 canal developer的配置

####admin的配置

<img src="/Users/ouhiroshi/Library/Application Support/typora-user-images/image-20201015155851715.png" alt="image-20201015155851715" style="zoom:50%;" />

这里的server端主要配置项就一个

另外，配置之前要在 库A 中执行conf/canal_manager.sql 这个sql， 创建admin依赖的库

application.yml

```yml
server:
  port: 8089
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

spring.datasource:
  address: localhost:13306
  database: canal_manager
  username: root
  password: root
  driver-class-name: com.mysql.jdbc.Driver
  url: jdbc:mysql://${spring.datasource.address}/${spring.datasource.database}?useUnicode=true&characterEncoding=UTF-8&useSSL=false
  hikari:
    maximum-pool-size: 30
    minimum-idle: 1

canal:
  adminUser: admin
  adminPasswd: admin

```



#### server(developer)配置

![image-20201015161354167](/Users/ouhiroshi/Library/Application Support/typora-user-images/image-20201015161354167.png)





需要配置的有

/canal.deployer-1.1.4/conf/canal.properties

和

/canal.deployer-1.1.4/conf/example/instance.properties

先配置 canal.properties， 以 -- 开头的备注是我后加的

```yml
# register ip
canal.register.ip =

# canal admin config  											---这个是刚才admin的地址
canal.admin.manager = 127.0.0.1:8089
canal.admin.port = 11110
canal.admin.user = admin
canal.admin.passwd = 4ACFE3202A5FF5CF467898FC58AAB1D615029441
# admin auto register
canal.admin.register.auto = true
canal.admin.register.cluster = 

# 可选项: tcp(默认), kafka, RocketMQ					-- 将binlog发送kafka
canal.serverMode = kafka
# ...
# kafka/rocketmq 集群配置: 192.168.1.117:9092,192.168.1.118:9092,192.168.1.119:9092 
canal.mq.servers = 127.0.0.1:9092					
canal.mq.retries = 0
# flagMessage模式下可以调大该值, 但不要超过MQ消息体大小上限
canal.mq.batchSize = 16384
canal.mq.maxRequestSize = 1048576
# flatMessage模式下请将该值改大, 建议50-200
canal.mq.lingerMs = 1
canal.mq.bufferMemory = 33554432
# Canal的batch size, 默认50K, 由于kafka最大消息体限制请勿超过1M(900K以下)
canal.mq.canalBatchSize = 50
# Canal get数据的超时时间, 单位: 毫秒, 空为不限超时
canal.mq.canalGetTimeout = 100
# 是否为flat json格式对象									-- 这里转换为json对象比较容易处理
canal.mq.flatMessage = true
canal.mq.compressionType = none
canal.mq.acks = all
# kafka消息投递是否使用事务
canal.mq.transaction = false
```



继续 example/instance.properties配置

```yml
#################################################
## mysql serverId , v1.0.26+ will autoGen
# canal.instance.mysql.slaveId=0

# enable gtid use true/false
canal.instance.gtidon=false

# position info												-- 监听的mysql地址和端口				
canal.instance.master.address=127.0.0.1:3306
canal.instance.master.journal.name=
canal.instance.master.position=
canal.instance.master.timestamp=
canal.instance.master.gtid=

# rds 阿里云存储服务 oss binlog
canal.instance.rds.accesskey=
canal.instance.rds.secretkey=
canal.instance.rds.instanceId=

# table meta tsdb info
canal.instance.tsdb.enable=true
#canal.instance.tsdb.url=jdbc:mysql://127.0.0.1:3306/canal_tsdb
#canal.instance.tsdb.dbUsername=canal
#canal.instance.tsdb.dbPassword=canal

#canal.instance.standby.address =
#canal.instance.standby.journal.name =
#canal.instance.standby.position =
#canal.instance.standby.timestamp =
#canal.instance.standby.gtid=

# username/password													--被监听库的账号
canal.instance.dbUsername=canal
canal.instance.dbPassword=canal
canal.instance.connectionCharset = UTF-8
# enable druid Decrypt database password
canal.instance.enableDruid=false
#canal.instance.pwdPublicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALK4BUxdDltRRE5/zXpVEVPUgunvscYFtEip3pmLlhrWpacX7y7GCMo2/JM6LeHmiiNdH1FWgGCpUfircSwlWKUCAwEAAQ==

# table regex
canal.instance.filter.regex=.*\\..*
# table black regex
canal.instance.filter.black.regex=
# table field filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)
#canal.instance.filter.field=test1.t_product:id/subject/keywords,test2.t_company:id/name/contact/ch
# table field black filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)
#canal.instance.filter.black.field=test1.t_product:subject/product_image,test2.t_company:id/name/contact/ch

# mq config    																		-- 你的topic 下面也可以指定动态topic
canal.mq.topic=yjmap
# dynamic topic route by schema or table regex
#canal.mq.dynamicTopic=mytest1.user,mytest2\\..*,.*\\..*
canal.mq.partition=0
# hash partition config
#canal.mq.partitionsNum=3
#canal.mq.partitionHash=test.table:id^name,.*\\..* e.g: .*\\..*:$pk$
#################################################

```



主要配置就这些 ok