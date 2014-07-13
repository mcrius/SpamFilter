package com.georgiev.spamfilter;

import com.georgiev.spamfilter.interfaces.PreProcessor;
import com.georgiev.spamfilter.model.MessageModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bzzzt
 */
public class StopWordPreProcessor implements PreProcessor {

    private final File file;
    private List<String> stopWords;

    public StopWordPreProcessor(File file) {
        this.file = file;
    }

    public StopWordPreProcessor(List<String> words) {
        this.stopWords = words;
        this.file = null;
    }

    @Override
    public MessageModel process(MessageModel model) {
        if (stopWords == null || stopWords.isEmpty()) {
            stopWords = new ArrayList<>();
            try {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line = br.readLine();
                    while (line != null) {
                        stopWords.add(line.trim());
                        line = br.readLine();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(StopWordPreProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String body = model.getBody();
        if (body != null) {
            body = body.replaceAll("[^a-zA-Z ]", "").toLowerCase();
            for (String sw : stopWords) {
                body = body.replaceAll(sw, "");
            }
        }
        model.setBody(body);

        return model;
    }

}
