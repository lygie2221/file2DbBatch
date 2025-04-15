package de.lygie.batch;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("batch")
public class BatchJobResource {

    @GET
    @Path("start")
    @Produces(MediaType.TEXT_PLAIN)
    public String startJob() {
        // Ruft den JobOperator ab und startet den Batch-Job (Jobname entspricht dem in der Job-Definition)
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("chunkJob", null);
        return "Batch-Job gestartet mit execution id: " + executionId;
    }
}