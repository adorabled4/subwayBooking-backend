package pers.dhx_.subwaybooking.service;

import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.model.Edge;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dhx
* @description 针对表【t_edge(路线表)】的数据库操作Service
* @createDate 2022-11-28 20:05:44
*/
public interface EdgeService extends IService<Edge> {

    BaseResponse<String> addEdge(String a, String b);
}
