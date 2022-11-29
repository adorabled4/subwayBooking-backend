package pers.dhx_.subwaybooking.controller;

import org.springframework.web.bind.annotation.*;
import pers.dhx_.subwaybooking.DTO.OrderInfo;
import pers.dhx_.subwaybooking.DTO.PlaceOrderParam;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.common.ErrorCode;
import pers.dhx_.subwaybooking.exception.BusinessException;
import pers.dhx_.subwaybooking.model.User;
import pers.dhx_.subwaybooking.service.OrderService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static pers.dhx_.subwaybooking.contant.UserConstant.ADMIN_ROLE;
import static pers.dhx_.subwaybooking.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author dhx_
 * @className OrderController
 * @date : 2022/11/28/ 21:37
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @PostMapping("/placeOrder")
    public BaseResponse<String> placeOrder(HttpServletRequest request, @RequestBody PlaceOrderParam orderParams){
        String A= orderParams.getStart();
        String B=orderParams.getDes();
        return orderService.placeOrder(request,A,B);
    }

    @GetMapping("/list")
    public BaseResponse<List<OrderInfo>> getOrderList(HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return orderService.getOrderInfoList();
    }


    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getIsAdmin() == ADMIN_ROLE;
    }
}
