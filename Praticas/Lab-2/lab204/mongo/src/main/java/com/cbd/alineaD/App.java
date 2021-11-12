package com.cbd.alineaD;

import static com.mongodb.client.model.Filters.eq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

        String uri = "mongodb://localhost";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            //Numero de localidades distintas
            System.out.println("Numero de localidades distintas:");
            System.out.println(countLocalidades());

            System.out.println();

            //Numero de restaurantespor localidade
            System.out.println("Numero de restaurantespor localidade:");
            
            Map<String, Integer> result = countRestByLocalidade();

            for (String key : result.keySet()) {
                System.out.println(key + " - " + result.get(key) + " restaurants");
            }

            System.out.println();

            //Nome de restaurantes contendo 'Park' no nome:
            System.out.println("Nome de restaurantes contendo 'Park' no nome:");

            List<String> restNames = getRestWithNameCloserTo("Park");

            for(String restName : restNames){
                System.out.println(restName);
            }
        }
    }

    public static int countLocalidades(){
        int count = 0;
        
        DistinctIterable<String> documents = collection.distinct("localidade", String.class);
        for (String document:documents){
            count++;
        }

        return count;
    }

    public static Map<String, Integer> countRestByLocalidade(){
            Map<String, Integer> result = new HashMap<>();

            AggregateIterable<Document> doc2 = collection
                    .aggregate(Arrays.asList(group("$localidade", Accumulators.sum("sum", 1))));
            for (Document document : doc2) {
                result.put(document.get("_id").toString(), (int) (document.get("sum")));
            }

            return result;
    }

    public static List<String> getRestWithNameCloserTo(String name){
        List<String> result = new ArrayList<String>();
        FindIterable<Document> doc3 = collection.find(regex("nome", String.format("(%s)", name))); 
        
        for (Document doc : doc3) {
            result.add((String) doc.get("nome"));
        }

        return result;
    }
}
