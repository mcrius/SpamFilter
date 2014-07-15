/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.georgiev.test.smapfilter;

import com.georgiev.spamfilter.ClassifierData;
import com.georgiev.spamfilter.ClassifierType;
import com.georgiev.spamfilter.NaiveBayes;
import com.georgiev.spamfilter.Result;
import com.georgiev.spamfilter.StopWordPreProcessor;
import com.georgiev.spamfilter.interfaces.PreProcessor;
import com.georgiev.spamfilter.model.MessageModel;
import com.georgiev.spamfilter.util.SpamFilterUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author bzzzt
 */
public class NaiveBayesTest {

    public NaiveBayesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void test() throws MessagingException, FileNotFoundException, IOException {
        try {
            File testFolder = new File("D:\\text mining\\test");
            File stopWordsFile = new File("D:\\text mining\\stopwords.txt");
            ClassifierData data = SpamFilterUtils.readDataFromFile(new File("data.dat"));
            PreProcessor swpp = new StopWordPreProcessor(stopWordsFile);
            List<PreProcessor> list = new ArrayList<>();
            list.add(swpp);
            NaiveBayes naiveBayes = new NaiveBayes(data, ClassifierType.NEVER_TRUST, list);
            int correct = 0;
            File[] files = testFolder.listFiles();
//            StopWordPreProcessor swpp = new StopWordPreProcessor(stopWordsFile);
            for (File file : files) {
                Message m = new MimeMessage(null, new FileInputStream(file));
                MessageModel mm = SpamFilterUtils.getModelFromMimeMessage(m);
                if (mm != null) {
                    mm = swpp.process(mm);
                    Result result = naiveBayes.classify(mm);
                    if (result.equals(getResultForFile(file))) {
                        correct++;
                    }
                }
            }
            System.out.println("Precision: " + ((double) correct / (double) files.length) * 100 + "%");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NaiveBayesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Result getResultForFile(File f) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\text mining\\SPAMTrain.txt")));
        String line = br.readLine();
        while (line != null) {
            String[] split = line.split(" ");
            if (split[1].trim().equalsIgnoreCase(f.getName())) {
//                System.out.println("File :" + f.getName() + "Class : " + split[0]);
                if (split[0].trim().equals("0")) {
                    return Result.HAM;
                } else {
                    return Result.SPAM;
                }

            }
            line = br.readLine();
        }
        return null;
    }
}
