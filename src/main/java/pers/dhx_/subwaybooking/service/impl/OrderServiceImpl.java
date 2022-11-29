package pers.dhx_.subwaybooking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.dhx_.subwaybooking.DTO.OrderInfo;
import pers.dhx_.subwaybooking.common.BaseResponse;
import pers.dhx_.subwaybooking.common.ResultUtils;
import pers.dhx_.subwaybooking.model.Edge;
import pers.dhx_.subwaybooking.model.Order;
import pers.dhx_.subwaybooking.model.Station;
import pers.dhx_.subwaybooking.model.User;
import pers.dhx_.subwaybooking.service.EdgeService;
import pers.dhx_.subwaybooking.service.OrderService;
import pers.dhx_.subwaybooking.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import pers.dhx_.subwaybooking.service.StationService;
import pers.dhx_.subwaybooking.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pers.dhx_.subwaybooking.contant.UserConstant.USER_LOGIN_STATE;

/**
* @author dhx
* @description 针对表【t_order(订单表)】的数据库操作Service实现
* @createDate 2022-11-28 20:06:55
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Resource
    StationService stationService;

    @Resource
    EdgeService edgeService;


    @Resource
    UserService userService;


    boolean [][]vis;
    int [][]graph;

    Map<Integer,Long> idx2id;

    LinkedList<LinkedList<Station> > tracks;
    StringBuilder message; // 统计乘车信息
    StringBuilder transfer;//统计需要换乘的站点
    final static Map<Integer,Integer> costMap ;
    static{
        //2+(min-1)/2 规律如下
        costMap=new HashMap<>();
        costMap.put(1,2);
        costMap.put(2,2);
        costMap.put(3,3);
        costMap.put(4,3);
        costMap.put(5,4);
        costMap.put(6,4);
        costMap.put(7,5);
        costMap.put(8,5);
        costMap.put(9,6);
        costMap.put(10,6);
    }
    // 从数据库中取出站点的数据  , 然后初始化 邻接矩阵
    private void init(){
        List<Station> stations = stationService.list();
        idx2id =new HashMap<>();
        int idx =0 ;
        for (Station station : stations) { // 下标映射 station
            idx2id.put(idx++,station.getStationId());
        }
        List<Edge> edges = edgeService.list();
        vis= new boolean[stations.size()][stations.size()];
        graph= new int[stations.size()][stations.size()];
        for (Edge edge : edges) {
            Long a = edge.getA();
            Long b = edge.getB();
            Station A = stationService.getById(a);
            Station B = stationService.getById(b);
            graph[A.getStationIndex()][B.getStationIndex()]=1;
            graph[B.getStationIndex()][A.getStationIndex()]=1;
        }
    }

    @Override
    public BaseResponse<String> placeOrder(HttpServletRequest request, String a, String b) {
        init();
        Station stationA = stationService.query().eq("station_name", a).one();
        Station stationB = stationService.query().eq("station_name", b).one();
        // 计算最少花费
        int cost = selectCost(stationA, stationB);
        //统计用户的乘车路线
        message=new StringBuilder("您乘车的路线为: {");
        int preStationRoute=tracks.get(0).get(0).getStationRoute();
        for (Station station : tracks.get(0)) {
            message.append(station.getStationName())
                    .append(preStationRoute == station.getStationRoute() ? (" 您需要在本站换乘 " + station.getStationRoute() + " 线") : "")
                    .append(" ,");
            preStationRoute=station.getStationRoute();
        }
        message.append("}");
        // 获取当前登录用户
        Object object = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser= (User)object;
        //保存订单信息
        Order order = new Order();
        order.setOrderId(RandomUtil.randomLong());
        order.setUserId(currentUser.getUserId());
        order.setCost(cost);
        order.setStartId(stationA.getStationId());
        order.setDesId(stationB.getStationId());
        //写入订单到数据库
        save(order);
        return ResultUtils.success("下单成功 , 您本次消费消费"+cost+"元"+message);
    }


    @Override
    public BaseResponse<List<OrderInfo>> getOrderInfoList() {
        List<Order> list = list();
        List<OrderInfo> orderInfos = list.stream().map(order -> {
            // 查询orderInfo 相关信息
            OrderInfo orderInfo = BeanUtil.copyProperties(order, OrderInfo.class);
            Station start = stationService.getById(order.getStartId());
            Station des = stationService.getById(order.getDesId());
            User user = userService.getById(order.getUserId());
            if(user!=null){
                orderInfo.setUserName(user.getUserName());
            }else{
                orderInfo.setUserName("user-lost");
            }
            orderInfo.setStartName(start.getStationName());
            orderInfo.setDesName(des.getStationName());
            return orderInfo;
        }).collect(Collectors.toList());
        return ResultUtils.success(orderInfos);
    }

    private int selectCost(Station A, Station B){
        int idxA=A.getStationIndex();
        int idxB=B.getStationIndex();
        tracks =new LinkedList<>();
        dfs(new LinkedList<>(),idxA,idxB);
        int min=Integer.MAX_VALUE;
        int idx =0 ;
        for (int i = 0; i < tracks.size(); i++) {
            LinkedList<Station> track =tracks.get(i);
            if(track.size()<min){
                min=track.size();
                idx = i;
            }
        }
        //重新赋值, 把最短路径 置于 索引为0处
        LinkedList<Station> minTrack= tracks.get(idx);
        tracks=new LinkedList<>();
        tracks.add(minTrack);
        return 2+(min-1)/2;
    }
    /**
     * 递归求  需要走的站点的数量 => 对应需要的 cost
     * @param now
     * @param des
     * @return
     */
    void dfs(LinkedList<Station> track,int now, int des){
        track.add(stationService.query().eq("station_index",now).one());// 保存当前到达的站点
        if(now == des){//走到了目的地
            tracks.add(new LinkedList<>(track));// 保存路线
            return;
        }
        for (int i = 0; i < graph[now].length; i++) {
            if(now==i){
                continue;
            }
            if(graph[now][i]==1&& !vis[now][i]&&!vis[i][now]){// 需要有路径, 并且走过的路不能走, 也不能往回走
                vis[now][i]=true;//当前路线走过
                vis[i][now]=true;//不能往回走
                dfs(track,i,des);
                vis[now][i]=false;
                vis[i][now]=false;
                track.removeLast();
            }
        }
    }


}




