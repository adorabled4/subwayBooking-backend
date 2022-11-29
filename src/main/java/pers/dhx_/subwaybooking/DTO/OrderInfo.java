package pers.dhx_.subwaybooking.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author dhx_
 * @className OrderInfo
 * @date : 2022/11/29/ 21:51
 **/
@Data
public class OrderInfo {
    private static final long serialVersionUID = 16435345234654545L;

    /**
     *
     */
    private Long orderId;

    /**
     * 用户id
     */
    private String userName;

    /**
     * 花费
     */
    private Integer cost;

    /**
     * 出发站点id
     */
    private String startName;

    /**
     * 目的地站点id
     */
    private String desName;

    /**
     * 创建时间
     */
    private Date createTime;

}
