package de.lygie.batch;

import javax.batch.api.chunk.ItemProcessor;

public class LargeFile2DBProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        return item;
    }
}
