package site.wanjiahao.live.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import site.wanjiahao.live.valid.group.SaveGroup;
import site.wanjiahao.live.valid.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

/**
 * 
 * 
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@Data
@TableName("live")
public class LiveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 直播间id号
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 关联的身份证号
	 */
	private String idCard;
	/**
	 * app_name
	 */
	private String appName;
	/**
	 * stream_name
	 */
	private String streamName;
	/**
	 * 标题
	 */

	private String title;
	/**
	 * 播流地址 各种清晰度的播流地址分号间隔
	 */
	private String playUrl;
	/**
	 * 推流地址
	 */
	private String pushUrl;
	/**
	 * 热度
	 */
	private String hot;
	/**
	 * 分类id
	 */
	private String cid;
	/**
	 * 直播间状态 0-未开播 1-已开播
	 */
	private Integer status;
	/**
	 * 乐观锁字段
	 */
	@Version
	private Integer version;
	/**
	 * 逻辑删除
	 */
	private Integer deleted;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
