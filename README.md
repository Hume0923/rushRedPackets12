# rushRedPackets12

開發環境IntelliJ IDEA 
編譯用命令行 gradle build
File->setting->Build,Execution,Deployment->Annotation Processors
勾選Enable annotation processing

1.初始化紅包，console會顯示每個紅包金額
http://localhost:8080/red_packet/init/{包數量}/{總金額}
範例：http://localhost:8080/red_packet/init/10/10000

2.顧客端搶紅包，在console會有sessionId與他搶到的紅包金額
http://localhost:8080/gen_client/init/{客戶端連線人數}
範例：http://localhost:8080/gen_client/init/100

3.查看系統時間，同時連線搶紅包有哪些人
http://localhost:8080/red_packet/get