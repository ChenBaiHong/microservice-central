package com.baimicro.central.platform.component.utils;

import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.exception.BusinessException;
import com.baimicro.central.platform.pojo.enums.PlatformExcEnum;

/**
 * @ClassName PlatformExcepUtil
 * @Description TODO
 * @Author baiHoo.chen
 * @Date 2020/3/18 15:02
 */
public class PlatformExcepUtil {


    public static BusinessException throwBusinessException(PlatformExcEnum excEnum, String message) {
        return new BusinessException(excEnum.getCode(), message);
    }

    public static BusinessException throwBusinessException(PlatformExcEnum excEnum) {

        return new BusinessException(excEnum.getCode(), excEnum.getMessage());
    }

    public static BusinessException throwBusinessException(String message) {

        return new BusinessException(CommonConstant.SC_ERR, message);
    }
}
