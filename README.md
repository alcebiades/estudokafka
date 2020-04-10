Para este projeto de estudo foi utilizado o ZooKeeper 3.6.0 e o Kafka 2.4.1 com a versao do scala 2.13

O windows tem um problema de nao suportar path de arquivos muito grande, entao extraia o kafka e o zookeeper em uma pasta dentra da pasta C:\ ou D:\ etc... Como no exemplo abaixo
C:\kafka\...
No linux e MAC esse problema nao existe, porem caso haja espaco no path ate o executavel pode haver falha na inicializacao do processo, entao certifique-se que nao haja espaco no path do diretorio.

Execute primeiro o zookeeper e depois o kafka:

Antes de iniciar o zookeeper precisa configurar o arquivo de configuracao, para isso, va na pasta ".\conf" e altere o nome do arquivo "zoo_sample.cfg" para "zoo.cfg", esse arquivo salva as configuracoes do zookeeper.

Observacao: caso vc inicie o zookeeper com as configuracoes padrao, quando vc deslicar a maquina as suas configuracoes serao perdidas, para corrigir isso altere o arquivo "zoo.cfg", no campo "dataDir=/tmp/zookeeper" altere para um diretorio na sua maquina.
Faca o mesmo para o arquivo ".\config\server.properties" do kafka, porem o campo a ser alterado no arquivo do kafka e o "log.dirs=/tmp/kafka-logs".

Iniciando o Zookeeper no windows, linux e MAC:
.\bin\zkServer.cmd

Iniciando o kafka no windows:
.\bin\windows\kafka-server-start.bat .\config\server.properties

Iniciando o kafka no linux e MAC:
.\bin\kafka-server-start.bat .\config\server.properties

Criando topico:
.\bin\windows\kafka-topics.bat --bootstrap-server 127.0.0.1:9092 --create --replication-factor 1 --partitions 1 --topic TOPIC_SALE
.\bin\windows\kafka-topics.bat --bootstrap-server 127.0.0.1:9092 --create --replication-factor 1 --partitions 1 --topic TOPIC_EMAIL

Listando os topicos:
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list

Lendo as mensagens de um topico desde o inicio
.\bin\windows\kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic TOPIC_SALE --from-beginning
.\bin\windows\kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic TOPIC_EMAIL --from-beginning

Descrevendo um topico:
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --describe

Alterando o topico, adicionando mais particoes:
.\bin\windows\kafka-topics.bat --alter --zookeeper localhost:2181 --topic TOPIC_SALE --partitions 3

Descrevendo os grupos de consumo:
.\bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --all-groups



