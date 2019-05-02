package com.domatron

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/* Author: Dominic Triano
 * Date: 4/24/2019
 * Language: Kotlin
 * Project: TagGame-Backend
 * Description:
 * Database for the server
 *
 */
object Players : Table(){
    var user: Column<String> = varchar("user",50).primaryKey()
    var tid: Column<String> = varchar("tid",50)
    var status: Column<Int> = integer("status")
    var tcount: Column<Int> = integer("tcount")
    var groupId: Column<String> = varchar("groupId",50).primaryKey()
    var macAddrs: Column<String> = varchar("macAddrs",50).primaryKey()
}