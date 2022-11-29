package pers.dhx_.subwaybooking.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.common.ErrorCode;
import pers.dhx_.subwaybooking.exception.BusinessException;
import pers.dhx_.subwaybooking.service.EdgeService;

import javax.annotation.Resource;

/**
 * @author dhx_
 * @className EdgeController
 * @date : 2022/11/28/ 21:17
 **/
@RestController
@RequestMapping("/edge")
public class EdgeController {
    @Resource
    EdgeService edgeService;

    @PostMapping("/add")
    public BaseResponse<String> addEdge(@RequestParam("A")String A, @RequestParam("B")String B){
        if(StringUtils.isAnyBlank(A,B)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请检查输入的参数");
        }
        return edgeService.addEdge(A,B);
    }
}
