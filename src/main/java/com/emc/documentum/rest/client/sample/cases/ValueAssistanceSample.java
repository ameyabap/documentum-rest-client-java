/*
 * Copyright (c) 2016. EMC Corporation. All Rights Reserved.
 */
package com.emc.documentum.rest.client.sample.cases;

import java.util.Map;

import com.emc.documentum.rest.client.sample.client.annotation.RestServiceSample;
import com.emc.documentum.rest.client.sample.client.annotation.RestServiceVersion;
import com.emc.documentum.rest.client.sample.model.RestType;
import com.emc.documentum.rest.client.sample.model.ValueAssistant;
import com.emc.documentum.rest.client.sample.model.plain.PlainValueAssistantRequest;

import static com.emc.documentum.rest.client.sample.client.util.Debug.printNewLine;
import static com.emc.documentum.rest.client.sample.client.util.Debug.printStep;
import static com.emc.documentum.rest.client.sample.client.util.Reader.read;

@RestServiceSample("Value Assistance")
@RestServiceVersion(7.3)
public class ValueAssistanceSample extends Sample {
    public void valueAssistance() {
        printStep("get type value assistance of the fixed list");
        String typeWithFixedVAList = read("Please input the type name with the fixed value assistance list(no such type press 'return' directly to skip):", "");
        if(!typeWithFixedVAList.isEmpty()) {
            String attrWithFixedVAList = read("Please input the attribute name of " + typeWithFixedVAList + " with the fixed assistance list:");
            RestType fixedVAListType = client.getType(typeWithFixedVAList);
            ValueAssistant va = client.getValueAssistant(fixedVAListType, new PlainValueAssistantRequest(), "included-properties", attrWithFixedVAList);
            for(ValueAssistant.Attribute a : va.getProperties()) {
                System.out.println(a.name() + " " + (a.allowUserValues() ? "allows user values" : "disallows user values"));
                for(ValueAssistant.Value v : a.values()) {
                    System.out.println(v.value() + ", label=" + v.label());
                }
            }
        }
        printNewLine();

        printStep("get type value assistance depencencies");
        String typeWithDenpendency = read("Please input the type name with value assistance dependencies(no such type press 'return' directly to skip):", "");
        if(!typeWithDenpendency.isEmpty()) {
            RestType dependenciesVAType = client.getType(typeWithDenpendency);
            System.out.println(dependenciesVAType.getName() + " " + dependenciesVAType.getCategory());
            for(Map<String, Object> map : dependenciesVAType.getProperties()) {
                System.out.println(map.get("name") + ": " + map.get("dependencies"));
            }
            
            printStep("get type value assistance query values with depencencies");
            String attrWithDependencies = read("Please input the attribute name of " + typeWithDenpendency + " with the query dependencies:");
            String dependency = read("Please input the dependency name of " + typeWithDenpendency + "." + attrWithDependencies + ":");
            String dependencyValue = read("Please input the dependency value of " + dependency + ":");
            ValueAssistant va = client.getValueAssistant(dependenciesVAType, new PlainValueAssistantRequest(dependency, dependencyValue), "included-properties", attrWithDependencies);
            for(ValueAssistant.Attribute a : va.getProperties()) {
                System.out.println(a.name() + " " + (a.allowUserValues() ? "allows user values" : "disallows user values"));
                for(ValueAssistant.Value v : a.values()) {
                    System.out.println(v.value() + ", label=" + v.label());
                }
            }
        }
        printNewLine();
    }
}
