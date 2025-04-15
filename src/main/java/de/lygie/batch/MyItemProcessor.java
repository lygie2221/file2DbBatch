package de.lygie.batch;


import javax.batch.api.chunk.ItemProcessor;

public class MyItemProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        // Verarbeitet das Item (hier: einfache Multiplikation)
        if (item instanceof Integer) {
            return ((Integer) item) * 2;
        }
        return item;
    }
}