/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpb.business;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author kuuhaku
 */
public class RandomChoiceCreator {

    public static List<String> getAlternatives(int correctMethodId) {
        Set<String> collectedMethods = new HashSet<>();
        ClassLoader classLoader = new RandomChoiceCreator().getClass().getClassLoader();
        String path = classLoader.getResource("repo.json").getFile();
        JsonObject object;
        try {
            object = (JsonObject) new JsonParser().parse(new FileReader(path));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        JsonArray method_types = object.getAsJsonArray("method_types");
        randomCollectMethod(collectedMethods, method_types.size(), correctMethodId, method_types);
        while(collectedMethods.size() < 6){
            randomCollectMethod(collectedMethods, 7-method_types.size(), 0, method_types);
            System.out.println("travei");
        }
        return collectedMethods.stream().collect(Collectors.toList());
    }

    private static void randomCollectMethod(Set<String> collectedMethods, int numberOfChoices, int correctMethodId, JsonArray methodTypesJson) {
        if (numberOfChoices > methodTypesJson.size()) {
            throw new RuntimeException("number of choices is greater than methodsType array");
        }
        Set<Integer> visitedArrayIds = new HashSet<>();
        visitedArrayIds.add(0);
        Random random = new Random();
        JsonObject tempObj;
        JsonArray tempArray;
        int count = 0;
        while (!visitedArrayIds.contains(correctMethodId) && count < numberOfChoices) {
            int nextInt = random.nextInt(methodTypesJson.size());
            boolean added = visitedArrayIds.add(nextInt);
            if(!added)
                continue;
            tempObj = methodTypesJson.get(nextInt).getAsJsonObject();
            tempArray = tempObj.getAsJsonArray("methods");
            collectedMethods.add(tempObj.get("method_type")+":"+tempArray.get(random.nextInt(tempArray.size())).getAsString());
            count++;
        }
    }
}