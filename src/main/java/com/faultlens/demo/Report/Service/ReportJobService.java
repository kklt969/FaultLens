package com.faultlens.demo.Report.Service;

import com.faultlens.demo.Report.Exception.ReportQueueFullException;
import com.faultlens.demo.Report.Model.JobSubmitResponse;
import com.faultlens.demo.Report.Model.ReportJob;
import com.faultlens.demo.Report.Model.ReportJobStatus;
import com.faultlens.demo.Report.Model.ResilienceReport;
import jakarta.annotation.PostConstruct;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ReportJobService {

    private final BlockingQueue<ReportJob> queue =
            new ArrayBlockingQueue<>(5);

    private final ConcurrentMap<UUID, ReportJob> jobs =
            new ConcurrentHashMap<>();

    private final ResilienceReportService reportService;


    private static final Logger log =
            LoggerFactory.getLogger(ReportJobService.class);

    public ReportJobService(ResilienceReportService reportService) {
        this.reportService = reportService;

    }

    public ReportJob submitJob(
            Integer firstExperimentId,
            Integer secondExperimentId) {

        ReportJob job = new ReportJob();

        job.setId(UUID.randomUUID());
        job.setStatus(ReportJobStatus.QUEUED);
        job.setFirstExperimentId(firstExperimentId);
        job.setSecondExperimentId(secondExperimentId);

        jobs.put(job.getId(), job);



        if (!queue.offer(job)) {
            jobs.remove(job.getId());
            throw new ReportQueueFullException(
                    "Report queue is full. Try again later."
            );
        }

        return job;
    }


    @PostConstruct // start this method when the service is initilized
    public void startWorker() throws InterruptedException {


        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                ReportJob job = null;
                try {
                   job = queue.take();

                    job.setStatus(ReportJobStatus.RUNNING);

                    ResilienceReport report = reportService.generateReport(
                            job.getFirstExperimentId(),
                            job.getSecondExperimentId()
                    );

                    job.setReport(report);
                    job.setStatus(ReportJobStatus.COMPLETED);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;

                } catch (Exception e) {
                    // Mark the current job as FAILED.
                    if(job != null) {
                        job.setStatus(ReportJobStatus.FAILED);
                        job.setErrorMessage(e.getMessage());
                    }
                    log.error("Report job failed: {}", job != null ? job.getId() : "unknown",e);

                }
            }
        });

        worker.setName("faultlens-report-worker");
        worker.setDaemon(true);
        worker.start();


    }



    public ReportJob getJob(UUID id) {
        return jobs.get(id);
    }

}
