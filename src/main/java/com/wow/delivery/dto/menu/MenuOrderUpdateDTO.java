package com.wow.delivery.dto.menu;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.InvalidParameterException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
public class MenuOrderUpdateDTO {

    @NotNull
    @Comment(value = "수정하는 메뉴 카테고리 ID")
    private Long menuCategoryId;

    @NotNull
    @Comment(value = "기존의 메뉴 순서")
    private List<Long> beforeMenuIds;

    @NotNull
    @Comment(value = "수정된 메뉴 순서")
    private List<Long> afterMenuIds;

    @Comment(value = "메뉴 리스트의 사이즈")
    private int size;

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
