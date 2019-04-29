package com.domatron

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*

/* Author: Dominic Triano
 * Date: 4/22/2019
 * Language: Kotlin
 * Project: TagGame-Backend
 * Description:
 * Backend server for TagGame to process the data it receives from participants
 *
 */

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val db = Database.connect("jdbc:sqlite:/Players.db", "org.sqlite.JDBC")

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("user-id")
        header("tag-id")
        header("mac")
        header("group-id")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
    runBlocking {
        // Sample for making a HTTP Client request
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */
    }

    routing {
//        get("/") {
//            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
//        }

        get("/check-status")
        {
            val userId : String = call.request.header("user-id") ?: "0"

            try {
                call.respondText("${checkStatus(userId)}")
            }catch(e: java.lang.IllegalStateException)
            {
                call.respond(HttpStatusCode(69, "AYYY LMAO"), "Does Not Exist")
            }

        }

        get("/tag")
        {
            val tagN : String = call.request.header("tag-id") ?: "0"
            val userId : String = call.request.header("user-id") ?: "0"

            tag(tagN, userId)
        }

        //If a player moves phones, this will change the macAddress so that they don't have to use a password
//        get("/update-mac")
//        {
//            val userId = call.request.header("user-id")
//            val mac = call.request.header("mac")
//
//        }

        get("/reg-grp")
        {
            val userId : String = call.request.header("user-id") ?: "0"
            val mac : String = call.request.header("mac") ?: "0"
            val grpCode : String = call.request.header("group-id") ?: "0"

            regGroup(userId, mac, grpCode)
        }

        get("/join-grp")
        {
            val userId : String = call.request.header("user-id") ?: "0"
            val mac : String = call.request.header("mac") ?: "0"
            val grpCode : String = call.request.header("group-id") ?: "0"

            joinGroup(userId, mac, grpCode)
        }

        get("/leader")
        {
            //TODO -- Send back leading players in their group
        }

        get("/register")
        {
            val userId : String = call.request.header("user-id") ?: "0"
            val mac : String = call.request.header("mac") ?: "0"
            val grpCode : String = call.request.header("group-id") ?: "0"

            registerPlayer(userId, mac, grpCode)
            
            call.respondText("User ID: $userId Registered", contentType = ContentType.Text.Plain)
        }

    }

}

data class JsonSampleClass(val hello: String)
