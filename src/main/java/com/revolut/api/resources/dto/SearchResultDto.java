package com.revolut.api.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto<T> extends BaseResponseDto {
    private List<T> result;

    public SearchResultDto(boolean success, List<T> result) {
        super.setSuccess(success);
        this.result = result;
    }

}
