package com.georgiev.spamfilter.interfaces;

import com.georgiev.spamfilter.model.MessageModel;
import java.util.List;

/**
 *
 * @author bzzzt
 */
public interface PreProcessor {

    public MessageModel process(MessageModel model);

}
