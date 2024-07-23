package com.wow.delivery.util;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.CustomException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
@NoArgsConstructor
public class SnowFlakeGenerator {

    private static final int CASE_ONE_BITS = 10;
    private static final int CASE_TWO_BITS = 9;
    private static final int SEQUENCE_BITS = 4;

    private static final int maxSequence = (int) (Math.pow(2, CASE_ONE_BITS) - 1); // 2^10-1

    private static final long CUSTOM_EPOCH = 1420070400000L; // 41bit
    private static int case_one = 10;
    private static int case_two = 0;

    private static class State {
        final long lastTimestamp;
        final long sequence;

        State(long lastTimestamp, long sequence) {
            this.lastTimestamp = lastTimestamp;
            this.sequence = sequence;
        }
    }

    private static AtomicReference<State> state = new AtomicReference<>(new State(-1L, 0L));

    /**
     * 21자리 long타입 기본 SnowFlakeId를 반환
     * @return 21자리 long타입 SnowFlake
     */
    public static long generate() {
        return nextId();
    }

    /**
     * 0~9, a~z 까지 나타낸 36진법으로 변환된 SnowFlakeId를 반환
     * @return 36진법으로 치환된 SnowFlake
     */
    public static String generateBase36String() {
        return Long.toString(nextId(), 36);
    }

    /**
     * 41비트를 맞춰주기 위한 메서드
     * @return 현재 타임스탬프를 커스텀 에포크 시간 기준으로 반환함
     */
    private static long timestamp() {
        return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
    }

    private static long nextId() {
        while (true) {
            long currentTimestamp = timestamp();
            State currentState = state.get();

            // 현재 타임스탬프가 마지막 타임스탬프보다 작다면 시스템 시계 오류로 간주하여 예외를 던짐
            if (currentTimestamp < currentState.lastTimestamp) {
                throw new CustomException(ErrorCode.INVALID_PARAMETER, "유효하지 않는 시스템 시간입니다.");
            }

            long nextSequence = currentState.sequence;

            // 현재 타임스탬프가 마지막 타임스탬프와 같다면 시퀀스를 증가시킴
            if (currentTimestamp == currentState.lastTimestamp) {
                nextSequence = (nextSequence + 1) & maxSequence;
                // 시퀀스가 0이 되면 다음 밀리초로 이동함
                if (nextSequence == 0) {
                    currentTimestamp = waitNextMillis(currentTimestamp);
                }
            } else {
                // 현재 타임스탬프가 마지막 타임스탬프와 다르다면 시퀀스를 0으로 초기화함
                nextSequence = 0;
            }

            State newState = new State(currentTimestamp, nextSequence);

            if (state.compareAndSet(currentState, newState)) {
                return makeId(currentTimestamp, nextSequence);
            }
        }
    }

    // 주어진 타임스탬프를 기반으로 ID를 생성함
    private static Long makeId(long currentTimestamp, long seq) {
        long id = 0;

        // 타임스탬프를 CASE_ONE_BITS + CASE_TWO_BITS + SEQUENCE_BITS 만큼 왼쪽으로 시프트하여 ID에 추가함
        id |= (currentTimestamp << (CASE_ONE_BITS + CASE_TWO_BITS + SEQUENCE_BITS));

        // case_one을 CASE_TWO_BITS + SEQUENCE_BITS 만큼 왼쪽으로 시프트하여 ID에 추가함
        id |= ((long) case_one << (CASE_TWO_BITS + SEQUENCE_BITS));

        // case_two를 SEQUENCE_BITS 만큼 왼쪽으로 시프트하여 ID에 추가함
        id |= ((long) case_two << SEQUENCE_BITS);

        // 시퀀스를 ID에 추가함
        id |= seq;

        return id;
    }

    // 다음 밀리초까지 대기하는 메서드
    private static long waitNextMillis(long currentTimestamp) {
        // 현재 타임스탬프가 마지막 타임스탬프와 같을 동안 루프를 실행함
        while (currentTimestamp == state.get().lastTimestamp) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }
}
