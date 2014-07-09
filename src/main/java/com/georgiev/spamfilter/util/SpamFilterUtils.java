package com.georgiev.spamfilter.util;

import com.georgiev.spamfilter.ClassifierData;
import com.georgiev.spamfilter.model.MessageModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author bzzzt
 */
public class SpamFilterUtils {

    public static MessageModel getModelFromMimeMessage(Part p) {
        try {
            MimeMessage mm = (MimeMessage) p;
            MessageModel model = new MessageModel();
            List<String> from = new ArrayList<>(mm.getFrom().length);
            for (Address f : mm.getFrom()) {
                from.add(f.toString());
            }
            model.setFrom(from);
            model.setTitle(mm.getSubject());
            model.setBody(getText(p));
            return model;
        } catch (MessagingException ex) {
//            Logger.getLogger(SpamFilterUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public static MessageModel
    public static ClassifierData readDataFromFile(File file) throws IOException, ClassNotFoundException {
        ClassifierData data;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            data = (ClassifierData) ois.readObject();
        }
        return data;
    }

    public static void readDataFromFile(File file, ClassifierData data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        }
    }

    /**
     * Return the primary text content of the message.
     */
    private static String getText(Part p) {
        try {
            boolean textIsPlain = false;
            if (p.isMimeType("text/*")) {
                try {
                    String s = (String) p.getContent();
                    textIsPlain = p.isMimeType("text/plain");
                    return s;
                } catch (ClassCastException e) {
                    return null;
                }

            }

            if (p.isMimeType("multipart/alternative")) {
                // prefer html text over plain text
                Multipart mp = (Multipart) p.getContent();
                String text = null;
                for (int i = 0; i < mp.getCount(); i++) {
                    Part bp = mp.getBodyPart(i);
                    if (bp.isMimeType("text/plain")) {
                        if (text == null) {
                            text = getText(bp);
                        }
                        return text;
                    } else if (bp.isMimeType("text/html")) {
                        String s = getText(bp);
                        if (s != null) {
                            return s;
                        }
                    } else {
                        return getText(bp);
                    }
                }
                return text;
            } else if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    String s = getText(mp.getBodyPart(i));
                    if (s != null) {
                        return s;
                    }
                }
            }

        } catch (MessagingException | IOException ex) {

        }
        return null;
    }
}
