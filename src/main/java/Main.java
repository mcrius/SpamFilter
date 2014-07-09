
import com.georgiev.spamfilter.NaiveBayes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        NaiveBayes naiveBayes = new NaiveBayes();
        naiveBayes.train(new File("D:\\text mining\\spam"), new File("D:\\text mining\\ham"), new File("D:\\text mining\\stopwords.txt"));
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
