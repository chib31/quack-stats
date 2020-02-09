package uk.cbradbury.backend.controllers;

import uk.cbradbury.backend.entities.StatResponse;
import uk.cbradbury.backend.enumerations.StatType;
import uk.cbradbury.backend.services.StatsService;
import org.springframework.web.bind.annotation.*;

@RestController()
@CrossOrigin(maxAge = 3600)
@RequestMapping("/api")
public class StatsRestController {

  private final StatsService statsService;

  public StatsRestController(StatsService statsService) {
    this.statsService = statsService;
  }

  @RequestMapping(path = "/stats")
  public @ResponseBody
  StatResponse fetchReport(@RequestParam StatType statType) {
    return statsService.fetchStats(statType);
  }
}