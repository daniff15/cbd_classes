package com.cbd.alineaABC;

import static com.mongodb.client.model.Filters.eq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Aggregates.group;

public class App {

    static MongoCollection<Document> collection;

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string

        alineaa();

        alineab();

        alineac();

    }

    static void alineaa() {
        System.out.println("ALINEA A:");
        String uri = "mongodb://localhost";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");
            Document doc1 = collection.find(eq("localidade", "Brooklyn")).first();
            System.out.println("PROCURA DE INFO");
            System.out.println(doc1.toJson());
            System.out.println("INSERIR INFO");
            System.out.println("Find dessa info->");
            Document doc2 = new Document().append("localidade", "Pesqueira").append("nome", "Douros")
                    .append("restaurant_id", "40000011");
            collection.insertOne(doc2);
            System.out.println(collection.find(eq("localidade", "Pesqueira")).first());
            System.out.println("UPDATE INFO");
            System.out.println("Find dessa info updated->");
            collection.updateOne(Filters.eq("localidade", "Pesqueira"), Updates.set("nome", "Pizzaria"));
            System.out.println(collection.find(eq("localidade", "Pesqueira")).first());

        }
    }

    static void alineab() {
        System.out.println("ALINEA B:");
        String uri = "mongodb://localhost";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            System.out.println("WITHOUT INDEXES!");

            System.out.println("Searching all american restaurants located in Brooklyn");
            long start = System.nanoTime();
            FindIterable<Document> doc = collection
                    .find(and(eq("gastronomia", "American"), eq("localidade", "Brooklyn")));
            long end = System.nanoTime();
            long elapsedTime = end - start;
            double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
            System.out.println("Search time: " + elapsedTimeInSecond + " seconds");

            System.out.println("Searching all restaurants started with Pi");
            start = System.nanoTime();
            doc = collection.find(regex("nome", "^Pi"));
            end = System.nanoTime();
            elapsedTime = end - start;
            elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
            System.out.println("Search time: " + elapsedTimeInSecond + " seconds");

            System.out.println("WITH INDEXES!");
            // Create indexes;
            createIndex("gastronomia");
            createIndex("localidade");
            createIndexByText("nome");

            System.out.println("Searching all american restaurants located in Brooklyn");
            start = System.nanoTime();
            doc = collection.find(and(eq("gastronomia", "American"), eq("localidade", "Brooklyn")));
            end = System.nanoTime();
            elapsedTime = end - start;
            elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
            System.out.println("Search time: " + elapsedTimeInSecond + " seconds");

            System.out.println("Searching all restaurants started with Pi");
            start = System.nanoTime();
            doc = collection.find(regex("nome", "^Pi"));
            end = System.nanoTime();
            elapsedTime = end - start;
            elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
            System.out.println("Search time: " + elapsedTimeInSecond + " seconds");

        }
    }

    public static void createIndex(String idx) {
        try {
            collection.createIndex(Indexes.ascending(idx));
        } catch (Exception e) {
            System.err.println("Error creating ascending index on " + idx + "field in MongoDB collection: " + e);
        }
    }

    public static void createIndexByText(String textIdx) {
        try {
            collection.createIndex(Indexes.text((textIdx)));
        } catch (Exception e) {
            System.err.println("Error creating text index on " + textIdx + "field in MongoDB collection: " + e);
        }
    }

    static void alineac() {
        System.out.println("ALINEA C:");
        String uri = "mongodb://localhost";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            // How many restaurants in Bronx
            System.out.println("How many restaurants in Bronx?");
            FindIterable<Document> doc = collection.find(eq("localidade", "Bronx"));
            int count = 0;
            for (Document document : doc) {
                count++;
            }
            System.out.println("Number of restaurants in Bronx: " + count);

            System.out.println();
            // Count restaurants per localidade
            System.out.println("Restaurants per localidade:");
            Map<String, Integer> result = new HashMap<>();

            AggregateIterable<Document> doc2 = collection
                    .aggregate(Arrays.asList(group("$localidade", Accumulators.sum("sum", 1))));
            for (Document document : doc2) {
                result.put(document.get("_id").toString(), (int) (document.get("sum")));
            }

            for (String key : result.keySet()) {
                System.out.println(key + " - " + result.get(key) + " restaurants");
            }

            System.out.println();

            //Liste o nome, a localidadee gastronomiados restaurantes cujo nome começam por "Wil".
            System.out.println("Liste o nome, a localidadee gastronomiados restaurantes cujo nome começam por \"Wil\".");
            doc = collection.find(regex("nome", "^Wil")).projection(Projections.include("nome" , "localidade", "gastronomia"));
            for (Document document : doc) {
                document.remove("_id");
                System.out.println(document);
            }

            System.out.println();

            //Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo "American" ou "Chinese".
            System.out.println("Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo \"American\" ou \"Chinese\".");

            doc = collection.find(and(eq("localidade", "Bronx") , or(eq("gastronomia", "American"), eq("gastronomia", "Chinese")))).projection(Projections.include("nome" , "localidade", "gastronomia"));
            for (Document document : doc) {
                document.remove("_id");
                System.out.println(document);
            }

            System.out.println();
            //Apresente o número de gastronomias diferentes na rua "Fifth Avenue"
            System.out.println("Apresente o número de gastronomias diferentes na rua \"Fifth Avenue\"");

            DistinctIterable<String> documents = collection.distinct("gastronomia", eq("address.rua", "Fifth Avenue"), String.class);

            count = 0;
            for (String document : documents) {
                count++;
            }
            System.out.println("Existem " + count + " gastronomias diferentes na rua \"Fifth Avenue\"");

        }
    }
}
