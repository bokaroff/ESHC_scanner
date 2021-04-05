package com.example.eshc_scanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items_table")
data class Items(
  @PrimaryKey(autoGenerate = true) var entity_id: Int = 0,
  var item_id: String = "",
  var objectName: String = "",
  var objectPhone: String = "",
  var mobilePhone: String = "",
  var kurator: String = "",
  var worker08: String = "",
  var address: String = "",
  var worker15: String = "",
  var img: String = "",
  var serverTimeStamp: String = "",
  var itemLongTime: Long = 0,
  var order00: String = "",
  var order02: String = "",
  var order04: String = "",
  var order06: String = "",
  var order08: String = "",
  var order15: String = "",
  var order21: String = "",
  var state: String = "main",
  var savedToRoom: String = "false"
) : Serializable