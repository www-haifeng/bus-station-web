package com.shuzhi.entity.vo;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zgk
 * @description
 * @date 2019-08-11 16:13
 */
@Data
public class JsonVo {

    private Set<Integer> id = new HashSet<>();

    private Set<String> source = new HashSet<>();
}
