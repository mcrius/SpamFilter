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
    private final ClassifierType classfierType;

    public NaiveBayes(ClassifierData data, ClassifierType type) {
        this.data = data;
        this.classfierType = type;
    }

    public NaiveBayes(ClassifierType type) {
        this.data = null;
        this.classfierType = type;
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
                            if (hamMap.containsKey(w.trim())) {
                                hamMap.replace(w.trim(), hamMap.get(w.trim()) + 1);
                            } else {
                                hamMap.put(w.trim(), 1L);
                            }
                        }

                    }
                    if (mm.getBody() != null) {
                        String[] words = mm.getBody().split("\\s+");
                        for (String w : words) {
                            if (hamMap.containsKey(w.trim())) {
                                hamMap.replace(w.trim(), hamMap.get(w.trim()) + 1);
                            } else {
                                hamMap.put(w.trim(), 1L);
                            }
                        }
//                        hamCount++;
                    }
                    hamCount++;
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
                            if (spamMap.containsKey(w.trim())) {
                                spamMap.replace(w.trim(), spamMap.get(w.trim()) + 1);
                            } else {
                                spamMap.put(w.trim(), 1L);
                            }
                        }
//                        spamCount++;
                    }
                    if (mm.getBody() != null) {
                        String[] words = mm.getBody().split("\\s+");
                        for (String w : words) {
                            if (spamMap.containsKey(w.trim())) {
                                spamMap.replace(w.trim(), spamMap.get(w.trim()) + 1);
                            } else {
                                spamMap.put(w.trim(), 1L);
                            }
                        }
//                        hamCount++;
                    }
                    spamCount++;
                }

            }
        }
        this.data = new ClassifierData(hamMap, spamMap, spamCount, hamCount);
        System.out.println(hamCount);
        System.out.println(spamCount);
        File f = new File("data.dat");
        SpamFilterUtils.writeDataToFile(f, this.data);
    }

    public Result classify(MessageModel mm) {
        String[] titleWords = null;
        String[] bodyWords = null;
        if (mm.getTitle() != null) {
            titleWords = mm.getTitle().trim().split("\\s+");
        }
        if (mm.getBody() != null) {
            bodyWords = mm.getBody().trim().split("\\s+");
        }
        //ham
        double h = Math.log((double) data.getHamCount() / (double) (data.getHamCount() + data.getSpamCount()));
//        System.out.println("H: " + h);
        long hamWordCount = 0l;
        for (Long l : data.getHamMap().values()) {
            hamWordCount += l;
        }
        if (titleWords != null) {
            for (String w : titleWords) {
                h = h + Math.log(2 * (data.getHamMap().get(w.trim()) != null ? (double) data.getHamMap().get(w.trim()) : 0.000001d) / (double) hamWordCount);
            }
        }
        if (bodyWords != null) {
            for (String bw : bodyWords) {
                h = h + Math.log((data.getHamMap().get(bw.trim()) != null ? (double) data.getHamMap().get(bw.trim()) : 0.000001d) / (double) hamWordCount);
            }
        }

        double p = Math.log((double) data.getSpamCount() / (double) (data.getHamCount() + data.getSpamCount()));
//        System.out.println("P: " + p);
        long spamWordCount = 0l;
        for (Long l : data.getSpamMap().values()) {
            spamWordCount += l;
        }
        if (titleWords != null) {
            for (String w : titleWords) {
                p = p + Math.log(2 * (data.getSpamMap().get(w.trim()) != null ? (double) data.getSpamMap().get(w.trim()) : 0.000001d) / (double) spamWordCount);
            }
        }
        if (bodyWords != null) {
            for (String bw : bodyWords) {
                p = p + Math.log((data.getSpamMap().get(bw.trim()) != null ? (double) data.getSpamMap().get(bw.trim()) : 0.000001d) / (double) spamWordCount);
            }
        }

        Result result = (p > h) ? Result.SPAM : Result.HAM;
        switch (classfierType) {
            case TRUST_HIGH_PROB: {
                double spam = Math.pow(Math.E, p);
                double ham = Math.pow(Math.E, h);
                double spamProb = spam / spam + ham;
                double hamProb = ham / spam + ham;
                if (spamProb > 0.95d && result == Result.SPAM) {
                    data.setSpamMap(SpamFilterUtils.updateMap(data.getSpamMap(), titleWords, bodyWords));
                    data.setSpamCount(data.getSpamCount() + 1);
                } else {
                    if (hamProb > 0.95d && result == Result.HAM) {
                        data.setHamMap(SpamFilterUtils.updateMap(data.getHamMap(), titleWords, bodyWords));
                        data.setHamCount(data.getHamCount() + 1);
                    }
                }
                break;
            }
            case ALWAYS_TRUST: {
                if (result == Result.SPAM) {
                    data.setSpamMap(SpamFilterUtils.updateMap(data.getSpamMap(), titleWords, bodyWords));
                    data.setSpamCount(data.getSpamCount() + 1);
                } else {
                    data.setHamMap(SpamFilterUtils.updateMap(data.getHamMap(), titleWords, bodyWords));
                    data.setHamCount(data.getHamCount() + 1);
                }
                break;
            }
        }
        try {
            SpamFilterUtils.writeDataToFile(new File("data.dat"), data);
        } catch (IOException ex) {/*IGNORE*/

        }
        return result;
    }

}
