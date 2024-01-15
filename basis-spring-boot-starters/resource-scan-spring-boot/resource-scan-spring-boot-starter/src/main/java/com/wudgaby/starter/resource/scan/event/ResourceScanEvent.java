package com.wudgaby.starter.resource.scan.event;

import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
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
public class ResourceScanEvent {
    private Collection<ResourceDefinition> resourceDefinitionList;
}
