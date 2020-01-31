package uk.cbradbury.backend.controllers;

import uk.cbradbury.backend.entities.Report;
import uk.cbradbury.backend.enumerations.Column;
import uk.cbradbury.backend.enumerations.ReportType;
import uk.cbradbury.backend.services.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin(maxAge = 3600)
@RequestMapping("/api")
public class StatsRestController {

  private final ReportService reportService;

  public StatsRestController(ReportService reportService) {
    this.reportService = reportService;
  }

  @RequestMapping(path = "/reports")
  @CrossOrigin
  public @ResponseBody
  Report fetchReport(@RequestParam ReportType reportType, @RequestParam(required = false) List<Column> groupBy) {
    return reportService.fetchReport(reportType, groupBy);
  }
}