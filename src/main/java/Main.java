
import com.georgiev.spamfilter.ClassifierData;
import com.georgiev.spamfilter.ClassifierType;
import com.georgiev.spamfilter.NaiveBayes;
import com.georgiev.spamfilter.util.SpamFilterUtils;
import java.io.File;

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
        NaiveBayes naiveBayes = new NaiveBayes(data, ClassifierType.NEVER_TRUST);
    }
}
