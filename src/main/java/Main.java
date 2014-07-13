
import com.georgiev.spamfilter.ClassifierData;
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

    public static void main(String[] args) throws Exception{
//        NaiveBayes naiveBayes = new NaiveBayes();
//        naiveBayes.train(new File("D:\\text mining\\spam"), new File("D:\\text mining\\ham"), new File("D:\\text mining\\stopwords.txt"));
//        System.exit(1);
        ClassifierData data = SpamFilterUtils.readDataFromFile(new File("data.dat"));
        NaiveBayes naiveBayes = new NaiveBayes(data);
//        naiveBayes.test();
    }
//    public static void main(String[] args) throws FileNotFoundException, IOException {
//        File f = new File("D:\\text mining\\SPAMTrain.txt");
//        BufferedReader br = new BufferedReader(new FileReader(f));
//        String line = br.readLine();
//        File newFile;
//        while (line != null) {
//            String[] split = line.split(" ");
//            File file = new File("D:\\text mining\\TRAINING\\".concat(split[1].trim()));
//            if (split[0].trim().equals("0")) {
//                newFile = new File("D:\\text mining\\ham\\".concat(split[1].trim()));
//            } else {
//                newFile = new File("D:\\text mining\\spam\\".concat(split[1].trim()));
//            }
//            if (!newFile.exists()) {
//                newFile.createNewFile();
//            }
//            Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            line = br.readLine();
//        }
//    }
}
