package site.wanjiahao.live.valid.validatior;

import cn.hutool.core.util.NumberUtil;
import site.wanjiahao.live.valid.custom.Number;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckNumberValidator implements ConstraintValidator<Number, Integer> {

    public int max;

    public int min;

    /**
     * 初始化验证数据， 可以初始化一些验证数据，必须初始化指定数据的集合
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(Number constraintAnnotation) {
        // 获取@Number自定义数据
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return NumberUtil.isNumber(value.toString()) && (value >= min && value <= max);
    }
}
