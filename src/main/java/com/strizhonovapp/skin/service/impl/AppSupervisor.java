package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.jsonhelper.InvalidSkinVolumeException;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.AppRunner;
import com.strizhonovapp.skin.service.CoreService;
import com.strizhonovapp.skin.service.MarketItemsHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppSupervisor implements AppRunner {

    private final CoreService coreService;
    private final MarketItemsHolder marketItemsHolder;

    @Value("${misc.item-info-life-time-hours}")
    private int itemsInfoLifeTimeHours;

    @Override
    public void run() {
        while (true) {
            reloadItemsIfNecessary();
            Skin next = marketItemsHolder.nextSkin();
            try {
                coreService.processListingsOf(next);
            } catch (InvalidSkinVolumeException e) {
                logInvalidSkinVolumeException(e);
            } catch (Exception e) {
                logException(e);
            }
        }
    }

    private void reloadItemsIfNecessary() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime itemsInitializationTime = marketItemsHolder.getItemsInitializationTime();
        if (isTimeToReload(now, itemsInitializationTime)) {
            marketItemsHolder.reload();
        }
    }

    private boolean isTimeToReload(LocalDateTime now, LocalDateTime itemsInitializationTime) {
        return now.minus(itemsInfoLifeTimeHours, ChronoUnit.HOURS)
                .isAfter(itemsInitializationTime);
    }

    private void logInvalidSkinVolumeException(InvalidSkinVolumeException e) {
        log.warn(e.getMessage());
    }

    private void logException(Exception e) {
        log.error(e.getMessage());
        String stackTrace = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        log.error(stackTrace);
    }

}
