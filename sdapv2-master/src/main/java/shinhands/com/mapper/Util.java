package shinhands.com.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    Logger logger = LoggerFactory.getLogger(Util.class);
    public void process(Person person){
        //TODO processing code
        logger.info(">> Incoming "+ person.getName());
    }
}
