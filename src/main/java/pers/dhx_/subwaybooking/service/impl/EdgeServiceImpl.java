package pers.dhx_.subwaybooking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import pers.dhx_.subwaybooking.common.ResultUtils;
import pers.dhx_.subwaybooking.exception.BusinessException;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.common.ErrorCode;
import pers.dhx_.subwaybooking.model.Edge;
import pers.dhx_.subwaybooking.model.Station;
import pers.dhx_.subwaybooking.service.EdgeService;
import pers.dhx_.subwaybooking.mapper.EdgeMapper;
import org.springframework.stereotype.Service;
import pers.dhx_.subwaybooking.service.StationService;

import javax.annotation.Resource;

/**
* @author dhx
* @description 针对表【t_edge(路线表)】的数据库操作Service实现
* @createDate 2022-11-28 20:05:44
*/
@Service
public class EdgeServiceImpl extends ServiceImpl<EdgeMapper, Edge>
    implements EdgeService{

    @Resource
    StationService stationService;
    @Override
    public BaseResponse<String> addEdge(String a, String b) {
        Station station_A = stationService.query().eq("station_name", a).one();
        Station station_B = stationService.query().eq("station_name", b).one();
        if(station_A==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,a+" 站点不存在!");
        }else if(station_B==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,b+" 站点不存在!");
        }
        Edge edge = new Edge(station_A.getStationId(), station_B.getStationId());
        if(query().eq("A",edge.getA()).eq("B",edge.getB()).count()
                +query().eq("B",edge.getA()).eq("A",edge.getB()).count()>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"路线已经存在了!");
        }
        boolean result = save(edge);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"添加路线失败!");
        }
        return ResultUtils.success("添加成功");
    }
}




