package pers.dhx_.subwaybooking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.dhx_.subwaybooking.DTO.OrderInfo;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.model.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author dhx
* @description 针对表【t_order(订单表)】的数据库操作Service
* @createDate 2022-11-28 20:06:55
*/
public interface OrderService extends IService<Order> {

    BaseResponse<String> placeOrder(HttpServletRequest request, String a, String b);

    BaseResponse<List<OrderInfo>> getOrderInfoList();
}
