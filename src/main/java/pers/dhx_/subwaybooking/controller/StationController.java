package pers.dhx_.subwaybooking.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import pers.dhx_.subwaybooking.DTO.AddStationParam;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.common.ErrorCode;
import pers.dhx_.subwaybooking.common.ResultUtils;
import pers.dhx_.subwaybooking.exception.BusinessException;
import pers.dhx_.subwaybooking.model.Station;
import pers.dhx_.subwaybooking.service.StationService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dhx_
 * @className StationController
 * @date : 2022/11/28/ 20:27
 **/
@RestController
@RequestMapping("/station")
public class StationController {
    @Resource
    StationService stationService;

    @PostMapping("/add")
    public BaseResponse<Long> addStation(@RequestBody AddStationParam params){
        String stationName = params.getStationName();
//        String[] canGo = params.getCanGo()
        if(StringUtils.isAnyBlank(stationName)||params.getStationRoute()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请检查输入的参数");
        }
        Station station = new Station();
        station.setStationName(params.getStationName());
        station.setStationRoute(params.getStationRoute());
        stationService.save(station);
        return ResultUtils.success(station.getStationId());
    }

    @GetMapping("/get")
    public BaseResponse<List<Station>> getAllStation(){
        return ResultUtils.success(stationService.list());
    }
}
