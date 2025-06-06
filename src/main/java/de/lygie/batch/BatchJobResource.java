package de.lygie.batch;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("batch")
public class BatchJobResource {

    @GET
    @Path("start")
    @Produces(MediaType.TEXT_PLAIN)
    public String startJob() {
        // Ruft den JobOperator ab und startet den Batch-Job (Jobname entspricht dem in der Job-Definition)
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("import-job", null);
        return "Batch-Job gestartet mit execution id: " + executionId;
    }

    @GET
    @Path("startJob2")
    @Produces(MediaType.TEXT_PLAIN)
    public String startJob2() {

        Properties jobParams = new Properties();
        jobParams.setProperty("chunkSize", "100");

        // Ruft den JobOperator ab und startet den Batch-Job (Jobname entspricht dem in der Job-Definition)
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("import-job", jobParams);
        return "Batch-Job gestartet mit execution id: " + executionId;
    }

    @GET
    @Path("startJob3")
    @Produces(MediaType.TEXT_PLAIN)
    public String startJob3() {

        Properties jobParams = new Properties();
        jobParams.setProperty("chunkSize", "100");

        // Ruft den JobOperator ab und startet den Batch-Job (Jobname entspricht dem in der Job-Definition)
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("import-job3", jobParams);
        return "Batch-Job gestartet mit execution id: " + executionId;
    }

    @GET
    @Path("blobImport")
    @Produces(MediaType.TEXT_PLAIN)
    public String blobImport() {


        // Ruft den JobOperator ab und startet den Batch-Job (Jobname entspricht dem in der Job-Definition)
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("file2blob", null);
        return "Batch-Job gestartet mit execution id: " + executionId;
    }
}