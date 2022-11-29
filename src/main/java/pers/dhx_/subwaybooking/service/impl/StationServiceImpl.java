package pers.dhx_.subwaybooking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.common.ErrorCode;
import pers.dhx_.subwaybooking.common.ResultUtils;
import pers.dhx_.subwaybooking.exception.BusinessException;
import pers.dhx_.subwaybooking.model.Station;
import pers.dhx_.subwaybooking.service.StationService;
import pers.dhx_.subwaybooking.mapper.StationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author dhx
* @description 针对表【t_station(站点表)】的数据库操作Service实现
* @createDate 2022-11-28 20:06:55
*/
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station>
    implements StationService{

    @Override
    public BaseResponse<String> addStation(String stationName, String[] canGo) {
        List<Long> ids=new ArrayList<>();
        Station station = new Station(stationName);
        if(query().eq("station_name",stationName).count()!=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"以及存在该站点了!");
        }
        baseMapper.insert(station);
        Long id= station.getStationId();
        // 遍历站点 , 如果不存在就添加站点
        for (String go : canGo) {
            Station tmpStation = new Station(go);
            if (query().eq("station_name",go).count()==0){
                int result = this.baseMapper.insert(tmpStation);
            }else{
                tmpStation=query().eq("station_name",go).getEntity();
            }
//            ids.add(tmpStation.getStationId());
        }
        // 添加 路线
//        for (Long canGoId : ids) {
//            Edge edgeA = new Edge(id, canGoId);
//            Edge edgeB = new Edge(canGoId,id);
//            edgeA.setCost(2); // 默认票价是两元
//            edgeB.setCost(2); // 默认票价是两元
//            edgeService.getBaseMapper().insert(edgeA);
//            edgeService.getBaseMapper().insert(edgeB);
//        }
        return ResultUtils.success("添加成功!");
    }
}




