package com.georgiev.spamfilter;

import com.georgiev.spamfilter.model.MessageModel;
import com.georgiev.spamfilter.util.SpamFilterUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class NaiveBayes {

    private ClassifierData data;

    public NaiveBayes(ClassifierData data) {
        this.data = data;
    }

    public NaiveBayes() {
        this.data = null;
    }

    public void train(File spamFolder, File hamFolder, File stopWords) throws MessagingException, FileNotFoundException, IOException {
        long spamCount = 0;
        long hamCount = 0;
        HashMap<String, Long> hamMap = new HashMap<>();
        HashMap<String, Long> spamMap = new HashMap<>();
        if (!spamFolder.isDirectory() && !hamFolder.isDirectory()) {
            throw new IllegalArgumentException("Files must point to a folder");
        } else {
            StopWordPreProcessor swpp = new StopWordPreProcessor(stopWords);
            File[] spamFiles = spamFolder.listFiles();
            File[] hamFiles = hamFolder.listFiles();
            for (File file : hamFiles) {
                Message m = new MimeMessage(null, new FileInputStream(file));
                MessageModel mm = SpamFilterUtils.getModelFromMimeMessage(m);
                if (mm != null) {
                    mm = swpp.process(mm);
                    if (mm.getTitle() != null) {
                        String[] words = mm.getTitle().split("\\s+");
                        for (String w : words) {
                            if (hamMap.containsKey(w)) {
                                hamMap.replace(w, hamMap.get(w) + 1);
                            } else {
                                hamMap.put(w, 1L);
                            }
                        }
                        hamCount++;
                    }
                }

            }
            for (File file : spamFiles) {
                Message m = new MimeMessage(null, new FileInputStream(file));
                MessageModel mm = SpamFilterUtils.getModelFromMimeMessage(m);
                if (mm != null) {
                    mm = swpp.process(mm);
                    if (mm.getTitle() != null) {
                        String[] words = mm.getTitle().split("\\s+");
                        for (String w : words) {
                            if (spamMap.containsKey(w)) {
                                spamMap.replace(w, spamMap.get(w) + 1);
                            } else {
                                spamMap.put(w, 1L);
                            }
                        }
                        spamCount++;
                    }
                }

            }
        }
        this.data = new ClassifierData(hamMap, spamMap, spamCount, hamCount);
        System.out.println(hamCount);
        System.out.println(spamCount);
    }

}
