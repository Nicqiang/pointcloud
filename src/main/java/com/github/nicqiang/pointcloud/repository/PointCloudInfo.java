package com.github.nicqiang.pointcloud.repository;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-11
 */
@Entity
@Data
@Table(name = "point_cloud")
@EntityListeners(AuditingEntityListener.class)
public class PointCloudInfo implements Serializable {
    private static final long serialVersionUID = 780459857552728094L;

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue
    @Column(name = "name", nullable = false)
    private Long id;

    /**
     * 点云名称
     */
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    /**
     * 原始点路径
     */
    @Column(name = "origin_path", length = 255, nullable = false)
    private String originPath;

    /**
     * 原始点个数
     */
    @Column(name = "origin_point_num", length = 12, nullable = true)
    private Long originPointNum;


    /**
     * 简化后点处理的路劲
     */
    @Column(name = "simplify_path", length = 255, nullable = true)
    private String simplifyPath;

    /**
     * 简化后点的个数
     */
    @Column(name = "simplify_point_num", length = 12, nullable = true)
    private Long simplifyPointNum;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /**
     * 修改日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "modify_time", nullable = false)
    private Date modifyTime;

    /**
     * 备注
     */
    @Column(name = "remark", nullable = true)
    private String remark;

}
