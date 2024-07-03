package com.wow.delivery.dto.menu.category;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.InvalidParameterException;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
public class MenuCategoryOrderUpdateDTO {

    @Comment(value = "수정하는 메뉴 카테고리의 가게 ID")
    private Long shopId;

    @Comment(value = "기존의 메뉴 카테고리 순서")
    private List<Long> beforeIds;

    @Comment(value = "수정된 메뉴 카테고리 순서")
    private List<Long> afterIds;

    @Comment(value = "메뉴 카테고리 리스트의 사이즈")
    private int size;

    @Builder
    public MenuCategoryOrderUpdateDTO(Long shopId, List<Long> beforeIds, List<Long> afterIds) {
        this.shopId = shopId;
        this.beforeIds = beforeIds;
        this.afterIds = afterIds;
        validate();
        this.size = beforeIds.size();
    }

    private void validate() {
        if (this.beforeIds.size() != this.afterIds.size()) {
            throw new InvalidParameterException(ErrorCode.INVALID_PARAMETER, "수정 전과 수정 된 메뉴 카테고리 리스트 개수가 일치하지 않습니다.");
        }
        if (this.beforeIds.isEmpty()) {
            throw new InvalidParameterException(ErrorCode.INVALID_PARAMETER, "수정 전과 수정 된 메뉴 카테고리 리스트 개수가 비었습니다.");
        }
    }
}
