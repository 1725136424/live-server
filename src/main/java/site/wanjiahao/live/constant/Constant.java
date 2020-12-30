/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package site.wanjiahao.live.constant;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Constant {
    // 跨域属性
    /**
     * 当前协议
     */
//    public static final String SCHEMAS = "https://";

    public static final String SCHEMAS = "http://";
    /**
     * 当前域名
     */
//    public static final String DOMAIN = "wanjiahao.site";

    public static final String DOMAIN = "live.com";
    /**
     * 端口
     */
    public static final String PORT = "8080";

    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";
    /**
     * 电话号码正则匹配串
     */
    public static final String REG_PHONE = "0?(13|14|15|18|17)[0-9]{9}";
    /**
     * url匹配
     */
    public static final String REG_URL = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";
    /**
     *  盐
     */
    public static final String SALT = "haogege";
    /**
     * 散列次数
     */
    public static final int num = 2;
    /**
     * 验证码redis前缀
     */
    public static final String CODE_PREFIX = "live:code";
    /**
     * 保存用户前缀
     */
    public static final String USER_PREFIX = "live:user";
    /**
     * 验证码的过期时间 5min后有效
     */
    public static final int CODE_EXPIRE = 60 * 5;
    /**
     * 验证码防刷间隔 60s
     */
    public static final int CODE_PROTECT_BRUSH_INTERVAL = 60;
    /**
     * 跨域预检请求发送间隔 1h
     */
    public static final int MAX_AGE = 60 * 60;
    /**
     * 用户保存在redis的过期时间
     */
    public final static int REDIS_USER_EXPIRE = 60 * 60 * 24 * 7;
    /**
     * Token
     */
    public static final String USER_TOKEN = "token";
    /**
     * 分类缓存前缀
     */
    public static final String CATEGORY_CACHE_PREFIX = "live:cache:category";
    /**
     * 分类缓存前缀
     */
    public static final String LIVE_CACHE_PREFIX = "live:cache:live";
    /**
     * 分类缓存前缀
     */
    public static final String LIVE_USER_CACHE_PREFIX = "live:cache:live-user";
    /**
     * 分类缓存前缀
     */
    public static final String USER_CACHE_PREFIX = "live:cache:user";
    /**
     * 菜单类型
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年11月15日 下午1:24:29
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * 分类层级枚举
     */
    public enum CategoryLevel {
        ONE(1), TWO(2), THREE(3);

        private final Integer level;

        CategoryLevel(Integer level) {
            this.level = level;
        }

        public Integer getLevel() {
            return level;
        }
    }

}
