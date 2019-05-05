package com.domatron

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/* Author: Dominic Triano
 * Date: 4/29/2019
 * Language: Kotlin
 * Project: TagGame-Backend
 * Description:
 *  Data handling for the database of the server
 *
 *
 */



fun registerPlayer(userId : String, mac : String, grpCode : String)
{
    Database.connect("jdbc:h2:./test;DB_CLOSE_DELAY=-1", "org.h2.Driver")
//    SchemaUtils.create(Players)

    transaction {
        SchemaUtils.create(Players)

        Players.insert {
            it[user] = userId
            it[macAddrs] = mac
            it[groupId] = grpCode
            it[status] = 0
            it[tid] = ""
            it[tcount] = 0
        }
    }
}

fun joinGroup(userId : String, mac : String, grpCode : String)
{
    Database.connect("jdbc:h2:~/Players", "org.h2.Driver")

    transaction {

        //sees if the group is created
        val query: Query = Players.slice(Players.groupId).select { Players.groupId eq grpCode }

        //if the group is existing, then allows the player to join
        if (checkGroup(userId, grpCode).isNotBlank()) {
            registerPlayer(userId, mac, grpCode)
        } else {
            //TODO -- Send to user, "Unexisting Group, Please try again"
        }
    }
}

fun checkGroup(userId : String, grpCode : String) : String
{
    Database.connect("jdbc:h2:./test;DB_CLOSE_DELAY=-1", "org.h2.Driver")

    var grp : String = ""

    transaction {
        val query: Query = Players.slice(Players.groupId).select { Players.groupId eq grpCode }
        grp = query.first()[Players.groupId]
    }

    return grp
}

fun regGroup(userId : String, mac : String, grpCode : String)
{
    Database.connect("jdbc:h2:./test;DB_CLOSE_DELAY=-1", "org.h2.Driver")
//    SchemaUtils.create(Players)


    transaction {
        SchemaUtils.create(Players)

        registerPlayer(userId, mac, grpCode)
        //sees if the group is created
        //val query: Query = Players.slice(Players.groupId).select { Players.groupId eq grpCode }

        //if the group is existing, then the player must make a new group ID
//        if (checkGroup(userId, grpCode).isBlank()) {
//            registerPlayer(userId, mac, grpCode)
//        } else {
//            //TODO -- Send to user, "Existing Group, Please try again"
//        }
    }
}

fun tag(tagN : String, userId : String)
{
    Database.connect("jdbc:h2:~/Players", "org.h2.Driver")

    transaction {
        //Set player who tagged the player's status to not 'it'
        Players.update({ Players.user eq userId }) {
            it[Players.status] = 0
        }

        //Set players status who was tagged to 'it'
        Players.update({ Players.tid eq tagN }) {
            it[Players.status] = 1
        }
    }
    //TODO -- Also increment the tcount by one of the player who got tagged
}

fun checkStatus(userId : String) : String
{
    Database.connect("jdbc:h2:~/Players", "org.h2.Driver")
    var stats : String = "5"

    transaction {
        //Checks to see if the user is 'it'
        val query: Query = Players.slice(Players.status).select { Players.user eq userId }.withDistinct()
        stats = query.first()[Players.status].toString()
    }
    
    println(stats)
    return stats
}

fun checkUser(mac : String) : String
{
    Database.connect("jdbc:h2:./test", "org.h2.Driver")
    var userN : String = ""

    transaction {
        val query: Query = Players.slice(Players.user).select { Players.macAddrs eq mac }.withDistinct()
        userN = query.first()[Players.user]
    }

     println(userN)
    return userN
}

fun tagGet(mac : String) : String
{
    Database.connect("jdbc:h2:~/Players", "org.h2.Driver")
    var tag : String = ""

    transaction {
        val query: Query = Players.slice(Players.tid).select { Players.macAddrs eq mac }.withDistinct()
        tag = query.first()[Players.tid]
    }

    return tag
}

fun tagSet(mac : String, tagN : String)
{
    Database.connect("jdbc:h2:~/Players", "org.h2.Driver")

    transaction {
        Players.update({ Players.macAddrs eq mac }) {
            it[Players.tid] = tagN
        }
    }
}