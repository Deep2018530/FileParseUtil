package com.deepz.fileparse;

import javax.mail.MessagingException;
import java.io.IOException;

public interface FileParse<T> {
    default T parse(String path) throws IOException, MessagingException {

        return null;
    }

}
