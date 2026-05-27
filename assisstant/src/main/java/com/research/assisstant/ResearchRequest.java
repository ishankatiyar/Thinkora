package com.research.assisstant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResearchRequest {
    private String content;
    private String operation;
}
