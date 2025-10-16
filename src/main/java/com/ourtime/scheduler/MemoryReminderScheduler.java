package com.ourtime.scheduler;

import com.ourtime.domain.Memory;
import com.ourtime.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemoryReminderScheduler {

    private final MemoryRepository memoryRepository;

    /**
     * 매일 오전 9시에 실행 - 1년 전 오늘의 추억 알림
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendMemoryReminders() {
        log.info("추억 리마인더 스케줄러 시작");

        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int yearAgo = now.getYear() - 1;

        // 1년 전 오늘의 추억 조회
        List<Memory> memories = memoryRepository.findMemoriesByDate(month, day, yearAgo);

        if (memories.isEmpty()) {
            log.info("1년 전 오늘의 추억이 없습니다.");
            return;
        }

        log.info("1년 전 오늘의 추억 {}개 발견", memories.size());

        // 실제 알림 전송 로직 (이메일, 푸시 알림 등)
        for (Memory memory : memories) {
            sendReminder(memory);
        }

        log.info("추억 리마인더 스케줄러 종료");
    }

    private void sendReminder(Memory memory) {
        // TODO: 실제 알림 전송 로직 구현
        // 이메일, 푸시 알림, 웹소켓 등을 통해 그룹 멤버들에게 알림 전송
        log.info("리마인더 전송: memory={}, group={}, user={}, title={}", 
                memory.getId(), 
                memory.getGroup().getId(), 
                memory.getUser().getId(), 
                memory.getTitle());
    }

    /**
     * 매일 자정에 실행 - 통계 정보 업데이트 (선택적)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStatistics() {
        log.info("통계 정보 업데이트 스케줄러 시작");
        // TODO: 통계 정보 업데이트 로직 구현
        log.info("통계 정보 업데이트 스케줄러 종료");
    }

}
