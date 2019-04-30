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
    var user: Column<String> = varchar("",50).primaryKey()
    var tid: Column<String> = varchar("",50)
    var status: Column<Int> = integer("0")
    var tcount: Column<Int> = integer("0")
    var groupId: Column<String> = varchar("",50).primaryKey()
    var macAddrs: Column<String> = varchar("",50).primaryKey()
}