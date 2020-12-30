package site.wanjiahao.live.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.valid.group.SaveGroup;
import site.wanjiahao.live.valid.group.UpdateGroup;

import javax.validation.constraints.*;

/**
 * 
 * 
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Null(message = "id必须为空", groups = {SaveGroup.class})
	@NotBlank(message = "id不能为空", groups = {UpdateGroup.class})
	private String id;
	/**
	 * 用户昵称
	 */
	@NotBlank(message = "昵称不能为空", groups = {SaveGroup.class})
	private String username;
	/**
	 * 用户密码
	 */
	@NotBlank(message = "密码不能为空", groups = {SaveGroup.class})
	@Size(min = 8, max = 15, message = "密码长度不符合", groups = {SaveGroup.class})
	private String password;
	/**
	 * 用户头像
	 */
	@NotBlank(message = "头像不能为空", groups = {SaveGroup.class})
	@Pattern(regexp = Constant.REG_URL,
			message = "图片地址格式不正确",
			groups = {SaveGroup.class})
	private String pic;
	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空", groups = {SaveGroup.class})
	@Pattern(regexp = Constant.REG_PHONE,
			message = "手机号格式不正确",
			groups = {SaveGroup.class})
	private String phone;
	/**
	 * 真实姓名
	 */
	private String name;
	/**
	 * 身份证号码
	 */
	private String idCard;
	/**
	 * 用户状态0-普通用户 1-等待审核 2-主播 3-管理员
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
