package com.wudgaby.apiautomatic.event;

import com.wudgaby.apiautomatic.dto.ApiDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/26 0026 11:17
 * @desc :
 */
@Data
@AllArgsConstructor
public class ApiEvent {
    private Collection<ApiDTO> apiDTOList;
}
