
import com.georgiev.spamfilter.ClassifierData;
import com.georgiev.spamfilter.ClassifierType;
import com.georgiev.spamfilter.NaiveBayes;
import com.georgiev.spamfilter.StopWordPreProcessor;
import com.georgiev.spamfilter.interfaces.PreProcessor;
import com.georgiev.spamfilter.util.SpamFilterUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bzzzt
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ClassifierData data = SpamFilterUtils.readDataFromFile(new File("data.dat"));
        PreProcessor swpp = new StopWordPreProcessor(new File("stopwords.txt"));
        List<PreProcessor> list = new ArrayList<>();
        list.add(swpp);
        NaiveBayes naiveBayes = new NaiveBayes(data, ClassifierType.NEVER_TRUST, list);
    }
}
