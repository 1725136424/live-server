package site.wanjiahao.live.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import site.wanjiahao.live.valid.custom.Number;
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
@TableName("category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Null(message = "id必须为空", groups = {SaveGroup.class})
	@NotBlank(message = "id不能为空", groups = {UpdateGroup.class})
	private String id;
	/**
	 * 分类名称
	 */
	@NotBlank(message = "id不能为空", groups = {SaveGroup.class})
	private String name;
	/**
	 * 排序大小，越小越前
	 */
	@Number(groups = {SaveGroup.class})
	private Integer sort;
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
