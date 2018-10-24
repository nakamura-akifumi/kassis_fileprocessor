# kassis_fileprocessor
kassis シリーズ用ファイル処理サービス

## 概要

Office系ファイル(xlsx,odp,docxなど)やPDFなどを処理し、データのインポートや全文検索(Elasticsearch)に投入するサービスです。
他の kassis サービスから RabbitMQ 経由で処理メッセージを受け取り、処理を実行します。

## 環境構築方法

Linux系OSかMacOSで動くはずです。

## 動作方法



## メモ

- kotlin 1.2
- SpringBoot 2.0
- Java 8
- Redis
- RabbitMQ 3.7
- Apache POI
- PostgreSQL 9
- Elasticsearch 6
- kassis soda 0.1?

## ライセンス

MIT
