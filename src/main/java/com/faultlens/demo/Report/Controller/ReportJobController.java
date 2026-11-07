package com.faultlens.demo.Report.Controller;

import com.faultlens.demo.Report.Model.CreateReportJobRequest;
import com.faultlens.demo.Report.Model.JobSubmitResponse;
import com.faultlens.demo.Report.Model.ReportJob;
import com.faultlens.demo.Report.Model.ReportJobStatus;
import com.faultlens.demo.Report.Service.ReportJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/experiments/reports")
public class ReportJobController {
    private final ReportJobService reportJobService;

    public ReportJobController(ReportJobService reportJobService) {
        this.reportJobService = reportJobService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ReportJob> submitReport(
            @RequestBody CreateReportJobRequest request) {

        ReportJob job = reportJobService.submitJob(
                request.getFirstExperimentId(),
                request.getSecondExperimentId()
        );

        return ResponseEntity.accepted().body(job);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ReportJob> getReportJob(@PathVariable UUID jobId) {
        ReportJob job = reportJobService.getJob(jobId);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(job);
    }





}


