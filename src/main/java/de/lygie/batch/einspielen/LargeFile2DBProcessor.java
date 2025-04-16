package de.lygie.batch.einspielen;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named
@Dependent
public class LargeFile2DBProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        return item;
    }
}
