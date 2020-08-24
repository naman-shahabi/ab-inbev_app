package com.abinbev.config.swagger;

import io.swagger.annotations.ApiModelProperty;

public class SwaggerPageable {
    @ApiModelProperty(notes = "Number of records per page", example = "20")
    public int size;
    @ApiModelProperty(notes = "Results page you want to retrieve (0..N)", example = "0")
    public int page;
    @ApiModelProperty("Sorting criteria in the format: property(,asc|desc)." +
            "Default sort order is ascending. Multiple sort criteria are supported.")
    public String sort;
}
