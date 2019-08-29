package com.auction.app.scheduled;

import com.auction.app.services.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UpdateBidsTask {
    private static final Logger logger = LoggerFactory.getLogger(UpdateBidsTask.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final int updateRate = 60 * 1000;

    private final BidService bidService;

    public UpdateBidsTask(BidService bidService) {
        this.bidService = bidService;
    }

    @Scheduled(fixedRate = updateRate)
    public void scheduleTaskWithFixedRate() {
        Integer number = bidService.updateAllBids();
        logger.info("Fixed Rate Task :: Execution Time - {}, updated Bids:: - {}", dateTimeFormatter.format(LocalDateTime.now()), number);
    }
}
