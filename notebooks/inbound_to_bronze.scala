// Databricks notebook source
// MAGIC %md
// MAGIC
// MAGIC ### Conferindo se os dados foram montados e se temos acesso a pasta inbound

// COMMAND ----------

// MAGIC %python
// MAGIC
// MAGIC display(dbutils.fs.ls("/mnt/dados/inbound"))

// COMMAND ----------

// MAGIC %md
// MAGIC
// MAGIC ### Lendos os dados na camada de inbound

// COMMAND ----------

val path = "/mnt/dados/inbound/dados_brutos_imoveis.json"
val dados = spark.read.json(path)

// COMMAND ----------

display(dados)

// COMMAND ----------

// MAGIC %md
// MAGIC
// MAGIC ### Removendo colunas

// COMMAND ----------

val dados_anuncio = dados.drop("imagens","usuario")
display(dados_anuncio)

// COMMAND ----------

// MAGIC %md 
// MAGIC
// MAGIC ### Criando uma coluna de identificação

// COMMAND ----------

import org.apache.spark.sql.functions.col

// COMMAND ----------

val df_bronze = dados_anuncio.withColumn("id", col("anuncio.id"))
display(df_bronze)

// COMMAND ----------

// MAGIC %md
// MAGIC
// MAGIC ### Salvando na camada bronze

// COMMAND ----------

val path = "dbfs:/mnt/dados/bronze/dataset_imoveis"
df_bronze.write.format("delta").mode(SaveMode.Overwrite).save(path)
