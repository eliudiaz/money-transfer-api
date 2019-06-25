package com.revolut.api.resources.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResultDto<T> extends BaseResponseDto {
    private List<T> result;

    public SearchResultDto(boolean success, List<T> result) {
        super.setSuccess(success);
        this.result = result;
    }

}
