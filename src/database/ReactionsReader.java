/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import chart.ChartBuilder;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandre
 */
public class ReactionsReader {

    String postId;

    public ReactionsReader(String postId) {
        this.postId = postId;
    }

    public LinkedHashMap<String, Integer> getReactionsTotal() {

        LinkedHashMap<String, Integer> reactionsCount = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("./reaction_monitoring/" + this.postId + "_reactions_monitoring.txt"))) {
            String lastLine = null;
            String currentLine;
            br.readLine();

            while ((currentLine = br.readLine()) != null) {

                if (!currentLine.equals("\n")) {
                    lastLine = currentLine;
                }
            }

            String[] items = lastLine.split(",");

            String timeStamp = items[0].split(" ")[1];

            reactionsCount.put("loves", Integer.parseInt(items[1]));
            reactionsCount.put("wow", Integer.parseInt(items[2]));
            reactionsCount.put("haha", Integer.parseInt(items[3]));
            reactionsCount.put("likes", Integer.parseInt(items[4]));
            reactionsCount.put("sad", Integer.parseInt(items[5]));
            reactionsCount.put("angry", Integer.parseInt(items[6]));
            reactionsCount.put("shares", Integer.parseInt(items[7]));

        } catch (FileNotFoundException ex) {
            Logger.getAnonymousLogger();
        } catch (IOException ex) {
            Logger.getAnonymousLogger();
        }

        return reactionsCount;
    }

    public LinkedHashMap<String, LinkedHashMap> getAllReactionsCount() {

        try (BufferedReader br = new BufferedReader(new FileReader("./reaction_monitoring/" + this.postId + "_reactions_monitoring.txt"))) {
            String line;
            br.readLine();

            LinkedHashMap<String, LinkedHashMap> allReactions = new LinkedHashMap<>();

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] items = line.split(",");

                String timeStamp = items[0].split(" ")[1];

                LinkedHashMap<String, Float> reactions = new LinkedHashMap<>();

                reactions.put("loves", Float.parseFloat(items[1]));
                reactions.put("wow", Float.parseFloat(items[2]));
                reactions.put("haha", Float.parseFloat(items[3]));
                reactions.put("likes", Float.parseFloat(items[4]));
                reactions.put("sad", Float.parseFloat(items[5]));
                reactions.put("angry", Float.parseFloat(items[6]));
                reactions.put("shares", Float.parseFloat(items[7]));

                allReactions.put(timeStamp, reactions);
            }

            return allReactions;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChartBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChartBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

}

//    public static void main(String args[]){
//    
////    435464776514810_1380362052025073_reactions_monitoring.txt
//
//        ReactionsReader r = new ReactionsReader("435464776514810_1380362052025073");
//        
//        r.getReactionsTotal();
//    
//    }
//    
//}
