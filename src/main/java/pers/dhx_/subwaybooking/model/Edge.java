package pers.dhx_.subwaybooking.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 路线表
 * @TableName t_edge
 */
@TableName(value ="t_edge")
@Data
public class Edge implements Serializable {
    /**
     * 路线id
     */
    @TableId(type = IdType.AUTO)
    private Long edgeId;

    /**
     * 起始地点
     */
    private Long B;

    /**
     * 可到达地点
     */
    private Long A;

    /**
     * 花费
     */
    private Integer cost=2;

    /**
     * 
     */
    private Date createTime;
    public Edge(){}

    public Edge(Long id ,Long canGoId){
        this.B=id;
        this.A=canGoId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", edgeId=").append(edgeId);
        sb.append(", startId=").append(B);
        sb.append(", desId=").append(A);
        sb.append(", cost=").append(cost);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}