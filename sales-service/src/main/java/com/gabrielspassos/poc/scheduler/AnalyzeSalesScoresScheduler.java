package com.gabrielspassos.poc.scheduler;

import com.gabrielspassos.poc.service.SaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class AnalyzeSalesScoresScheduler {

    private final SaleService saleService;

    @Scheduled(fixedDelayString = "${sales.scheduler.analyze-sales-scores-milliseconds-time}")
    @SchedulerLock(
            name = "AnalyzeSalesScores",
            lockAtMostFor = "${sales.scheduler.analyze-sales-scores-milliseconds-time}",
            lockAtLeastFor = "${sales.scheduler.analyze-sales-scores-milliseconds-time}"
    )
    public void analyzeSalesScores() {
        LockAssert.assertLocked();
        log.info("Realizando expiração de assembleias");
        saleService.analyzeSalesScores()
                .doOnComplete(() -> log.info("Analisado scores das vendas as assembleias"))
                .subscribe();
    }

}
