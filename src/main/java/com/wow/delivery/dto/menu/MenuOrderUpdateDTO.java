package com.wow.delivery.dto.menu;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.InvalidParameterException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MenuOrderUpdateDTO {

    @NotNull
    private Long menuCategoryId; // 수정하는 메뉴 카테고리 ID

    @NotNull
    private List<Long> beforeMenuIds; // 기존의 메뉴 순서

    @NotNull
    private List<Long> afterMenuIds; // 수정된 메뉴 순서

    private int size; // 메뉴 리스트의 사이즈

    @Builder
    public MenuOrderUpdateDTO(Long menuCategoryId, List<Long> beforeMenuIds, List<Long> afterMenuIds) {
        this.menuCategoryId = menuCategoryId;
        this.beforeMenuIds = beforeMenuIds;
        this.afterMenuIds = afterMenuIds;
        validate();
        this.size = beforeMenuIds.size();
    }

    private void validate() {
        if (this.beforeMenuIds.size() != this.afterMenuIds.size()) {
            throw new InvalidParameterException(ErrorCode.INVALID_PARAMETER, "수정 전과 수정 된 메뉴 리스트 개수가 일치하지 않습니다.");
        }
        if (this.beforeMenuIds.isEmpty()) {
            throw new InvalidParameterException(ErrorCode.INVALID_PARAMETER, "수정 전과 수정 된 메뉴 리스트 개수가 비었습니다.");
        }
    }
}
