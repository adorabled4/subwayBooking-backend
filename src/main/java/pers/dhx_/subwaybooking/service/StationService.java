package pers.dhx_.subwaybooking.service;

import pers.dhx_.subwaybooking.common.BaseResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.dhx_.subwaybooking.model.Station;

/**
* @author dhx
* @description 针对表【t_station(站点表)】的数据库操作Service
* @createDate 2022-11-28 20:06:55
*/
public interface StationService extends IService<Station> {

    BaseResponse<String> addStation(String stationName, String[] canGo);
}
