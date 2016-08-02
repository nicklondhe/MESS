/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distance;

import Utils.FileRead;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author professor87
 */
public class SentAlign implements Distance {

	HashMap<String, HashMap<String, Double>> corruptionProbability = null;

    
    public SentAlign (String pathToFile) {
    	corruptionProbability = new HashMap<String, HashMap<String, Double>>();
    	populateCorruptionProbability(pathToFile);
    }
    

     private void populateCorruptionProbability(String pathToFile) {
        try {
            File file = new File(pathToFile);
            FileRead f = new FileRead();
            ArrayList<String> lines = f.fileReadAsArrayList(file);
            Iterator iter = lines.iterator();
            while (iter.hasNext()) {
                String line = iter.next().toString();
                String tokens[] = line.split("->");
                String key = tokens[0].trim();
                String innerTokens[] = tokens[1].split("=");
                String innerKey = innerTokens[0].trim();
                Double innervalue = Double.parseDouble(innerTokens[1].trim());
                if (corruptionProbability.containsKey(key)) {
                    HashMap<String, Double> value = corruptionProbability.get(key);
                    value.put(innerKey, innervalue);
                } else {
                    HashMap<String, Double> value = new HashMap<String, Double>();
                    value.put(innerKey, innervalue);
                    corruptionProbability.put(key, value);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SentAlign.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public double getDistance(Object object1, Object object2)
    {
        String s1 = (String) object1;
        String s2 = (String) object2;   
       
        double distance=10;
        String str1Toks[] = s1.split("_");
        String str2Toks[] = s2.split("_");
        int totalLengthS1 = str1Toks.length;
        int totalLengthS2 = str2Toks.length;
        double finalError = 0;

            for(int i = 0;i<totalLengthS1;i++)
            {

                String ss1 = "";
                String ss2 = "";
                if(i<totalLengthS1) {
                    ss1 = str1Toks[i];
                }
               if(i<totalLengthS2) {
                    ss2 = str2Toks[i];
                }
                HashMap<String,Double>innerHashMap = corruptionProbability.get(ss1);
                if(innerHashMap != null && innerHashMap.containsKey(ss2)){
                Double corruption = innerHashMap.get(ss2);
//                corruption = 1-corruption;
                finalError = finalError+corruption;//the corruption probabilities are not independent

                }
                else
                {
                    
                   Double corruption = 0.0;
                   finalError+=corruption;
                }
            
         
        }
           if(totalLengthS1>totalLengthS2) {
                finalError = finalError/totalLengthS1;
            }
            else {
                finalError = finalError/totalLengthS2;
               }
//        System.out.println(s1+"\t"+s2+"\t"+distance);
        distance = finalError;        
        return distance;
    }
    
    @Override
	public String getName() {
		return "SentAlign";
	}
}
